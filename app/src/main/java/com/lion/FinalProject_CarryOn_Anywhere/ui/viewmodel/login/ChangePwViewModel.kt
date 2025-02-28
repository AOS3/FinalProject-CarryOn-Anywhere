package com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.login

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lion.FinalProject_CarryOn_Anywhere.CarryOnApplication
import com.lion.FinalProject_CarryOn_Anywhere.data.server.service.UserService
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.ScreenName
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
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
    }

    // 비밀번호 변경 성공 시 화면 이동
    fun navigationConfirmButtonClick() {
        showDialogPwOk.value = false
        carryOnApplication.navHostController.navigate(ScreenName.LOGIN_SCREEN.name) {
            popUpTo(ScreenName.LOGIN_SCREEN.name) {inclusive = true} // 뒤로가기 스택 정리
            launchSingleTop = true
        }
    }

    // 비밀번호 변경 버튼
    fun buttonChangePwDoneOnClick() {

        viewModelScope.launch {
            if (validatePassword()) {
                UserService.updateUserPwData(
                    carryOnApplication.loginUserModel,
                    textFieldChangePwPwValue.value.toString()
                )
                showDialogPwOk.value = true // 비밀번호 변경 성공 다이얼로그 표시
            }
        }

    }

    val showDialogPwMismatch = mutableStateOf(false) // 새 비밀번호 불일치 확인
    val showDialogPwShort = mutableStateOf(false) // 새 비밀번호 10자 미만 확인

    // ✅ 비밀번호 유효성 검사 메서드
    fun validatePassword(): Boolean {

        return when {

            textFieldChangePwPwValue.value.length < 8 -> {
                showDialogPwShort.value = true // 새 비밀번호가 8자 미만이면 다이얼로그 표시
                false
            }

            textFieldChangePwPwValue.value != textFieldChangePwCheckPwValue.value -> {
                showDialogPwMismatch.value = true // 새 비밀번호 확인이 일치하지 않으면 다이얼로그 표시
                false
            }
            else -> true
        }
    }

    // 현재 비밀번호 가져오기
    suspend fun selectUserPasswordByUserId(userId:String): String? {
        return UserService.selectUserPasswordByUserId(userId)
    }

}