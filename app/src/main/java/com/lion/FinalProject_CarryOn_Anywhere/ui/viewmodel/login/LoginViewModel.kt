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
class LoginViewModel
    @Inject constructor(
    @ApplicationContext context: Context
) : ViewModel() {

    val carryOnApplication = context as CarryOnApplication

    // 로그인, 비밀번호 - textField
    val textFieldLoginIdValue = mutableStateOf("")
    val textFieldLoginPwValue = mutableStateOf("")

    // 자동 로그인 - checkBox
    val isAutoLoginEnabled = mutableStateOf(false)

    // 로그인 버튼 - button
    val isButtonEnabled = mutableStateOf(false)

    // 텍스트 필드 버튼 활성화 메서드
    fun updateButtonState() {
        isButtonEnabled.value = textFieldLoginIdValue.value.isNotEmpty() &&
                textFieldLoginPwValue.value.isNotEmpty()
    }

    fun buttonAroundClick() {
        // 메인 화면으로 이동
        carryOnApplication.navHostController.navigate(ScreenName.MAIN_SCREEN.name)
    }

    // 로그인 버튼 동작 메서드
    fun buttonLoginClick() {
        // 아이디, 비밀번호 체크

        // 메인 화면으로 이동
        carryOnApplication.navHostController.navigate(ScreenName.MAIN_SCREEN.name)
    }

    // 회원가입 화면 이동 메서드
    fun buttonUserJoinClick() {
        carryOnApplication.navHostController.navigate(ScreenName.USER_JOIN_SCREEN.name)
    }

    // 아이디 찾기 화면 이동 메서드
    fun buttonFindIdClick() {
        carryOnApplication.navHostController.navigate(ScreenName.FIND_ID_SCREEN.name)
    }

    // 비밀번호 찾기 화면 이동 메서드
    fun buttonFindPwClick() {
        carryOnApplication.navHostController.navigate(ScreenName.FIND_PW_SCREEN.name)

    }
}