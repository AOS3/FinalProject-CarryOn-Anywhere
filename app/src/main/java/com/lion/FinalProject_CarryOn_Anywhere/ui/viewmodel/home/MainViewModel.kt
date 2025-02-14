package com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.home

import android.content.Context
import androidx.lifecycle.ViewModel
import com.lion.FinalProject_CarryOn_Anywhere.CarryOnApplication
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.ScreenName
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    @ApplicationContext context: Context
) : ViewModel() {

    val carryOnApplication = context as CarryOnApplication

    // 내 일정 보기 버튼 클릭
    fun buttonMainUserTripList() {

    }

    // 일정 등록 버튼 클릭
    fun buttonMainAddTrip() {
        carryOnApplication.navHostController.navigate(ScreenName.SELECT_TRIP_REGION.name)
    }

    // search 로 이동하는 메서드
    fun searchOnClick(){
        carryOnApplication.navHostController.navigate(ScreenName.PLACE_SEARCH_SCREEN.name)
    }

    // 여행 후기로 이동하는 메서드
    fun showTripReviewDetailScreen(){
        //carryOnApplication.navHostController.navigate()
    }
}