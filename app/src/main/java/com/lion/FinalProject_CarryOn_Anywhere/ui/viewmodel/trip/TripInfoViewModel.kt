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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class TripInfoViewModel @Inject constructor(
    @ApplicationContext context: Context,
    val tripService: TripService,
    val planService: PlanService
) : ViewModel() {

    val carryOnApplication = context as CarryOnApplication

    val regionCodes = mutableStateListOf<String>()
    val subRegionCodes = mutableStateListOf<String>()

    // ViewModel
    var selectedPlaces = mutableStateListOf<LatLng>() // 선택된 장소 리스트
    var selectedPlaceLocation = mutableStateOf(LatLng(37.5665, 126.9780)) // 기본 위치 (대구)

    // 다이얼로그 상태
    val deletePlanDialogState = mutableStateOf(false)
    val editTripNameDialogState = mutableStateOf(false)
    val deletePlaceDialogState = mutableStateOf(false)

    var deleteTargetPlace = mutableStateOf<Map<String, Any?>?>(null)

    var currentTripName = mutableStateOf("여행1")
    val editTripNameTextFieldValue = mutableStateOf("")

    // 날짜별 리스트 자동 생성
    var tripDays = mutableStateListOf<String>()

    // 바텀시트 상태
    val showBottomSheet = mutableStateOf(false)

    val selectRegion = mutableStateListOf<String>()

    // 날짜 선택
    var startDate = mutableStateOf<Long?>(null)
    var endDate = mutableStateOf<Long?>(null)

    val planDateFormatter = SimpleDateFormat("M.d (E)", Locale.KOREA)
    val dateFormatter = SimpleDateFormat("yyyy. M. d", Locale.getDefault())

    var formattedStartDate = mutableStateOf("")
    var formattedEndDate = mutableStateOf("")

    // 날짜가 변경될 때 자동 업데이트
    fun updateFormattedDates() {
        formattedStartDate.value = startDate.value?.let { dateFormatter.format(Date(it)) } ?: ""
        formattedEndDate.value = endDate.value?.let { dateFormatter.format(Date(it)) } ?: ""
    }

    // 데이터를 가져와 상태 관리 변수에 담아준다.
    fun gettingTripData(tripDocumentId: String) {
        // 서버에서 데이터를 가져온다.
        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO) {
                tripService.selectTripDataOneById(tripDocumentId)
            }
            tripModel = work1.await()

            currentTripName.value = tripModel.tripTitle
            startDate.value = tripModel.tripStartDate
            endDate.value = tripModel.tripEndDate
            selectRegion.clear()

            tripModel.tripCityList.forEach { cityMap ->
                val regionName = cityMap["regionName"] as? String ?: ""
                val regionCode = cityMap["regionCode"] as? String ?: ""
                val subRegionName = cityMap["subRegionName"] as? String ?: ""
                val subRegionCode = cityMap["subRegionCode"] as? String ?: ""

                regionCodes.add(regionCode)
                subRegionCodes.add(subRegionCode)

                // 🔹 "서울시 마포구" 형태로 저장
                val fullRegionInfo = if (regionName == "서울" ||
                    regionName == "부산" || regionName == "대구" || regionName == "인천" ||
                    regionName == "광주" || regionName == "대전" || regionName == "울산"
                ) {
                    "${regionName}시 $subRegionName"
                } else if (regionName == "세종") {
                    "${regionName}특별자치시 $subRegionName"
                } else if (regionName == "강원" || regionName == "경기" || regionName == "충청" ||
                    regionName == "경상" || regionName == "전라"
                ) {
                    "${regionName}도 $subRegionName"
                } else {
                    "${regionName}특별자치도 $subRegionName"
                }
                selectRegion.add(fullRegionInfo)
            }

            // 날짜별로 Firestore에서 PlanData 불러오기
            tripDays.forEach { day ->
                getPlanDataByTripAndDay(tripDocumentId, day)
            }
        }
    }

    fun getPlanDataByTripAndDay(tripDocumentId: String, day: String) {
        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO) {
                planService.getPlanByDocumentIdAndDay(tripDocumentId, day)
            }
            val planData = work1.await()

            if (planData != null) {
                // Firestore에서 불러온 데이터를 placesByDay에 저장
                placesByDay[day] = planData.placeList.map { place ->
                    place
                }.toMutableList()
            }

            selectedPlaces.clear()
            planData?.placeList?.forEach { place ->
                val lat = (place["mapy"] as? String)?.toDoubleOrNull()
                val lng = (place["mapx"] as? String)?.toDoubleOrNull()
                if (lat != null && lng != null) {
                    selectedPlaces.add(LatLng(lat, lng))
                }
            }

            // 마지막 좌표로 `selectedPlaceLocation` 업데이트 (중복 방지)
            val lastLocation = selectedPlaces.lastOrNull()
            if (lastLocation != null && selectedPlaceLocation.value != lastLocation) {
                selectedPlaceLocation.value = lastLocation
            }
        }
    }

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
    var placesByDay = mutableStateMapOf<String, MutableList<Map<String, Any?>>>()
    var selectedDay = mutableStateOf("")

    fun removePlaceFromDay(day: String, place: Map<String, Any?>) {
        placesByDay[day]?.let { places ->
            // Firestore에서 받아온 데이터가 `Map<String, Any?>` 형태이므로 `contentid`를 추출
            val placeIdToRemove = place["contentid"] as? String

            if (placeIdToRemove == null) {
                Log.e("TripInfoViewModel", "삭제할 contentid가 없음: ${place["title"]}")
                return
            }

            // 기존 리스트를 복사하여 변경 (Compose가 감지할 수 있도록)
            val updatedList = places.toMutableList()
            val removed = updatedList.removeIf { it["contentid"] == placeIdToRemove }

            if (removed) {
                // 변경된 리스트를 placesByDay에 다시 할당하여 Compose가 감지할 수 있도록 함
                placesByDay[day] = updatedList.toMutableList()
            }

            // 삭제 후 지도 중심 좌표 업데이트
            selectedPlaceLocation.value = if (selectedPlaces.isNotEmpty()) {
                selectedPlaces.last()
            } else {
                LatLng(35.8714, 128.6014) // 기본값 (대구)
            }
        }
    }

    val selectedIndex = mutableStateOf<Int?>(null)

    fun reorderPlaces(day: String, fromIndex: Int, toIndex: Int) {
        placesByDay[day]?.let { list ->
            if (fromIndex in list.indices && toIndex in list.indices) {
                // 기존 리스트를 복사해서 새로운 리스트 생성
                val newList = list.toMutableList()
                // 기존 위치에서 제거
                val movedItem = newList.removeAt(fromIndex)
                // 새로운 위치에 추가
                newList.add(toIndex, movedItem)

                // 변경된 리스트를 새로 할당하여 Compose가 감지하도록 함
                placesByDay[day] = newList
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

    fun deletePlanOnClick(tripDocumentId: String) {
        CoroutineScope(Dispatchers.Main).launch {
            // 여행 정보를 삭제한다.
            val work1 = async(Dispatchers.IO){
                tripService.deleteTripData(tripDocumentId = tripDocumentId)
                planService.deleteAllPlansByTripId(tripDocumentId = tripDocumentId)
            }
            work1.join()
            // 홈 화면으로 이동한다.
            carryOnApplication.navHostController.popBackStack()
            carryOnApplication.navHostController.navigate(ScreenName.MAIN_SCREEN.name)
        }
    }

    // 서버에서 받아온 데이터를 담을 변수
    lateinit var tripModel: TripModel

    // 지도를 눌렀을 때
    fun mapOnClick(tripDocumentId: String) {
        carryOnApplication.navHostController.popBackStack()
        carryOnApplication.navHostController.navigate("${ScreenName.SHOW_TRIP_MAP.name}/$tripDocumentId")
    }

    // 일정 만들기에서 뒤로가기 눌렀을 때
    fun addPlanNavigationOnClick() {
        placesByDay.clear()
        selectedPlaceLocation.value = LatLng(37.5665, 126.9780)
        carryOnApplication.navHostController.popBackStack()
        carryOnApplication.navHostController.navigate(ScreenName.MAIN_SCREEN.name)
    }

    fun dialogEditDateOnClick(tripDocumentId: String?) {
        carryOnApplication.previousScreen.value = "${ScreenName.ADD_TRIP_PLAN.name}/$tripDocumentId"

        carryOnApplication.navHostController.popBackStack()

        val route = if (tripDocumentId.isNullOrEmpty()) {
            "${ScreenName.SELECT_TRIP_DATE.name}"
        } else {
            "${ScreenName.SELECT_TRIP_DATE.name}?tripDocumentId=$tripDocumentId"
        }

        carryOnApplication.navHostController.navigate(route)
    }

    // 장소추가 버튼 눌렀을 때
    fun plusPlaceOnClick(day: String, tripDocumentId: String) {
        // `selectedDay` 업데이트
        selectedDay.value = day

        // `regionCodes`와 `subRegionCodes`를 문자열로 변환
        val regionCodesParam = regionCodes.joinToString(",")
        val subRegionCodesParam = subRegionCodes.joinToString(",")

        // selectedDay가 비어있지 않으면 이동
        carryOnApplication.navHostController.popBackStack()
        carryOnApplication.navHostController.navigate("${ScreenName.TRIP_SEARCH_PLACE.name}/$day/$tripDocumentId/${regionCodesParam}/${subRegionCodesParam}")
    }

    // 장소 편집에서 뒤로가기 눌렀을 때
    fun editPlaceNavigationOnClick(tripDocumentId: String) {
        carryOnApplication.navHostController.popBackStack()
        carryOnApplication.navHostController.navigate("${ScreenName.ADD_TRIP_PLAN.name}/$tripDocumentId")
    }

    // 장소 편집 완료 눌렀을 때
    fun editPlaceDoneOnClick(tripDocumentId: String) {
        // 현재 선택된 날짜의 장소 리스트 가져오기
        val day = selectedDay.value
        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO){
                placesByDay[day]?.let { updatedPlaceList ->
                    planService.updatePlanByDocumentIdAndDay(
                        tripDocumentId = tripDocumentId,
                        day = day,
                        newPlaceList = updatedPlaceList
                    )
                }
            }
            work1.await()
        }

        // 화면 이동
        carryOnApplication.navHostController.popBackStack()
        carryOnApplication.navHostController.navigate("${ScreenName.ADD_TRIP_PLAN.name}/$tripDocumentId")
    }

    // 날짜별 장소 편집을 눌렀을 때
    fun editPlaceOnClick(day: String, index: Int, tripDocumentId: String) {
        selectedDay.value = day
        selectedIndex.value = index

        carryOnApplication.navHostController.popBackStack()
        carryOnApplication.navHostController.navigate("${ScreenName.EDIT_PLAN_PLACE.name}/$day/$index/$tripDocumentId")
    }

    // 지도상세보기에서 뒤로가기 눌렀을 때
    fun showMapNavigationOnClick(tripDocumentId: String) {
        carryOnApplication.navHostController.popBackStack()
        carryOnApplication.navHostController.navigate("${ScreenName.ADD_TRIP_PLAN.name}/$tripDocumentId")
    }
}