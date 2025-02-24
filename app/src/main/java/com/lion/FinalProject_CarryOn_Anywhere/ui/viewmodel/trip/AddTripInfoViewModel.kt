package com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.trip

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lion.FinalProject_CarryOn_Anywhere.CarryOnApplication
import com.lion.FinalProject_CarryOn_Anywhere.component.ChipState
import com.lion.FinalProject_CarryOn_Anywhere.data.api.TourAPI.TourAPIRetrofitClient
import com.lion.FinalProject_CarryOn_Anywhere.data.api.TourAPI.TourApiModel
import com.lion.FinalProject_CarryOn_Anywhere.data.server.model.TripModel
import com.lion.FinalProject_CarryOn_Anywhere.data.server.service.TripService
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.ScreenName
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class AddTripInfoViewModel @Inject constructor(
    @ApplicationContext context: Context,
    val tripService: TripService
) : ViewModel(){

    val carryOnApplication = context as CarryOnApplication

    // 선택된 지역 리스트
    var selectedRegions = mutableStateListOf<ChipState>()

    // 시/도 목록
    private val _regions = mutableStateOf<List<String>>(emptyList())
    val regions: State<List<String>> = _regions

    // 구/군 목록
    private val _subRegionsMap = mutableStateOf<Map<String, List<String>>>(emptyMap())
    val subRegionsMap: State<Map<String, List<String>>> = _subRegionsMap

    private var regionCodeMap: MutableMap<String, String> = mutableMapOf()

    // 구/군 이름 + 시군구 코드 저장 (Firebase 저장 용도)
    private val _subRegionsCodeMap = mutableStateOf<Map<String, List<Map<String, String>>>>(emptyMap())
    val subRegionsCodeMap: State<Map<String, List<Map<String, String>>>> = _subRegionsCodeMap

    // 서버에서 받아온 데이터를 담을 변수
    lateinit var tripModel: TripModel

    // 날짜 선택
    var startDate = mutableStateOf<Long?>(null)
    var endDate = mutableStateOf<Long?>(null)

    // 날짜 선택
    var serverStartDate = mutableStateOf<Long?>(null)
    var serverEndDate = mutableStateOf<Long?>(null)

    val dateFormatter = SimpleDateFormat("yyyy. M. d", Locale.getDefault())

    var formattedStartDate = mutableStateOf("")
    var formattedEndDate = mutableStateOf("")

    var formattedServerStartDate = mutableStateOf("")
    var formattedServerEndDate = mutableStateOf("")

    val isButtonRegionEnabled = mutableStateOf(false)

    init {
        updateRegionButtonState()
        fetchRegions()
        fetchSubRegions("서울")
    }

    fun updateRegionButtonState() {
        isButtonRegionEnabled.value = selectedRegions.isNotEmpty()
    }

    fun fetchRegions() {
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
                    // 여러 지역 코드 동시 요청
                }.awaitAll()

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
        val parsedRegion = regionName.split(" ")[0]
            .replace("서울시", "서울")
            .replace("부산시", "부산")
            .replace("대구시", "대구")
            .replace("인천시", "인천")
            .replace("광주시", "광주")
            .replace("대전시", "대전")
            .replace("울산시", "울산")
            .replace("세종시", "세종")
            .replace("특별자치도", "제주")
            .replace("도$", "")

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

    // 날짜가 변경될 때 자동 업데이트
    fun updateFormattedDates() {
        formattedStartDate.value = startDate.value?.let { dateFormatter.format(Date(it)) } ?: ""
        formattedEndDate.value = endDate.value?.let { dateFormatter.format(Date(it)) } ?: ""
        formattedServerStartDate.value = serverStartDate.value?.let { dateFormatter.format(Date(it)) } ?: ""
        formattedServerEndDate.value = serverEndDate.value?.let { dateFormatter.format(Date(it)) } ?: ""
    }

    // 날짜 선택 버튼 눌렀을 때
    fun completeDateOnClick(){
        val tripModel = TripModel()
        tripModel.tripTitle = "여행1"

        // 선택된 지역 정보 리스트 변환 (시/도 & 선택된 구/군만 저장)
        tripModel.tripCityList = selectedRegions.mapNotNull { region ->
            val regionName = region.text
            val parsedRegion = parseRegionName(regionName)
            val regionCodeList = getRegionCode(parsedRegion).split(",")

            // 사용자가 선택한 "구/군"만 필터링
            val selectedSubRegionName = extractSubRegion(regionName)
            val matchedSubRegion = subRegionsCodeMap.value[parsedRegion]
                ?.find { it["subRegionName"] == selectedSubRegionName }

            matchedSubRegion?.let { subRegion ->
                // subRegionCode`가 포함된 `regionCodeList`에서 해당되는 코드 찾기
                val correctRegionCode = regionCodeList.find { it == subRegion["regionCode"] } ?: regionCodeList.first()

                mapOf(
                    "regionName" to parsedRegion,
                    "regionCode" to correctRegionCode,
                    "subRegionName" to subRegion["subRegionName"],
                    "subRegionCode" to subRegion["subRegionCode"]
                )
            }
        }.toMutableList()

        if (endDate.value == null) {
            tripModel.tripStartDate = startDate.value!!
            tripModel.tripEndDate = startDate.value!!
        } else {
            tripModel.tripStartDate = startDate.value!!
            tripModel.tripEndDate = endDate.value!!
        }

        tripModel.tripTimeStamp = System.currentTimeMillis()
        tripModel.userDocumentId = carryOnApplication.loginUserModel.userDocumentId

        // Firebase 저장 실행
        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO) {
                tripService.addTripData(tripModel)
            }

            val documentId = work1.await()

            if (documentId != null) {
                // `ADD_TRIP_PLAN`으로 `documentId` 전달하며 이동

                selectedRegions.clear()
                updateRegionButtonState()

                carryOnApplication.navHostController.popBackStack()
                carryOnApplication.navHostController.navigate("${ScreenName.ADD_TRIP_PLAN.name}/$documentId")
            }
        }
    }

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

    fun gettingTripData(tripDocumentId: String) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val work1 = async(Dispatchers.IO) {
                    tripService.selectTripDataOneById(tripDocumentId)
                }
                val tripData = work1.await()

                if (tripData != null) {
                    tripModel = tripData
                    serverStartDate.value = tripModel.tripStartDate
                    serverEndDate.value = tripModel.tripEndDate
                    Log.d("TripInfoViewModel", "서버 데이터 로드 완료: ${serverStartDate.value} ~ ${serverEndDate.value}")
                } else {
                    Log.e("TripInfoViewModel", "Firestore에서 데이터를 찾을 수 없음: $tripDocumentId")
                }
            } catch (e: Exception) {
                Log.e("TripInfoViewModel", "Firestore 데이터 가져오기 실패: ${e.message}")
            }
        }
    }

    // 지역 선택에서 뒤로가기 눌렀을 때
    fun selectRegionNavigationOnClick(){
        selectedRegions.clear()
        updateRegionButtonState()

        when (carryOnApplication.previousScreen.value) {
            ScreenName.MAIN_SCREEN.name -> {
                carryOnApplication.navHostController.popBackStack()
                carryOnApplication.navHostController.navigate(ScreenName.MAIN_SCREEN.name)
            }
            ScreenName.MY_TRIP_PLAN.name -> {
                carryOnApplication.navHostController.popBackStack()
                carryOnApplication.navHostController.navigate(ScreenName.MY_TRIP_PLAN.name)
            }
            else -> {
                carryOnApplication.navHostController.popBackStack()
            }
        }
    }

    fun tripDateNavigationOnClick(tripDocumentId: String) {
        Log.d("TripInfoViewModel", "previousScreen: ${carryOnApplication.previousScreen.value}")
        Log.d("TripInfoViewModel", "Checking: ${ScreenName.ADD_TRIP_PLAN.name}/$tripDocumentId")

        when (carryOnApplication.previousScreen.value) {
            ScreenName.SELECT_TRIP_REGION.name -> {
                carryOnApplication.navHostController.popBackStack()
                carryOnApplication.navHostController.navigate(ScreenName.SELECT_TRIP_REGION.name)
            }
            "${ScreenName.ADD_TRIP_PLAN.name}/$tripDocumentId" -> {
                Log.d("TripInfoViewModel", "Navigating to ADD_TRIP_PLAN")
                carryOnApplication.navHostController.popBackStack()
                carryOnApplication.navHostController.navigate("${ScreenName.ADD_TRIP_PLAN.name}/$tripDocumentId")
            }
            else -> {
                carryOnApplication.navHostController.popBackStack()
            }
        }
    }

    fun completeRegionOnClick() {
        carryOnApplication.previousScreen.value = ScreenName.SELECT_TRIP_REGION.name
        carryOnApplication.navHostController.popBackStack()

        // `tripDocumentId` 없이 이동 가능하도록 변경
        carryOnApplication.navHostController.navigate("${ScreenName.SELECT_TRIP_DATE.name}")
    }
}