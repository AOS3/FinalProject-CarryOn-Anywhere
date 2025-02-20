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
import com.lion.FinalProject_CarryOn_Anywhere.CarryOnApplication
import com.lion.FinalProject_CarryOn_Anywhere.component.ChipState
import com.lion.FinalProject_CarryOn_Anywhere.data.api.TourAPI.TourAPIRetrofitClient
import com.lion.FinalProject_CarryOn_Anywhere.data.api.TourAPI.TourApiModel
import com.lion.FinalProject_CarryOn_Anywhere.data.server.model.PlanModel
import com.lion.FinalProject_CarryOn_Anywhere.data.server.model.TripModel
import com.lion.FinalProject_CarryOn_Anywhere.data.server.service.PlanService
import com.lion.FinalProject_CarryOn_Anywhere.data.server.service.TripService
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.ScreenName
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TripSearchPlaceViewModel @Inject constructor(
    @ApplicationContext context: Context,
    val planService: PlanService,
    val tripService: TripService
):ViewModel() {

    val carryOnApplication = context as CarryOnApplication

    // 필터링된 장소 리스트
    var filteredPlaces = SnapshotStateList<TourApiModel.TouristSpotItem>()

    val tripModel = TripModel()

    // 검색 키워드
    val searchTextFieldValue = mutableStateOf("")

    // 지역별 관광지 데이터를 저장할 MutableState
    val placesByRegion = mutableStateOf(
        mutableMapOf<String, MutableList<Map<String, TourApiModel.TouristSpotItem>>>()
    )

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

    fun fetchPlaces(regionCodes: List<String>, subRegionCodes: List<String>) {
        viewModelScope.launch {
            try {
                val apiKey = "6d5mkmqFyluWJNMUzIer6qA43/S6w+LWlCCspcQwyeSs9fesUnARurM+nBCqBxQ982Sl0OoHXILuM8nFrjKsjQ=="

                // 중복 제거한 지역-시군구 쌍 리스트 생성
                val uniqueRegionPairs = regionCodes.zip(subRegionCodes).distinct()

                // 기존 데이터 초기화
                placesByRegion.value.clear()

                // 중복 요청 방지를 위해 `forEach` 사용
                val allPlaces = mutableListOf<TourApiModel.TouristSpotItem>()

                uniqueRegionPairs.forEach { (regionCode, subRegionCode) ->
                    try {
                        val response = TourAPIRetrofitClient.instance.getPlaces(
                            serviceKey = apiKey,
                            areaCode = regionCode,
                            sigunguCode = subRegionCode,
                        )
                        val placeList = response.body()?.response?.body?.items?.item ?: emptyList()

                        allPlaces.addAll(placeList)

                    } catch (e: Exception) {
                        Log.e("TripInfoViewModel", "API 요청 실패: areaCode=$regionCode, sigunguCode=$subRegionCode", e)
                    }
                }

                if (allPlaces.isEmpty()) {
                    return@launch
                }

                // 기존 데이터 초기화 후 새로운 데이터 저장
                placesByRegion.value = mutableMapOf()
                uniqueRegionPairs.forEach { (regionCode, _) ->
                    placesByRegion.value[regionCode] = allPlaces.map { mapOf(it.contentid!! to it) }.toMutableList()
                }

                // 초기 검색 필터링 수행하여 모든 장소 표시
                filterPlaces()

            } catch (e: Exception) {
                Log.e("TripInfoViewModel", "장소 데이터를 가져오는 중 오류 발생", e)
            }
        }
    }

    fun filterPlaces() {
        val query = searchTextFieldValue.value.lowercase().trim()

        // 저장된 `placesByRegion`에서 모든 관광지 데이터를 가져옴
        val allPlaces = placesByRegion.value.values
            // `TouristSpotItem` 리스트로 변환
            .flatMap { list -> list.flatMap { map -> map.values } }

        filteredPlaces.clear()

        // 검색 키워드가 비어있으면 전체 데이터를 보여줌
        if (query.isEmpty()) {
            filteredPlaces.addAll(allPlaces)
        } else {
            filteredPlaces.addAll(
                allPlaces.filter { place ->
                    listOfNotNull(
                        place.title?.lowercase(),
                        place.addr1?.lowercase(),
                        place.addr2?.lowercase()
                    ).any { it.contains(query) }
                }
            )
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
        carryOnApplication.navHostController.popBackStack()
        carryOnApplication.navHostController.navigate(ScreenName.WRITE_REQUEST_PLACE.name)
    }

    // 장소 검색에서 뒤로가기 눌렀을 때
    fun tripSearchNavigationOnClick(tripDocumentId:String){
        carryOnApplication.navHostController.popBackStack()
        carryOnApplication.navHostController.navigate("${ScreenName.ADD_TRIP_PLAN.name}/$tripDocumentId")
    }
}