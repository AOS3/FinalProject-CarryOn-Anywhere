package com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.trip

import android.content.Context
import android.content.Intent
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
import com.google.firebase.firestore.FirebaseFirestore
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
import kotlinx.coroutines.delay
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

    var currentTripName = mutableStateOf("")
    val editTripNameTextFieldValue = mutableStateOf("")

    // 날짜별 리스트 자동 생성
    var tripDays = mutableStateListOf<String>()

    // 바텀시트 상태
    val showBottomSheet = mutableStateOf(false)

    val isLoading = mutableStateOf(true)

    val selectRegion = mutableStateListOf<String>()

    val shareCode = mutableStateOf("")
    val tripTitle = mutableStateOf("")

    // 날짜 선택
    var startDate = mutableStateOf<Long?>(null)
    var endDate = mutableStateOf<Long?>(null)

    val planDateFormatter = SimpleDateFormat("M.d (E)", Locale.KOREA)
    val dateFormatter = SimpleDateFormat("yyyy. M. d", Locale.getDefault())

    var formattedStartDate = mutableStateOf("")
    var formattedEndDate = mutableStateOf("")

    // 일별 장소선택
    var placesByDay = mutableStateMapOf<String, MutableList<Map<String, Any?>>>()
    var selectedDay = mutableStateOf("")

    // 서버에서 받아온 데이터를 담을 변수
    lateinit var tripModel: TripModel

    val selectedIndex = mutableStateOf<Int?>(null)

    // 날짜가 변경될 때 자동 업데이트
    fun updateFormattedDates() {
        formattedStartDate.value = startDate.value?.let { dateFormatter.format(Date(it)) } ?: ""
        formattedEndDate.value = endDate.value?.let { dateFormatter.format(Date(it)) } ?: ""
    }

    // 데이터를 가져와 상태 관리 변수에 담아준다.
    fun gettingTripData(tripDocumentId: String) {
        // 서버에서 데이터를 가져온다.
        CoroutineScope(Dispatchers.Main).launch {
            isLoading.value = true
            val work1 = async(Dispatchers.IO) {
                tripService.selectTripDataOneById(tripDocumentId)
            }
            tripModel = work1.await()

            if(tripModel.tripTitle == "여행1") {
                currentTripName.value = "여행1"
            } else {
                currentTripName.value = tripModel.tripTitle
            }
            startDate.value = tripModel.tripStartDate
            endDate.value = tripModel.tripEndDate
            shareCode.value = tripModel.tripShareCode
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

            updateTripDays()

            // 날짜별로 Firestore에서 PlanData 불러오기
            tripDays.forEach { day ->
                getPlanDataByTripAndDay(tripDocumentId, day)
            }

            if (selectedDay.value == "") {
                selectedDay.value = tripDays.first()
            }

            delay(200)
            isLoading.value = false
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

            // 🔹 장소 리스트가 비어 있을 경우 기본값으로 설정
            selectedPlaceLocation.value = if (selectedPlaces.isNotEmpty()) {
                selectedPlaces.last()
            } else {
                LatLng(37.5665, 126.9780) // 기본값 (서울)
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

    fun generateRandomCode(): String {
        val allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        return (1..5)
            .map { allowedChars.random() }
            .joinToString("")
    }

    fun shareOnClick(context: Context) {
        if (shareCode.value != "") {
            shareTripCode(context, shareCode.value)
            Toast.makeText(context, "이미 공유된 계획입니다.", Toast.LENGTH_SHORT).show()
        } else {
            CoroutineScope(Dispatchers.Main).launch {
                shareCode.value = generateRandomCode()
                tripModel.tripShareCode = shareCode.value
                val work3 = async(Dispatchers.IO) {
                    tripService.updateTripShare(tripModel)
                }
                work3.join()

                shareTripCode(context, shareCode.value)

                Toast.makeText(context, "공유가 완료되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun updateTitleOnClick(context: Context, tripDocumentId: String){
        if (currentTripName.value == editTripNameTextFieldValue.value) {
            editTripNameTextFieldValue.value = ""
            Toast.makeText(context, "기존과 똑같은 이름입니다.", Toast.LENGTH_SHORT).show()
        } else {
            CoroutineScope(Dispatchers.Main).launch {
                tripModel.tripTitle = editTripNameTextFieldValue.value
                val work3 = async(Dispatchers.IO) {
                    tripService.updateTripTitle(tripModel)
                }
                work3.join()

                editTripNameDialogState.value = false
                editTripNameTextFieldValue.value = ""
                gettingTripData(tripDocumentId)
                Toast.makeText(context, "수정이 완료되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun shareTripCode(context: Context, shareCode: String) {
        val shareText = """
        🚀 [CarryOn 여행 일정 공유] 🚀
        여행 코드: $shareCode
        
        CarryOn 앱에서 "일정 코드 입력" 기능을 사용하여 여행을 확인하세요!
    """.trimIndent()

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)
        }
        context.startActivity(Intent.createChooser(intent, "여행 코드 공유하기"))
    }

    // 일정 만들기에서 뒤로가기 눌렀을 때
    fun addPlanNavigationOnClick() {
        isLoading.value = false
        when (carryOnApplication.previousScreen.value) {
            ScreenName.MY_TRIP_PLAN.name -> {
                carryOnApplication.navHostController.popBackStack()
                carryOnApplication.navHostController.navigate(ScreenName.MY_TRIP_PLAN.name)
            }
            else -> {
                carryOnApplication.navHostController.popBackStack()
                carryOnApplication.navHostController.navigate(ScreenName.MAIN_SCREEN.name)
            }
        }
        placesByDay.clear()
        selectedPlaceLocation.value = LatLng(37.5665, 126.9780)
        regionCodes.clear()
        subRegionCodes.clear()
        shareCode.value = ""
        selectedDay.value = ""
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

        placesByDay.clear()
        selectedPlaceLocation.value = LatLng(37.5665, 126.9780)
        regionCodes.clear()
        subRegionCodes.clear()
        shareCode.value = ""
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

    // 지도를 눌렀을 때
    fun mapOnClick(tripDocumentId: String) {
        carryOnApplication.navHostController.popBackStack()
        carryOnApplication.navHostController.navigate("${ScreenName.SHOW_TRIP_MAP.name}/$tripDocumentId")
    }
}