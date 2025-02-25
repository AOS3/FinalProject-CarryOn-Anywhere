package com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.mypage

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.lion.FinalProject_CarryOn_Anywhere.CarryOnApplication
import com.lion.FinalProject_CarryOn_Anywhere.data.server.model.TripModel
import com.lion.FinalProject_CarryOn_Anywhere.data.server.service.PlanService
import com.lion.FinalProject_CarryOn_Anywhere.data.server.service.TripService
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.ScreenName
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyTripPlanViewModel @Inject constructor(
    @ApplicationContext context: Context,
    val tripService: TripService,
    val planService: PlanService
): ViewModel(){
    val carryOnApplication = context as CarryOnApplication

    val contentListState = mutableStateListOf<TripModel>()

    private val _isLoading = mutableStateOf(false)  // 로딩 상태 관리
    val isLoading: State<Boolean> = _isLoading

    fun listItemOnClick(documentId:String){
        carryOnApplication.previousScreen.value = ScreenName.MY_TRIP_PLAN.name

        carryOnApplication.navHostController.popBackStack()
        carryOnApplication.navHostController.navigate("${ScreenName.ADD_TRIP_PLAN.name}/$documentId")
    }

    fun addPlanOnClick(){
        carryOnApplication.previousScreen.value = ScreenName.MY_TRIP_PLAN.name
        carryOnApplication.navHostController.popBackStack()
        carryOnApplication.navHostController.navigate(ScreenName.SELECT_TRIP_REGION.name)
    }

    fun gettingTripData(){
        CoroutineScope(Dispatchers.Main).launch {
            _isLoading.value = true
            val work1 = async(Dispatchers.IO){
                tripService.gettingTripList(carryOnApplication.loginUserModel.userDocumentId)
            }
            val recyclerViewList = work1.await()

            // 상태 관리 변수에 담아준다.
            contentListState.clear()
            contentListState.addAll(recyclerViewList)
            _isLoading.value = false
        }
    }

    fun deletePlanOnClick(tripDocumentId: String) {
        CoroutineScope(Dispatchers.Main).launch {
            _isLoading.value = true
            // 여행 정보를 삭제한다.
            val work1 = async(Dispatchers.IO){
                tripService.deleteTripData(tripDocumentId = tripDocumentId)
                planService.deleteAllPlansByTripId(tripDocumentId = tripDocumentId)
            }
            work1.join()

            gettingTripData()

            _isLoading.value = false
        }
    }

    fun joinSharedTrip(sharedCode: String) {
        // 공유 코드가 5자리가 아니면 함수 실행 중단
        if (sharedCode.length != 5) {
            // 유효하지 않은 코드에 대한 처리 (예: 사용자에게 에러 메시지 출력)
            return
        }
        CoroutineScope(Dispatchers.Main).launch {
            _isLoading.value = true
            // IO 디스패처에서 공유 코드를 이용해 TripModel을 가져온다.
            val trip = async(Dispatchers.IO) {
                tripService.selectTripDataOneBySharedCode(sharedCode)
            }.await()

            trip.let { tripData ->
                val currentUserId = carryOnApplication.loginUserModel.userDocumentId
                // shareUserDocumentId 리스트에 현재 유저의 documentId가 없으면 추가
                if (!tripData.shareUserDocumentId.contains(currentUserId)) {
                    tripData.shareUserDocumentId.add(currentUserId)
                    // 변경된 문서 정보를 서버에 업데이트
                    async(Dispatchers.IO) {
                        tripService.updateTripShareUser(tripData)
                    }.await()
                }
            }

            gettingTripData()

            _isLoading.value = false
        }
    }
}