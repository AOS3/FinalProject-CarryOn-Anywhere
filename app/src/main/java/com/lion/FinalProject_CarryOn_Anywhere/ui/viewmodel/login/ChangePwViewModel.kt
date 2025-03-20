package com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.login

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lion.FinalProject_CarryOn_Anywhere.CarryOnApplication
import com.lion.FinalProject_CarryOn_Anywhere.data.server.model.UserModel
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

    val isButtonChangePwDoneEnabled = mutableStateOf(false) // 비밀번호 변경 버튼 활성화

    // 다이얼로그 제어 변수
    val showDialogPwOk = mutableStateOf(false)

    // userModel 상태 저장
    val userModel = mutableStateOf<UserModel?>(null)

    // 에러 메시지
    val textFieldChangePwConditionErrorText = mutableStateOf("") // 조건에 맞지 않음
    val textFieldChangePwMismatchErrorText = mutableStateOf("") // 새 비밀번호 값과 다름

    val textFieldChangePwConditionError = mutableStateOf(false)
    val textFieldChangePwMismatchError = mutableStateOf(false)

    // Back 버튼 동작 메서드
    fun navigationIconOnClick() {
        carryOnApplication.navHostController.popBackStack(
            ScreenName.CHANGE_PW_SCREEN.name,
            inclusive = true
        )
    }

    // 전달받은 userId로 userModel을 가져온다.
    fun gettingUserModel(userId: String) {
        viewModelScope.launch {
            try {
                val user = UserService.selectUserDataByUserIdOne(userId)
                if (user != null) {
                    userModel.value = user
                    Log.d("CHANGE_PW", userId)
                } else {
                    userModel.value = null
                }
            } catch (e: Exception) {
                userModel.value = null
            }
        }
    }

    // 비밀번호 변경 버튼 활성화 메서드
    fun updateDoneButtonState() {
        isButtonChangePwDoneEnabled.value = textFieldChangePwPwValue.value.isNotBlank() && textFieldChangePwCheckPwValue.value.isNotBlank()
    }

    // 비밀번호 변경 버튼
    fun buttonChangePwDoneOnClick() {
        viewModelScope.launch {
            val user = userModel.value

            // user가 null이면 실행 중지
            if (user == null) {
                return@launch
            }
            // 유효성 검사를 통과하지 않으면 실행 중지
            if (!validatePassword()) {
                return@launch
            }

            // 비밀번호 변경
            UserService.updateUserPwData(user, textFieldChangePwPwValue.value.toString())

            // 비밀번호 변경 성공 시 다이얼로그 표시
            showDialogPwOk.value = true
        }
    }

    // 비밀번호 변경 성공 시 화면 이동
    fun navigationConfirmButtonClick() {
        showDialogPwOk.value = false
        carryOnApplication.navHostController.navigate(ScreenName.LOGIN_SCREEN.name) {
            popUpTo(ScreenName.LOGIN_SCREEN.name) {inclusive = true} // 뒤로가기 스택 정리
            launchSingleTop = true
        }
    }

    // ✅ 비밀번호 유효성 검사 메서드
    fun validatePassword(): Boolean {
        // 초기화
        textFieldChangePwConditionError.value = false
        textFieldChangePwMismatchError.value = false

        textFieldChangePwConditionErrorText.value = ""
        textFieldChangePwMismatchErrorText.value = ""

        return when {
            // 새 비밀번호가 8자 미만이면 에러 표시
            textFieldChangePwPwValue.value.length < 8 -> {
                textFieldChangePwConditionError.value = true
                textFieldChangePwConditionErrorText.value = "조건에 맞지 않습니다."
                false
            }

            // 비밀번호가 일치하지 않는 경우
            textFieldChangePwPwValue.value != textFieldChangePwCheckPwValue.value -> {
                textFieldChangePwMismatchError.value = true
                textFieldChangePwMismatchErrorText.value = "새 비밀번호가 동일하지 않습니다."
                false
            }
            else -> true
        }
    }

//    // 현재 비밀번호 가져오기
//    suspend fun selectUserPasswordByUserId(userId:String): String? {
//        return UserService.selectUserPasswordByUserId(userId)
//    }

}