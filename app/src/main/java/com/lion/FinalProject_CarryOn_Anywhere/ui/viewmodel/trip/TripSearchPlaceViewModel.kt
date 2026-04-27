package com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.trip

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.google.gson.JsonSyntaxException
import com.lion.FinalProject_CarryOn_Anywhere.CarryOnApplication
import com.lion.FinalProject_CarryOn_Anywhere.component.ChipState
import com.lion.FinalProject_CarryOn_Anywhere.data.api.TourAPI.TourAPIRetrofitClient
import com.lion.FinalProject_CarryOn_Anywhere.data.api.TourAPI.TourApiModel
import com.lion.FinalProject_CarryOn_Anywhere.data.server.model.PlanModel
import com.lion.FinalProject_CarryOn_Anywhere.data.server.model.RequestModel
import com.lion.FinalProject_CarryOn_Anywhere.data.server.model.TripModel
import com.lion.FinalProject_CarryOn_Anywhere.data.server.service.PlanService
import com.lion.FinalProject_CarryOn_Anywhere.data.server.service.RequestService
import com.lion.FinalProject_CarryOn_Anywhere.data.server.service.TripService
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.ScreenName
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TripSearchPlaceViewModel @Inject constructor(
    @ApplicationContext context: Context,
    val planService: PlanService,
    val tripService: TripService,
    val requestService: RequestService
):ViewModel() {

    val carryOnApplication = context as CarryOnApplication

    private val _allPlaces = MutableStateFlow<List<TourApiModel.TouristSpotItem>>(emptyList())

    // 필터링된 장소 리스트
    private val _filteredPlaces = MutableStateFlow<List<TourApiModel.TouristSpotItem>>(emptyList())
    val filteredPlaces: StateFlow<List<TourApiModel.TouristSpotItem>> = _filteredPlaces

    val tripModel = TripModel()

    // 검색 키워드
    val searchTextFieldValue = mutableStateOf("")

    var textFieldPlaceName = mutableStateOf("")

    val textFieldAddress = mutableStateOf("")

    val showAddressSearch = mutableStateOf(false)

    val requestPlaceDialogState = mutableStateOf(false)

    val dayVal = mutableStateOf("")
    val regionCodesParam = mutableStateOf("")
    val subRegionCodesParam = mutableStateOf("")
    val tripDocumentIdVal = mutableStateOf("")

    var currentPage = 1
    var hasMorePages = true
    var isFetching = false

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        filteredPlaces
    }

    fun toPlaceMap(place: TourApiModel.TouristSpotItem): Map<String, Any?> {
        return mapOf(
            "title" to place.title,
            "addr1" to place.addr1,
            "addr2" to place.addr2,
            "areacode" to place.areacode,
            "sigungucode" to place.sigungucode,
            "contentid" to place.contentid,
            "contenttypeid" to place.contenttypeid,
            "firstimage" to place.firstimage,
            "firstimage2" to place.firstimage2,
            "mapx" to place.mapx,
            "mapy" to place.mapy,
            "tel" to place.tel,
            "zipcode" to place.zipcode
        )
    }

    private suspend fun fetchPlacesFromAPI(
        regionCodes: List<String>,
        subRegionCodes: List<String>,
        page: Int,
        contentTypeId: String? = null
    ) {
        val apiKey = carryOnApplication.tourApiKey
        val uniqueRegionPairs = regionCodes.zip(subRegionCodes).distinct()
        val newPlaces = mutableListOf<TourApiModel.TouristSpotItem>()

        Log.d("TripSearchPlaceViewModel", "검색어에 따른 API 호출 시작 - page: $page, 요청 contentTypeId: $contentTypeId")

        delay(300) // 너무 빠른 요청 방지

        uniqueRegionPairs.forEach { (regionCode, subRegionCode) ->
            Log.d("TripSearchPlaceViewModel", "API 호출: areaCode = $regionCode, sigunguCode = $subRegionCode, pageNo = $page, contentTypeId = $contentTypeId")

            var retryCount = 0
            val maxRetries = 3 // 최대 재시도 횟수
            var success = false

            while (retryCount < maxRetries && !success) {
                try {
                    val response = TourAPIRetrofitClient.instance.getPlaces(
                        serviceKey = apiKey,
                        pageNo = page,
                        areaCode = regionCode,
                        sigunguCode = subRegionCode,
                        contentTypeId = contentTypeId
                    )

                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        val placeList = responseBody?.response?.body?.items?.item ?: emptyList()
                        newPlaces.addAll(placeList)
                        success = true
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.e("TripSearchPlaceViewModel", "API 응답 실패: HTTP ${response.code()}, 오류 내용: $errorBody")

                        // 429 (Too Many Requests)일 경우 일정 시간 대기 후 재시도
                        if (response.code() == 429) {
                            Log.w("TripSearchPlaceViewModel", "요청 제한됨. 3초 대기 후 재시도 (${retryCount + 1}/$maxRetries)")
                            delay(3000)
                        } else {
                            break // 다른 오류는 재시도하지 않음
                        }
                    }
                } catch (e: JsonSyntaxException) {
                    Log.e("TripSearchPlaceViewModel", "JSON 파싱 오류 발생: areaCode=$regionCode, sigunguCode=$subRegionCode, pageNo=$page, contentTypeId=$contentTypeId", e)
                    break // JSON 파싱 오류는 재시도하지 않음
                } catch (e: Exception) {
                    Log.e("TripSearchPlaceViewModel", "API 요청 중 오류 발생: areaCode=$regionCode, sigunguCode=$subRegionCode, contentTypeId=$contentTypeId", e)

                    // 네트워크 문제일 경우 재시도
                    if (retryCount < maxRetries - 1) {
                        Log.w("TripSearchPlaceViewModel", "네트워크 오류 발생. 3초 대기 후 재시도 (${retryCount + 1}/$maxRetries)")
                        delay(3000)
                    }
                }

                retryCount++
            }
        }

        // 데이터 업데이트
        _allPlaces.value = _allPlaces.value + newPlaces
        filterPlaces()
    }

    // 검색어를 함께 전달하도록 fetchPlaces 수정
    fun fetchPlaces(regionCodes: List<String>, subRegionCodes: List<String>) {
        if (isFetching) return
        isFetching = true
        _isLoading.value = true
        currentPage = 1
        hasMorePages = true
        _allPlaces.value = emptyList()

        val query = searchTextFieldValue.value

        // 검색어에 따른 contentTypeId 매칭
        val contentTypeId = when {
            query.contains("관광지") -> "12"
            query.contains("문화시설") -> "14"
            query.contains("축제") || query.contains("공연") || query.contains("행사") -> "15"
            query.contains("여행코스") -> "25"
            query.contains("레포츠") -> "28"
            query.contains("숙박") || query.contains("숙소") -> "32"
            query.contains("쇼핑") -> "38"
            query.contains("맛집") || query.contains("음식점") -> "39"
            else -> null
        }

        viewModelScope.launch(Dispatchers.IO) {
            fetchPlacesFromAPI(regionCodes, subRegionCodes, currentPage, contentTypeId)
            _isLoading.value = false
            isFetching = false
        }
    }

    // fetchNextPlaces도 검색어 전달 추가
    fun fetchNextPlaces(regionCodes: List<String>, subRegionCodes: List<String>) {
        if (!hasMorePages || isFetching) return
        isFetching = true
        _isLoading.value = true
        currentPage++

        val query = searchTextFieldValue.value

        // 검색어에 따른 contentTypeId 매칭
        val contentTypeId = when {
            query.contains("관광지") -> "12"
            query.contains("문화시설") -> "14"
            query.contains("축제") || query.contains("공연") || query.contains("행사") -> "15"
            query.contains("여행코스") -> "25"
            query.contains("레포츠") -> "28"
            query.contains("숙박") || query.contains("숙소") -> "32"
            query.contains("쇼핑") -> "38"
            query.contains("맛집") || query.contains("음식점") -> "39"
            else -> null
        }

        viewModelScope.launch(Dispatchers.IO) {
            fetchPlacesFromAPI(regionCodes, subRegionCodes, currentPage, contentTypeId)
            _isLoading.value = false
            isFetching = false
        }
    }

    fun filterPlaces() {
        val query = searchTextFieldValue.value.lowercase().trim()
        // 검색어를 공백 기준으로 분리하여 키워드 리스트 생성
        val queryKeywords = query.split(" ").filter { it.isNotBlank() }.toMutableList()

        // 검색어에 따른 contentTypeId 매칭 (매칭된 키워드는 키워드 리스트에서 제거)
        val contentTypeId = when {
            query.contains("관광지") -> {
                queryKeywords.remove("관광지")
                "12"
            }
            query.contains("문화시설") -> {
                queryKeywords.remove("문화시설")
                "14"
            }
            query.contains("축제") || query.contains("공연") || query.contains("행사") -> {
                listOf("축제", "공연", "행사").forEach { queryKeywords.remove(it) }
                "15"
            }
            query.contains("여행코스") -> {
                queryKeywords.remove("여행코스")
                "25"
            }
            query.contains("레포츠") -> {
                queryKeywords.remove("레포츠")
                "28"
            }
            query.contains("숙박") || query.contains("숙소") -> {
                listOf("숙박", "숙소").forEach { queryKeywords.remove(it) }
                "32"
            }
            query.contains("쇼핑") -> {
                queryKeywords.remove("쇼핑")
                "38"
            }
            query.contains("맛집") || query.contains("음식점") -> {
                listOf("맛집", "음식점").forEach { queryKeywords.remove(it) }
                "39"
            }
            else -> null
        }

        // 필터링: contentTypeId 조건과 남은 키워드 모두 매칭되어야 함
        val filteredList = _allPlaces.value.filter { place ->
            val matchesContentType = contentTypeId?.let { place.contenttypeid == it } ?: true

            // 남은 키워드가 없으면 기본적으로 true
            val matchesKeywords = if (queryKeywords.isEmpty()) {
                true
            } else {
                queryKeywords.all { keyword ->
                    listOfNotNull(
                        place.title?.lowercase(),
                        place.addr1?.lowercase(),
                        place.addr2?.lowercase()
                    ).any { it.contains(keyword) }
                }
            }
            matchesContentType && matchesKeywords
        }

        _filteredPlaces.value = filteredList

        // 검색 결과가 부족하면 추가 데이터 요청
        if (filteredList.isEmpty() || (filteredList.size <= currentPage * 10 && hasMorePages)) {
            fetchNextPlaces(regionCodesParam.value.split(","), subRegionCodesParam.value.split(","))
        }
    }

    fun addPlaceToDay(day: String, place: Map<String, Any?>, documentId: String) {
        if (day.isEmpty()) {
            Log.e("TripSearchPlaceViewModel", "오류: 추가할 날짜가 없음!")
            return
        }

        CoroutineScope(Dispatchers.Main).launch {
            try {
                // 기존 데이터 조회
                val work1 = async(Dispatchers.IO) {
                    planService.getPlanByDocumentIdAndDay(documentId, day)
                }
                val existingPlan = work1.await()

                // Firestore에서 불러온 기존 리스트 가져오기
                val placesList = existingPlan?.placeList?.toMutableList() ?: mutableListOf()

                // 리스트에서 마지막 추가된 장소 가져오기
                val lastPlace = placesList.lastOrNull()

                // 이전 인덱스와 비교하여 동일하면 추가 안함
                if (lastPlace != null && lastPlace["contentid"] == place["contentid"]) {
                    Toast.makeText(carryOnApplication, "연속으로 같은 장소를 추가할 수 없습니다.", Toast.LENGTH_SHORT).show()

                    searchTextFieldValue.value = ""
                    carryOnApplication.navHostController.popBackStack()
                    carryOnApplication.navHostController.navigate("${ScreenName.ADD_TRIP_PLAN.name}/$documentId")
                    return@launch
                }

                // 기존 리스트에 새로운 장소 추가
                placesList.add(place)

                // PlanModel 생성
                val planModel = PlanModel().apply {
                    planDay = day
                    placeList = placesList
                    tripDocumentId = documentId
                    planTimeStamp = System.currentTimeMillis()
                }

                if (existingPlan != null) {
                    val work2 = async(Dispatchers.IO) {
                        planService.updatePlanData(planModel)
                    }

                    work2.await()

                    searchTextFieldValue.value = ""
                    carryOnApplication.navHostController.popBackStack()
                    carryOnApplication.navHostController.navigate("${ScreenName.ADD_TRIP_PLAN.name}/$documentId")
                } else {
                    val work3 = async(Dispatchers.IO) {
                        planService.addPlanData(planModel)
                    }
                    val planDocumentId = work3.await()

                    if (!planDocumentId.isNullOrEmpty()) {
                        val work4 = async(Dispatchers.IO) {
                            tripService.updateTripPlanList(tripModel.apply { planList.add(planDocumentId) }, documentId)
                        }

                        work4.await()
                    }

                    searchTextFieldValue.value = ""
                    carryOnApplication.navHostController.popBackStack()
                    carryOnApplication.navHostController.navigate("${ScreenName.ADD_TRIP_PLAN.name}/$documentId")
                }
            } catch (e: Exception) {
                Toast.makeText(carryOnApplication, "오류 발생: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 장소추가 요청 버튼 눌렀을 때
    fun requestPlaceOnClick() {
        textFieldPlaceName.value = searchTextFieldValue.value

        carryOnApplication.navHostController.popBackStack()
        carryOnApplication.navHostController.navigate(ScreenName.WRITE_REQUEST_PLACE.name)
    }

    fun addPlaceRequst(){
        val requestModel = RequestModel()

        requestModel.userDocumentId = carryOnApplication.loginUserModel.userDocumentId
        requestModel.requestPlaceName = textFieldPlaceName.value
        requestModel.requestPlaceAddress = textFieldAddress.value
        requestModel.requestTimeStamp = System.currentTimeMillis()

        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO) {
                requestService.addRequestData(requestModel)
            }
            work1.await()

            carryOnApplication.navHostController.popBackStack()
            carryOnApplication.navHostController.navigate("${ScreenName.TRIP_SEARCH_PLACE.name}/${dayVal.value}/${tripDocumentIdVal.value}/${regionCodesParam.value}/${subRegionCodesParam.value}")
        }
    }

    // 장소 검색에서 뒤로가기 눌렀을 때
    fun tripSearchNavigationOnClick(tripDocumentId:String){
        searchTextFieldValue.value = ""
        carryOnApplication.navHostController.popBackStack()
        carryOnApplication.navHostController.navigate("${ScreenName.ADD_TRIP_PLAN.name}/$tripDocumentId")
    }

    // 장소 등록 요청 에서 뒤로가기 눌렀을 때
    fun requestPlaceNavigationOnClick(){
        when(carryOnApplication.previousScreen.value) {
            "${ScreenName.TRIP_SEARCH_PLACE.name}/${dayVal.value}/${tripDocumentIdVal.value}/${regionCodesParam.value}/${subRegionCodesParam.value}" -> {
                carryOnApplication.navHostController.popBackStack()
                carryOnApplication.navHostController.navigate("${ScreenName.TRIP_SEARCH_PLACE.name}/${dayVal.value}/${tripDocumentIdVal.value}/${regionCodesParam.value}/${subRegionCodesParam.value}")
            }

            ScreenName.PLACE_SEARCH_SCREEN.name -> {
                carryOnApplication.navHostController.popBackStack()
                carryOnApplication.navHostController.navigate(ScreenName.PLACE_SEARCH_SCREEN.name)
            }

            else -> {
                carryOnApplication.navHostController.popBackStack()
                carryOnApplication.navHostController.navigate("${ScreenName.TRIP_SEARCH_PLACE.name}/${dayVal.value}/${tripDocumentIdVal.value}/${regionCodesParam.value}/${subRegionCodesParam.value}")
            }
        }
    }
}