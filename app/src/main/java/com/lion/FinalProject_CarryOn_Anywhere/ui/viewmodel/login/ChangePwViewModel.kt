package com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.login

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.lion.FinalProject_CarryOn_Anywhere.CarryOnApplication
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.ScreenName
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class ChangePwViewModel@Inject constructor(
    @ApplicationContext context: Context
) : ViewModel() {

    val carryOnApplication = context as CarryOnApplication

    // 비밀번호 입력 요소
    val textFieldChangePwPwValue = mutableStateOf("")
    // 비밀번호 입력 요소
    val textFieldChangePwCheckPwValue = mutableStateOf("")

    // 다이얼로그 제어 변수
    val showDialogPwOk = mutableStateOf(false)

    // Back 버튼 동작 메서드
    fun navigationIconOnClick() {
        carryOnApplication.navHostController.popBackStack(
            ScreenName.CHANGE_PW_SCREEN.name,
            inclusive = true
        )
//        carryOnApplication.navHostController.navigate(ScreenName.LOGIN_SCREEN.name) {
//            launchSingleTop = true
//        }
    }

    // 비밀번호 변경 다이얼로그 확인 버튼 눌렀을 때
    fun navigationConfirmButtonClick() {
        showDialogPwOk.value = false
        carryOnApplication.navHostController.navigate(ScreenName.LOGIN_SCREEN.name) {
            popUpTo(ScreenName.LOGIN_SCREEN.name) {inclusive = true} // 뒤로가기 스택 정리
            launchSingleTop = true
        }
    }

    // 완료 버튼 눌렀을 때
    fun buttonChangePwDoneOnClick() {
        showDialogPwOk.value = true
    }


}