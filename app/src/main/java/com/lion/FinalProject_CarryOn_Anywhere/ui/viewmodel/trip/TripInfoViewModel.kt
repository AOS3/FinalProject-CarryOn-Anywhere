package com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.trip

import android.content.Context
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.lion.FinalProject_CarryOn_Anywhere.CarryOnApplication
import com.lion.FinalProject_CarryOn_Anywhere.component.ChipState
import com.lion.FinalProject_CarryOn_Anywhere.data.api.TourAPI.TourAPIInterface
import com.lion.FinalProject_CarryOn_Anywhere.data.api.TourAPI.TourAPIRetrofitClient
import com.lion.FinalProject_CarryOn_Anywhere.data.api.TourAPI.TourApiModel
import com.lion.FinalProject_CarryOn_Anywhere.data.server.model.TripModel
import com.lion.FinalProject_CarryOn_Anywhere.data.server.service.TripService
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.ScreenName
import com.lion.FinalProject_CarryOn_Anywhere.ui.screen.trip.Place
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class TripInfoViewModel @Inject constructor(
    @ApplicationContext context: Context,
    val tripService: TripService
) : ViewModel(){

    val carryOnApplication = context as CarryOnApplication

    // ViewModel
    var selectedPlaces = mutableStateListOf<LatLng>() // 선택된 장소 리스트
    var selectedPlaceLocation = mutableStateOf(LatLng(35.8714, 128.6014)) // 기본 위치 (대구)

    // 다이얼로그 상태
    val deletePlanDialogState = mutableStateOf(false)
    val editTripNameDialogState = mutableStateOf(false)
    val deletePlaceDialogState = mutableStateOf(false)

    var deleteTargetPlace = mutableStateOf<Place?>(null)

    // 검색 키워드
    val searchTextFieldValue = mutableStateOf("")

    var currentTripName = mutableStateOf("여행1")
    val editTripNameTextFieldValue = mutableStateOf("")

    // 바텀시트 상태
    val showBottomSheet = mutableStateOf(false)

    // 시/도 목록
    private val _regions = mutableStateOf<List<String>>(emptyList())
    val regions: State<List<String>> = _regions

    // 구/군 목록
    private val _subRegionsMap = mutableStateOf<Map<String, List<String>>>(emptyMap())
    val subRegionsMap: State<Map<String, List<String>>> = _subRegionsMap

    // 선택된 지역 리스트
    var selectedRegions = mutableStateListOf<ChipState>()

    private var regionCodeMap: MutableMap<String, String> = mutableMapOf()

    // 구/군 이름 + 시군구 코드 저장 (Firebase 저장 용도)
    private val _subRegionsCodeMap = mutableStateOf<Map<String, List<Map<String, String>>>>(emptyMap())
    val subRegionsCodeMap: State<Map<String, List<Map<String, String>>>> = _subRegionsCodeMap

    private fun fetchRegions() {
        viewModelScope.launch {
            try {
                val apiKey = "6d5mkmqFyluWJNMUzIer6qA43/S6w+LWlCCspcQwyeSs9fesUnARurM+nBCqBxQ982Sl0OoHXILuM8nFrjKsjQ=="
                val response = TourAPIRetrofitClient.instance.getRegionCodes(apiKey)

                val rawRegionList = response.body()?.response?.body?.items?.item
                val processedRegionList = processRegionList(rawRegionList)

                // 가공된 시/도명 리스트
                _regions.value = processedRegionList.map { it.first }
                // Map → MutableMap 변환
                regionCodeMap = processedRegionList.toMap().toMutableMap()
            } catch (e: Exception) {
                Log.e("TripInfoViewModel", "Error fetching regions", e)
            }
        }
    }

    // API 응답을 가공하는 함수 추가
    fun processRegionList(apiRegionList: List<TourApiModel.AreaCodeItem>?): List<Pair<String, String>> {
        val regionMap = mutableMapOf<String, String>()

        apiRegionList?.forEach { item ->
            val code = item.code ?: return@forEach
            val name = item.name ?: return@forEach

            // 이름 변경 규칙 적용
            val newName = when (name) {
                "세종특별자치시" -> "세종"
                "강원특별자치도" -> "강원"
                "경기도" -> "경기"
                "충청북도", "충청남도" -> "충청"
                "경상북도", "경상남도" -> "경상"
                "전북특별자치도", "전라남도" -> "전라"
                "제주도" -> "제주"
                else -> name
            }

            // 같은 이름이 여러 번 나오면 첫 번째 지역코드를 유지
            if (!regionMap.containsKey(newName)) {
                regionMap[newName] = code
            }
        }

        return regionMap.map { it.key to it.value }
    }


    fun fetchSubRegions(regionName: String) {
        viewModelScope.launch {
            try {
                val apiKey = "6d5mkmqFyluWJNMUzIer6qA43/S6w+LWlCCspcQwyeSs9fesUnARurM+nBCqBxQ982Sl0OoHXILuM8nFrjKsjQ=="
                val areaCodes = getRegionCode(regionName).split(",").map { it.trim() }

                if (areaCodes.isEmpty() || areaCodes.contains("0")) {
                    return@launch
                }

                val allSubRegions = mutableListOf<Map<String, String>>() // 시/군/구 데이터 저장 리스트

                val responses = areaCodes.map { areaCode ->
                    async(Dispatchers.IO) {
                        TourAPIRetrofitClient.instance.getSubRegions(
                            serviceKey = apiKey,
                            areaCode = areaCode
                        )
                    }
                }.awaitAll() // 🚀 여러 지역 코드 동시 요청

                responses.forEachIndexed { index, response ->
                    if (response.isSuccessful) {
                        val currentRegionCode = areaCodes[index] // 현재 요청한 지역 코드

                        val subRegionList = response.body()?.response?.body?.items?.item?.map {
                            mapOf(
                                "subRegionName" to (it.name ?: "알 수 없음"), // 구/군 이름
                                "subRegionCode" to (it.code ?: "0"), // 구/군 코드
                                "regionCode" to currentRegionCode // 현재 요청한 지역 코드 매핑
                            )
                        } ?: emptyList()

                        allSubRegions.addAll(subRegionList)
                    } else {
                        Log.e("TripInfoViewModel", "HTTP Error: ${response.code()} - ${response.message()}")
                    }
                }

                // 조회된 모든 구/군 리스트를 저장 (이름-코드 매핑)
                _subRegionsMap.value = _subRegionsMap.value.toMutableMap().apply {
                    put(regionName, allSubRegions.map { it["subRegionName"] ?: "알 수 없음" })
                }
                _subRegionsCodeMap.value = _subRegionsCodeMap.value.toMutableMap().apply {
                    put(regionName, allSubRegions)
                }

            } catch (e: Exception) {
                Log.e("TripInfoViewModel", "Error fetching sub-regions", e)
            }
        }
    }

    fun getRegionCode(regionName: String): String {
        // 시/군/구가 포함된 경우 시/도만 추출
        val parsedRegion = regionName.split(" ")[0] // "서울시 강서구" → "서울시"
            .replace("서울시", "서울") // 서울시 → 서울
            .replace("부산시", "부산")
            .replace("대구시", "대구")
            .replace("인천시", "인천")
            .replace("광주시", "광주")
            .replace("대전시", "대전")
            .replace("울산시", "울산")
            .replace("세종시", "세종")
            .replace("특별자치도", "제주") // 제주도 변환 추가
            .replace("도$", "") // "충청도", "경상도" → "충청", "경상"

        val regionCode = when (parsedRegion) {
            "서울" -> "1"
            "부산" -> "6"
            "대구" -> "4"
            "인천" -> "2"
            "광주" -> "5"
            "대전" -> "3"
            "울산" -> "7"
            "세종" -> "8"
            "경기" -> "31"
            "강원" -> "32"
            "충청" -> "33,34"
            "경상" -> "35,36"
            "전라" -> "37,38"
            "제주" -> "39"
            else -> regionCodeMap[parsedRegion] ?: "0"
        }

        return regionCode
    }

    fun parseRegionName(regionName: String): String {
        return regionName.split(" ")[0]
            .replace("서울시", "서울")
            .replace("부산시", "부산")
            .replace("대구시", "대구")
            .replace("인천시", "인천")
            .replace("광주시", "광주")
            .replace("대전시", "대전")
            .replace("울산시", "울산")
            .replace("세종시", "세종")
            .replace("경기도", "경기")
            .replace("강원도", "강원")
            .replace("충청도", "충청")
            .replace("경상도", "경상")
            .replace("전라도", "전라")
            .replace("제주특별자치도", "제주")
    }

    fun extractSubRegion(fullRegionName: String): String {
        // 예: "서울시 강북구" → "강북구"
        return fullRegionName.split(" ").last()
    }

    val isButtonRegionEnabled = mutableStateOf(false)

    fun updateRegionButtonState() {
        isButtonRegionEnabled.value = selectedRegions.isNotEmpty()
    }

    // 날짜 선택
    var startDate = mutableStateOf<Long?>(null)
    var endDate = mutableStateOf<Long?>(null)

    val dateFormatter = SimpleDateFormat("yyyy. M. d", Locale.getDefault())

    val planDateFormatter = SimpleDateFormat("M.d (E)", Locale.KOREA)

    var formattedStartDate = mutableStateOf("")
    var formattedEndDate = mutableStateOf("")

    // 날짜가 변경될 때 자동 업데이트
    fun updateFormattedDates() {
        formattedStartDate.value = startDate.value?.let { dateFormatter.format(Date(it)) } ?: ""
        formattedEndDate.value = endDate.value?.let { dateFormatter.format(Date(it)) } ?: ""
    }

    // 데이터를 가져와 상태 관리 변수에 담아준다.
    fun gettingTripData(tripDocumentId:String){
        // 서버에서 데이터를 가져온다.
        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO) {
                tripService.selectTripDataOneById(tripDocumentId)
            }
            tripModel = work1.await()

            startDate.value = tripModel.tripStartDate
            endDate.value = tripModel.tripEndDate
        }
    }

    // 날짜별 리스트 자동 생성
    var tripDays = mutableStateListOf<String>()

    fun updateTripDays() {
        tripDays.clear()
        val start = startDate.value ?: return
        val end = endDate.value ?: return

        val calendar = Calendar.getInstance().apply { timeInMillis = start }
        while (calendar.timeInMillis <= end) {
            tripDays.add(planDateFormatter.format(calendar.time)) // "3.8 (토)" 형식
            calendar.add(Calendar.DAY_OF_MONTH, 1) // 하루 증가
        }
    }

    // 일별 장소선택
    var placesByDay = mutableStateMapOf<String, MutableList<Place>>()
    var selectedDay = mutableStateOf("")

    // 장소 검색화면에서 선택을 눌렀을때
    fun addPlaceToDay(day: String, place: Place, documentId: String) {
        if (day.isEmpty()) {
            println("오류: 추가할 날짜가 없음!")
            return
        }

        val placesList = placesByDay[day]

        // 마지막으로 추가된 장소와 현재 추가할 장소가 동일하면 추가하지 않음
        if (placesList?.isNotEmpty() == true && placesList.last().title == place.title) {

            Toast.makeText(carryOnApplication, "동일한 장소를 연속으로 추가할 수 없습니다.", Toast.LENGTH_SHORT).show()

            carryOnApplication.navHostController.popBackStack()
            carryOnApplication.navHostController.navigate("${ScreenName.ADD_TRIP_PLAN.name}/$documentId")

            return
        }

        // placesByDay에 장소 추가
        if (placesList != null) {
            placesList.add(place)
        } else {
            placesByDay[day] = mutableStateListOf(place)
        }

        // 새로운 장소 좌표를 selectedPlaces 리스트에 추가
        val newLocation = LatLng(place.latitude, place.longitude)
        selectedPlaces.add(newLocation)

        // 지도 중심을 새 장소로 이동
        selectedPlaceLocation.value = newLocation

        println("장소 추가됨: ${place.title}, 날짜: $day")

        carryOnApplication.navHostController.popBackStack()
        carryOnApplication.navHostController.navigate("${ScreenName.ADD_TRIP_PLAN.name}/$documentId")
    }

    fun removePlaceFromDay(day: String, place: Place) {
        placesByDay[day]?.let { places ->
            // 장소 삭제
            places.remove(place)

            // 만약 해당 날짜에 장소가 없다면, 리스트에서 제거
            if (places.isEmpty()) {
                placesByDay.remove(day)
            }

            // `selectedPlaces`에서도 해당 장소의 좌표 삭제
            selectedPlaces.removeIf { it.latitude == place.latitude && it.longitude == place.longitude }

            // 남아있는 장소가 있으면 마지막 장소로 지도 중심 이동
            selectedPlaceLocation.value = if (selectedPlaces.isNotEmpty()) {
                selectedPlaces.last()
            } else {
                LatLng(35.8714, 128.6014) // 기본값 (대구)
            }

            println("🗑 장소 삭제됨: ${place.title}, 날짜: $day")
        }
    }

    // 원본 장소 리스트
    private val allPlaces = listOf(
        Place("https://image-url.com/image1.jpg", "관암사 (대구)", "관광지", "대구광역시 동구", 35.977827, 128.733872),
        Place("https://image-url.com/image2.jpg", "서울 N타워", "관광지", "서울특별시 용산구", 37.551187, 126.988240),
        Place("https://image-url.com/image3.jpg", "홍대", "관광지", "서울특별시 마포구", 37.558182, 126.926180),
        Place("https://image-url.com/image4.jpg", "서울 경복궁", "관광지", "서울특별시 종로구", 37.579808, 126.977756),
        Place("https://image-url.com/image5.jpg", "부산 해운대", "해변", "부산광역시 해운대구", 35.172465, 129.175751),
        Place("https://image-url.com/image6.jpg", "을왕리 해수욕장", "해변", "인천광역시 중구", 37.448277, 126.374478),
        Place("https://image-url.com/image7.jpg", "경주 첨성대", "관광지", "경상도 경주시", 35.894480, 129.324250),
        Place("https://image-url.com/image8.jpg", "서울 강남", "관광지", "서울특별시 강남구", 37.498553, 127.027764),
    )

    // 필터링된 장소 리스트
    var filteredPlaces = SnapshotStateList<Place>()

    init {
        filteredPlaces
        updateRegionButtonState()
        fetchRegions()
        fetchSubRegions("서울")
    }

    // 장소 필터링
    fun filterPlaces() {
        val query = searchTextFieldValue.value.lowercase().trim()
        // 1️selectedRegions 디버깅 로그 출력
        println("선택된 지역 목록: ${selectedRegions.joinToString { it.text }}")

        // selectedRegions 값 변환 (서울시 → 서울특별시, 부산시 → 부산광역시 등)
        val normalizedRegions = selectedRegions.map { region ->
            region.text
                .replace("서울시", "서울특별시")
                .replace("부산시", "부산광역시")
                .replace("대구시", "대구광역시")
                .replace("인천시", "인천광역시")
                .replace("광주시", "광주광역시")
                .replace("대전시", "대전광역시")
                .replace("울산시", "울산광역시")
                .replace("경기도", "경기도")
                .replace("강원도", "강원도")
                .replace("충청도", "충청도")
                .replace("전라도", "전라도")
                .replace("경상도", "경상도")
                .replace("제주시", "제주특별자치도")
        }

        // 변환된 지역명으로 필터링 (포함 여부 확인)
        val filteredByRegion = if (selectedRegions.isNotEmpty()) {
            allPlaces.filter { place ->
                normalizedRegions.any { region ->
                    // 포함 여부 체크
                    place.location.lowercase().contains(region.lowercase())
                }
            }
        } else {
            // 선택된 지역이 없으면 모든 장소 사용
            allPlaces
        }

        // 검색어 기반으로 한 번 더 필터링
        filteredPlaces.clear()
        filteredPlaces.addAll(
            filteredByRegion.filter { place ->
                place.title.lowercase().contains(query) ||
                        place.subtitle.lowercase().contains(query) ||
                        place.location.lowercase().contains(query)
            }
        )
    }

    val selectedIndex = mutableStateOf<Int?>(null)

    fun reorderPlaces(day: String, fromIndex: Int, toIndex: Int) {
        placesByDay[day]?.let { list ->
            if (fromIndex in list.indices && toIndex in list.indices) {
                val movedItem = list.removeAt(fromIndex)
                list.add(toIndex, movedItem)
                println("장소 순서 변경: $fromIndex -> $toIndex")
            }
        }
    }

    fun calculateDistance(start: LatLng, end: LatLng): Float {
        val results = FloatArray(1)
        Location.distanceBetween(
            start.latitude, start.longitude,
            end.latitude, end.longitude,
            results
        )
        // 미터(m) 단위 거리 반환
        return results[0]
    }

    fun deletePlanOnClick(){

    }

    fun completeRegionOnClick() {
        carryOnApplication.previousScreen.value = ScreenName.SELECT_TRIP_REGION.name
        carryOnApplication.navHostController.popBackStack()

        // ✅ `tripDocumentId` 없이 이동 가능하도록 변경
        carryOnApplication.navHostController.navigate("${ScreenName.SELECT_TRIP_DATE.name}")
    }

    // 날짜 선택 버튼 눌렀을 때
    fun completeDateOnClick(){
        val tripModel = TripModel()
        tripModel.tripTitle = "여행1"

        // 선택된 지역 정보 리스트 변환 (시/도 & 선택된 구/군만 저장)
        tripModel.tripCityList = selectedRegions.mapNotNull { region ->
            val regionName = region.text
            val parsedRegion = parseRegionName(regionName) // 🔹 "서울시 강북구" → "서울"
            val regionCodeList = getRegionCode(parsedRegion).split(",") // 🔹 "33,34" → ["33", "34"]

            // 사용자가 선택한 "구/군"만 필터링
            val selectedSubRegionName = extractSubRegion(regionName) // 🔹 "강북구"
            val matchedSubRegion = subRegionsCodeMap.value[parsedRegion]
                ?.find { it["subRegionName"] == selectedSubRegionName }

            matchedSubRegion?.let { subRegion ->
                // subRegionCode`가 포함된 `regionCodeList`에서 해당되는 코드 찾기
                val correctRegionCode = regionCodeList.find { it == subRegion["regionCode"] } ?: regionCodeList.first()

                mapOf(
                    "regionName" to parsedRegion,
                    "regionCode" to correctRegionCode, // 🔹 구/군이 속하는 정확한 `regionCode`
                    "subRegionName" to subRegion["subRegionName"],
                    "subRegionCode" to subRegion["subRegionCode"]
                )
            }
        }.toMutableList()

        if (endDate.value == null) {
            tripModel.tripStartDate = startDate.value!!
        } else {
            tripModel.tripStartDate = startDate.value!!
            tripModel.tripEndDate = endDate.value!!
        }

        // Firebase 저장 실행
        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO) {
                tripService.addTripData(tripModel)
            }

            val documentId = work1.await()

            if (documentId != null) {
                // `ADD_TRIP_PLAN`으로 `documentId` 전달하며 이동
                carryOnApplication.navHostController.popBackStack()
                carryOnApplication.navHostController.navigate("${ScreenName.ADD_TRIP_PLAN.name}/$documentId")
            }
        }
    }

    // 서버에서 받아온 데이터를 담을 변수
    lateinit var tripModel: TripModel

    fun updateDateOnClick(tripDocumentId:String) {
        CoroutineScope(Dispatchers.Main).launch {
            tripModel.tripStartDate = startDate.value!!
            tripModel.tripEndDate = endDate.value!!
            val work3 = async(Dispatchers.IO) {
                tripService.updateTripDate(tripModel)
            }
            work3.join()

            Toast.makeText(carryOnApplication, "수정이 완료되었습니다", Toast.LENGTH_SHORT).show()
            // `ADD_TRIP_PLAN`으로 `documentId` 전달하며 이동
            carryOnApplication.navHostController.popBackStack()
            carryOnApplication.navHostController.navigate("${ScreenName.ADD_TRIP_PLAN.name}/$tripDocumentId")
        }
    }

    // 지도를 눌렀을 때
    fun mapOnClick(tripDocumentId:String){
        carryOnApplication.navHostController.popBackStack()
        carryOnApplication.navHostController.navigate("${ScreenName.SHOW_TRIP_MAP.name}/$tripDocumentId")
    }

    fun tripDateNavigationOnClick(tripDocumentId:String) {
        when (carryOnApplication.previousScreen.value) {
            ScreenName.SELECT_TRIP_REGION.name -> {
                carryOnApplication.navHostController.popBackStack()
                carryOnApplication.navHostController.navigate(ScreenName.SELECT_TRIP_REGION.name)
            }
            "${ScreenName.ADD_TRIP_PLAN.name}/$tripDocumentId" -> {
                carryOnApplication.navHostController.popBackStack()
                carryOnApplication.navHostController.navigate("${ScreenName.ADD_TRIP_PLAN.name}/$tripDocumentId")
            }
            else -> {
                carryOnApplication.navHostController.popBackStack()
            }
        }
    }

    // 일정 만들기에서 뒤로가기 눌렀을 때
    fun addPlanNavigationOnClick(tripDocumentId:String){
        carryOnApplication.navHostController.popBackStack()
        carryOnApplication.navHostController.navigate(ScreenName.MAIN_SCREEN.name)
    }

    // 지역 선택에서 뒤로가기 눌렀을 때
    fun selectRegionNavigationOnClick(){
        carryOnApplication.navHostController.popBackStack()
        carryOnApplication.navHostController.navigate(ScreenName.MAIN_SCREEN.name)
    }

    // 장소 검색에서 뒤로가기 눌렀을 때
    fun tripSearchNavigationOnClick(tripDocumentId:String){
        carryOnApplication.navHostController.popBackStack()
        carryOnApplication.navHostController.navigate("${ScreenName.ADD_TRIP_PLAN.name}/$tripDocumentId")
    }

    // 다이얼로그에서 여행날짜 수정 눌렀을 때
    fun dialogEditDateOnClick(tripDocumentId:String){
        carryOnApplication.previousScreen.value = "${ScreenName.ADD_TRIP_PLAN.name}/$tripDocumentId"
        carryOnApplication.navHostController.popBackStack()
        carryOnApplication.navHostController.navigate("${ScreenName.SELECT_TRIP_DATE.name}/$tripDocumentId")
    }

    // 장소추가 버튼 눌렀을 때
    fun plusPlaceOnClick(day: String, tripDocumentId:String) {
        // `selectedDay` 업데이트
        selectedDay.value = day

        // selectedDay가 비어있지 않으면 이동
        carryOnApplication.navHostController.popBackStack()
        carryOnApplication.navHostController.navigate("${ScreenName.TRIP_SEARCH_PLACE.name}/${day}/$tripDocumentId")
    }

    // 장소추가 요청 버튼 눌렀을 때
    fun requestPlaceOnClick() {
        carryOnApplication.navHostController.popBackStack()
        carryOnApplication.navHostController.navigate(ScreenName.WRITE_REQUEST_PLACE.name)
    }

    // 장소 편집에서 뒤로가기 눌렀을 때
    fun editPlaceNavigationOnClick(tripDocumentId:String) {
        carryOnApplication.navHostController.popBackStack()
        carryOnApplication.navHostController.navigate("${ScreenName.ADD_TRIP_PLAN.name}/$tripDocumentId")
    }

    // 장소 편집 완료 눌렀을 때
    fun editPlaceDoneOnClick(tripDocumentId:String) {
        carryOnApplication.navHostController.popBackStack()
        carryOnApplication.navHostController.navigate("${ScreenName.ADD_TRIP_PLAN.name}/$tripDocumentId")
    }

    // 날짜별 장소 편집을 눌렀을 때
    fun editPlaceOnClick(day: String, index:Int, tripDocumentId:String){
        selectedDay.value = day
        selectedIndex.value = index
        // 로그 추가
        Log.d("TripInfoViewModel", "editPlaceOnClick - day: $day, index: $index")
        carryOnApplication.navHostController.popBackStack()
        carryOnApplication.navHostController.navigate("${ScreenName.EDIT_PLAN_PLACE.name}/$day/$index/$tripDocumentId")
    }

    // 지도상세보기에서 뒤로가기 눌렀을 때
    fun showMapNavigationOnClick(tripDocumentId:String) {
        carryOnApplication.navHostController.popBackStack()
        carryOnApplication.navHostController.navigate("${ScreenName.ADD_TRIP_PLAN.name}/$tripDocumentId")
    }
}