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
class FindIdViewModel
@Inject constructor(
    @ApplicationContext context: Context
) : ViewModel() {

    val carryOnApplication = context as CarryOnApplication

    // 이름 입력 요소
    val textFieldFindIdNameValue = mutableStateOf("")
    // 연락처 입력 요소
    val textFieldFindIdPhoneValue = mutableStateOf("")
    // 인증번호 입력 요소
    val textFieldFindIdAuthNumberValue = mutableStateOf("")

    val isButtonFindIdNameEnabled = mutableStateOf(false)
    val isButtonFindIdPhoneNoEnabled = mutableStateOf(false) // 인증 요청 버튼 활성화
    val isButtonFindIdAuthNoEnabled = mutableStateOf(false) // 인증 확인 버튼 활성화
    val isButtonFindIdJoinEnabled = mutableStateOf(false)

    // Back 버튼 동작 메서드
    fun navigationIconOnClick() {
        carryOnApplication.navHostController.popBackStack(
            ScreenName.FIND_ID_SCREEN.name,
            inclusive = true
        )
//        carryOnApplication.navHostController.navigate(ScreenName.LOGIN_SCREEN.name) {
//            launchSingleTop = true
//        }
    }

    // 휴대폰 인증 요청 버튼을 눌렀을 때
    fun buttonUserAuthOnClick() {
    }

    // 완료 버튼을 눌렀을 때
    fun buttonDoneOnClick() {
        carryOnApplication.navHostController.navigate(ScreenName.COMPLETED_FIND_ID_SCREEN.name)
    }

    // 인증요청 버튼 활성화 메서드
    fun updateSendAutoButtonState() {
        isButtonFindIdPhoneNoEnabled.value = textFieldFindIdPhoneValue.value.isNotBlank()
    }

    // 인증하기 버튼 활성화 메서드
    fun updateCheckAuthButtonState() {
        isButtonFindIdAuthNoEnabled.value = textFieldFindIdAuthNumberValue.value.isNotBlank()
    }

    // 완료 버튼 활성화 조건 메서드
    fun updateDoneButtonState() {

    }

}