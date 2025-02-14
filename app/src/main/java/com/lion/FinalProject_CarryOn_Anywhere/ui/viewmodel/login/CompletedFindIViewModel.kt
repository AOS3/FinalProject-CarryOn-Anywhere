package com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.login

import android.content.Context
import androidx.lifecycle.ViewModel
import com.lion.FinalProject_CarryOn_Anywhere.CarryOnApplication
import com.lion.FinalProject_CarryOn_Anywhere.util.ScreenName
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class CompletedFindIViewModel
@Inject constructor(
    @ApplicationContext context:Context
) : ViewModel() {

    val carryOnApplication = context as CarryOnApplication

    // Back 버튼 동작 메서드
    fun navigationIconOnClick() {
        carryOnApplication.navHostController.popBackStack(
            ScreenName.COMPLETED_FIND_ID_SCREEN.name,
            inclusive = true
        )
        carryOnApplication.navHostController.navigate(ScreenName.FIND_ID_SCREEN.name) {
            launchSingleTop = true
        }
    }

    // 완료 버튼을 눌렀을 때
    fun buttonCompleteFindIdLoginOnClick() {
        carryOnApplication.navHostController.popBackStack(
            ScreenName.COMPLETED_FIND_ID_SCREEN.name,
            inclusive = true
        )
        carryOnApplication.navHostController.navigate(ScreenName.LOGIN_SCREEN.name) {
            launchSingleTop = true
        }
    }

}