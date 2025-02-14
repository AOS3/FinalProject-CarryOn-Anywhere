package com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.home

import android.content.Context
import androidx.lifecycle.ViewModel
import com.lion.FinalProject_CarryOn_Anywhere.CarryOnApplication
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.ScreenName
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class PlaceInfoViewModel @Inject constructor(
    @ApplicationContext context: Context
) : ViewModel()  {

    val carryOnApplication = context as CarryOnApplication

    // Back 버튼 동작 메서드
    fun navigationBackIconOnClick() {
        carryOnApplication.navHostController.popBackStack(
            ScreenName.PLACE_INFO_SCREEN.name,
            inclusive = true
        )
//        carryOnApplication.navHostController.navigate(ScreenName.MAIN_SCREEN.name) {
//            launchSingleTop = true
//        }
    }
}