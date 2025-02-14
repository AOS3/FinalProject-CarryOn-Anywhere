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
class FindPwViewModel
@Inject constructor(
    @ApplicationContext context: Context
) : ViewModel() {

    val carryOnApplication = context as CarryOnApplication

    // 아이디 입력 요소
    val textFieldFindPwIdValue = mutableStateOf("")
    // 연락처 입력 요소
    val textFieldFindPwPhoneValue = mutableStateOf("")
    // 인증번호 입력 요소
    val textFieldFindPwAuthNumberValue = mutableStateOf("")

    val isButtonFindPwIdEnabled = mutableStateOf(false)
    val isButtonFindPwPhoneNoEnabled = mutableStateOf(false)
    val isButtonFindPwAuthNoEnabled = mutableStateOf(false)
    val isButtonFindPwJoinEnabled = mutableStateOf(false)

    // Back 버튼 동작 메서드
    fun navigationIconOnClick() {
        carryOnApplication.navHostController.popBackStack(
            ScreenName.FIND_PW_SCREEN.name,
            inclusive = true
        )
//        carryOnApplication.navHostController.navigate(ScreenName.LOGIN_SCREEN.name) {
//            launchSingleTop = true
//        }
    }

    // 인증요청 버튼 활성화 메서드
    fun updateSendAutoButtonState() {
        isButtonFindPwPhoneNoEnabled.value = textFieldFindPwPhoneValue.value.isNotBlank()
    }

    // 인증하기 버튼 활성화 메서드
    fun updateCheckAuthButtonState() {
        isButtonFindPwAuthNoEnabled.value = textFieldFindPwAuthNumberValue.value.isNotBlank()
    }

    // 완료 버튼 활성화 조건 메서드
    fun updateDoneButtonState() {

    }

    // 휴대폰 인증 요청 버튼을 눌렀을 때
    fun buttonFindPwVerificationOnClick() {

    }

    // 완료 버튼을 눌렀을 때
    fun buttonDoneOnClick() {
        carryOnApplication.navHostController.navigate(ScreenName.CHANGE_PW_SCREEN.name)
    }
}