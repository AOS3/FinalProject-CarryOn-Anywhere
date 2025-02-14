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
class UserJoinViewModel
    @Inject constructor(
    @ApplicationContext context: Context
) : ViewModel() {

    val carryOnApplication = context as CarryOnApplication

    // 아이디 입력 요소
    val textFieldUserJoinIdValue = mutableStateOf("")
    // 비밀번호 입력 요소
    val textFieldUserJoinPwValue = mutableStateOf("")
    // 비밀번호 입력 요소
    val textFieldUserJoinCheckPwValue = mutableStateOf("")
    // 이름 입력 요소
    val textFieldUserJoinNameValue = mutableStateOf("")
    // 연락처 입력 요소
    val textFieldUserJoinPhoneValue = mutableStateOf("")
    // 인증번호 입력 요소
    val textFieldUserJoinAuthNumberValue = mutableStateOf("")

    // 조건 충족 여부 상태
    // 8자 이상
    val isJoinLengthValid = mutableStateOf(false)
    // 특수문자 불가
    val isJoinSpecialCharInvalid = mutableStateOf(true)
    // 자음 모음 단독 사용 불가
    val isJoinConsonantVowelValid = mutableStateOf(false)

    // 2~10자
    val isJoinIdPwLengthValid = mutableStateOf(false)
    // 아이디랑 같은지
    val isJoinContainsIdValid = mutableStateOf(false)

    val isButtonUserJoinIdEnabled = mutableStateOf(false)
    val isButtonUserJoinPhoneNoEnabled = mutableStateOf(false)
    val isButtonUserJoinAuthNoEnabled = mutableStateOf(false)
    val isButtonUserJoinJoinEnabled = mutableStateOf(false)


    // 네비게이션 아이콘을 누르면 호출되는 메서드
    fun navigationIconOnClick() {
        // 회원 가입 스택 제거
        carryOnApplication.navHostController.popBackStack(ScreenName.USER_JOIN_SCREEN.name, inclusive = true)
        carryOnApplication.navHostController.navigate(ScreenName.LOGIN_SCREEN.name) {
            launchSingleTop = true
        }
    }

    // 입력 필드 체크하는 메서드
    fun joinIdPwCondition() {
        val id = textFieldUserJoinIdValue.value
        val pw = textFieldUserJoinPwValue.value

    }

    init {
        updateCheckIdButtonState()
        updateUserJoinButtonState()
    }

    // 가입 완료 버튼 활성화 조건 메서드
    fun updateUserJoinButtonState() {

    }

    // 중복확인 버튼 활성화 메서드
    fun updateCheckIdButtonState() {
        isButtonUserJoinJoinEnabled.value = textFieldUserJoinIdValue.value.isNotBlank()
    }

    // 아이디 중복확인 메서드
    fun checkUserId(){
        // 사용자가 입력한 아이디
        val userId = textFieldUserJoinIdValue.value


    }

    // 인증요청 버튼 활성화 메서드
    fun updateSendAutoButtonState() {
        isButtonUserJoinPhoneNoEnabled.value = textFieldUserJoinPhoneValue.value.isNotBlank()
    }

    // 휴대폰 인증 요청 버튼을 눌렀을 때
    fun buttonUserAuthOnClick() {

    }

    // 인증 확인 버튼을 눌렀을 때
    fun buttonCheckAuthOnClick() {

    }


    // 가입 완료 버튼을 눌렀을 때
    fun buttonUserJoinSubmitOnClick(){

    }


}