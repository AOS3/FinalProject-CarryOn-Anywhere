package com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.mypage

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
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
            val work1 = async(Dispatchers.IO){
                tripService.gettingTripList(carryOnApplication.loginUserModel.userDocumentId)
            }
            val recyclerViewList = work1.await()

            // 상태 관리 변수에 담아준다.
            contentListState.clear()
            contentListState.addAll(recyclerViewList)
        }
    }

    fun deletePlanOnClick(tripDocumentId: String) {
        CoroutineScope(Dispatchers.Main).launch {
            // 여행 정보를 삭제한다.
            val work1 = async(Dispatchers.IO){
                tripService.deleteTripData(tripDocumentId = tripDocumentId)
                planService.deleteAllPlansByTripId(tripDocumentId = tripDocumentId)
            }
            work1.join()

            gettingTripData()
        }
    }
}