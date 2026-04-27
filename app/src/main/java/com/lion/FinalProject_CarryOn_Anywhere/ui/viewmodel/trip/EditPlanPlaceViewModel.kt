package com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.trip

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.lion.FinalProject_CarryOn_Anywhere.CarryOnApplication
import com.lion.FinalProject_CarryOn_Anywhere.data.server.service.PlanService
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.ScreenName
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditPlanPlaceViewModel @Inject constructor(
    @ApplicationContext context: Context,
    val planService: PlanService
) : ViewModel() {
    val carryOnApplication = context as CarryOnApplication

    // 일별 장소선택
    var placesByDay = mutableStateMapOf<String, MutableList<Map<String, Any?>>>()

    val deletePlaceDialogState = mutableStateOf(false)

    var deleteTargetPlace = mutableStateOf<Map<String, Any?>?>(null)

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
        }
    }

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
        }
    }

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

    // 장소 편집에서 뒤로가기 눌렀을 때
    fun editPlaceNavigationOnClick(tripDocumentId: String) {
        carryOnApplication.navHostController.popBackStack()
        carryOnApplication.navHostController.navigate("${ScreenName.ADD_TRIP_PLAN.name}/$tripDocumentId")
    }

    // 장소 편집 완료 눌렀을 때
    fun editPlaceDoneOnClick(tripDocumentId: String, day: String) {
        // 현재 선택된 날짜의 장소 리스트 가져오기
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
}