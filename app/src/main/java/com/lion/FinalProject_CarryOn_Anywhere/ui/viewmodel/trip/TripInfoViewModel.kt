package com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.trip

import android.content.Context
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.lion.FinalProject_CarryOn_Anywhere.CarryOnApplication
import com.lion.FinalProject_CarryOn_Anywhere.component.ChipState
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.ScreenName
import com.lion.FinalProject_CarryOn_Anywhere.ui.screen.trip.Place
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class TripInfoViewModel @Inject constructor(
    @ApplicationContext context: Context
) : ViewModel(){

    val carryOnApplication = context as CarryOnApplication

    // ViewModel
    var selectedPlaces = mutableStateListOf<LatLng>() // ✅ 선택된 장소 리스트
    var selectedPlaceLocation = mutableStateOf(LatLng(35.8714, 128.6014)) // ✅ 기본 위치 (대구)

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
    val regions = listOf("서울", "부산", "경기", "인천", "강원", "경상", "전라", "충청", "제주")

    // 시/도를 클릭하면 해당 시/도의 구/군 목록을 보여줌
    val subRegionsMap = mapOf(
        "서울" to listOf("노원구", "은평구", "강남구", "서초구", "종로구", "마포구"),
        "부산" to listOf("해운대구", "남구", "부산진구", "사하구"),
        "제주" to listOf("제주시", "서귀포시"),
        "경기" to listOf("수원시", "고양시", "성남시", "부천시"),
        "인천" to listOf("연수구", "남동구", "부평구"),
        "강원" to listOf("춘천시", "강릉시", "원주시"),
        "경상" to listOf("대구", "창원", "포항"),
        "전라" to listOf("전주시", "광주시", "여수시"),
        "충청" to listOf("청주시", "천안시", "공주시")
    )

    // 선택된 지역 리스트 (예: "서울시 노원구")
    var selectedRegions: SnapshotStateList<ChipState> = mutableStateListOf()

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
    fun addPlaceToDay(day: String, place: Place) {
        if (day.isEmpty()) {
            println("오류: 추가할 날짜가 없음!")
            return
        }

        val placesList = placesByDay[day]

        // 마지막으로 추가된 장소와 현재 추가할 장소가 동일하면 추가하지 않음
        if (placesList?.isNotEmpty() == true && placesList.last().title == place.title) {

            Toast.makeText(carryOnApplication, "동일한 장소를 연속으로 추가할 수 없습니다.", Toast.LENGTH_SHORT).show()

            carryOnApplication.navHostController.popBackStack(ScreenName.TRIP_SEARCH_PLACE.name, true)
            carryOnApplication.navHostController.navigate(ScreenName.ADD_TRIP_PLAN.name)

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

        carryOnApplication.navHostController.popBackStack(ScreenName.TRIP_SEARCH_PLACE.name, true)
        carryOnApplication.navHostController.navigate(ScreenName.ADD_TRIP_PLAN.name)
    }

    fun removePlaceFromDay(day: String, place: Place) {
        placesByDay[day]?.let { places ->
            // ✅ 장소 삭제
            places.remove(place)

            // ✅ 만약 해당 날짜에 장소가 없다면, 리스트에서 제거
            if (places.isEmpty()) {
                placesByDay.remove(day)
            }

            // ✅ `selectedPlaces`에서도 해당 장소의 좌표 삭제
            selectedPlaces.removeIf { it.latitude == place.latitude && it.longitude == place.longitude }

            // ✅ 남아있는 장소가 있으면 마지막 장소로 지도 중심 이동
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
    }

    // 장소 필터링
    fun filterPlaces() {
        val query = searchTextFieldValue.value.lowercase().trim()
        // 1️⃣ selectedRegions 디버깅 로그 출력
        println("🟢 선택된 지역 목록: ${selectedRegions.joinToString { it.text }}")

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
                    place.location.lowercase().contains(region.lowercase()) // ✅ 포함 여부 체크
                }
            }
        } else {
            allPlaces // 선택된 지역이 없으면 모든 장소 사용
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
                println("🚀 장소 순서 변경: $fromIndex -> $toIndex")
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
        return results[0] // 미터(m) 단위 거리 반환
    }

    // 이전 화면 저장
    var previousScreen = mutableStateOf<String?>(null)

    fun deletePlanOnClick(){

    }

    // 지역 선택 버튼 눌렀을 때
    fun completeRegionOnClick(){
        previousScreen.value = ScreenName.SELECT_TRIP_REGION.name
        carryOnApplication.navHostController.popBackStack(ScreenName.SELECT_TRIP_REGION.name, true)
        carryOnApplication.navHostController.navigate(ScreenName.SELECT_TRIP_DATE.name)
    }

    // 날짜 선택 버튼 눌렀을 때
    fun completeDateOnClick(){
        carryOnApplication.navHostController.popBackStack(ScreenName.SELECT_TRIP_DATE.name, true)
        carryOnApplication.navHostController.navigate(ScreenName.ADD_TRIP_PLAN.name)
    }

    // 지도를 눌렀을 때
    fun mapOnClick(){
        carryOnApplication.navHostController.popBackStack(ScreenName.ADD_TRIP_PLAN.name, true)
        carryOnApplication.navHostController.navigate(ScreenName.SHOW_TRIP_MAP.name)
    }

    fun tripDateNavigationOnClick() {
        when (previousScreen.value) {
            ScreenName.SELECT_TRIP_REGION.name -> {
                carryOnApplication.navHostController.popBackStack(ScreenName.SELECT_TRIP_DATE.name, true)
                carryOnApplication.navHostController.navigate(ScreenName.SELECT_TRIP_REGION.name)
            }
            ScreenName.ADD_TRIP_PLAN.name -> {
                carryOnApplication.navHostController.popBackStack(ScreenName.SELECT_TRIP_DATE.name, true)
                carryOnApplication.navHostController.navigate(ScreenName.ADD_TRIP_PLAN.name)
            }
            else -> {
                carryOnApplication.navHostController.popBackStack(ScreenName.SELECT_TRIP_DATE.name, true)
            }
        }
    }

    // 일정 만들기에서 뒤로가기 눌렀을 때
    fun addPlanNavigationOnClick(){
        carryOnApplication.navHostController.popBackStack(ScreenName.ADD_TRIP_PLAN.name, true)
        carryOnApplication.navHostController.navigate(ScreenName.SELECT_TRIP_REGION.name)
    }

    // 장소 검색에서 뒤로가기 눌렀을 때
    fun tripSearchNavigationOnClick(){
        carryOnApplication.navHostController.popBackStack(ScreenName.TRIP_SEARCH_PLACE.name, true)
        carryOnApplication.navHostController.navigate(ScreenName.ADD_TRIP_PLAN.name)
    }

    // 다이얼로그에서 여행날짜 수정 눌렀을 때
    fun dialogEditDateOnClick(){
        previousScreen.value = ScreenName.ADD_TRIP_PLAN.name
        carryOnApplication.navHostController.popBackStack(ScreenName.ADD_TRIP_PLAN.name, true)
        carryOnApplication.navHostController.navigate(ScreenName.SELECT_TRIP_DATE.name)
    }

    // 장소추가 버튼 눌렀을 때
    fun plusPlaceOnClick(day: String) {
        selectedDay.value = day // `selectedDay` 업데이트

        // selectedDay가 비어있지 않으면 이동
        carryOnApplication.navHostController.popBackStack(ScreenName.ADD_TRIP_PLAN.name, true)
        carryOnApplication.navHostController.navigate("${ScreenName.TRIP_SEARCH_PLACE.name}/${day}")
    }

    // 장소추가 요청 버튼 눌렀을 때
    fun requestPlaceOnClick() {
        carryOnApplication.navHostController.popBackStack(ScreenName.TRIP_SEARCH_PLACE.name, true)
        carryOnApplication.navHostController.navigate(ScreenName.WRITE_REQUEST_PLACE.name)
    }

    // 장소 편집에서 뒤로가기 눌렀을 때
    fun editPlaceNavigationOnClick() {
        carryOnApplication.navHostController.popBackStack(ScreenName.EDIT_PLAN_PLACE.name, true)
        carryOnApplication.navHostController.navigate(ScreenName.ADD_TRIP_PLAN.name)
    }

    // 장소 편집 완료 눌렀을 때
    fun editPlaceDoneOnClick() {
        carryOnApplication.navHostController.popBackStack(ScreenName.EDIT_PLAN_PLACE.name, true)
        carryOnApplication.navHostController.navigate(ScreenName.ADD_TRIP_PLAN.name)
    }

    // 날짜별 장소 편집을 눌렀을 때
    fun editPlaceOnClick(day: String, index:Int){
        selectedDay.value = day
        selectedIndex.value = index
        Log.d("TripInfoViewModel", "editPlaceOnClick - day: $day, index: $index") // ✅ 로그 추가
        carryOnApplication.navHostController.popBackStack(ScreenName.ADD_TRIP_PLAN.name, true)
        carryOnApplication.navHostController.navigate("${ScreenName.EDIT_PLAN_PLACE.name}/$day/$index")
    }

    // 지도상세보기에서 뒤로가기 눌렀을 때
    fun showMapNavigationOnClick(){
        carryOnApplication.navHostController.popBackStack(ScreenName.SHOW_TRIP_MAP.name, true)
        carryOnApplication.navHostController.navigate(ScreenName.ADD_TRIP_PLAN.name)
    }
}