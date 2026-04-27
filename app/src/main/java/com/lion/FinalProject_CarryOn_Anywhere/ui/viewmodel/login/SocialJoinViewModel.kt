package com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.login

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.lion.FinalProject_CarryOn_Anywhere.CarryOnApplication
import com.lion.FinalProject_CarryOn_Anywhere.data.server.model.UserModel
import com.lion.FinalProject_CarryOn_Anywhere.data.server.service.UserService
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.AppPushState
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.PrivacyPolicyAgree
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.ScreenName
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.UserState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SocialJoinViewModel
    @Inject constructor(
    @ApplicationContext context: Context,
    ) : ViewModel() {

    val carryOnApplication = context as CarryOnApplication

    // 아이디 입력 요소
    val textFieldSocialJoinIdValue = mutableStateOf("")

    // 아이디 중복 확인 여부
    val isCheckId = mutableStateOf(false)

    // 다이얼로그 상태변수
    val showDialogIdOk = mutableStateOf(false)
    val showDialogIdNo = mutableStateOf(false)
    val showDialogSocialJoinOk = mutableStateOf(false)
    val showDialogPrivacyDisagree = mutableStateOf(false)

    // 버튼 활성화 상태 변수
    val isButtonSocialJoinIdEnabled = mutableStateOf(false)
    val isButtonUserSocialJoinEnabled = mutableStateOf(false)

    // 텍스트필드 활성화 상태 변수 추가
    val isTextFieldSocialJoinIdEnabled = mutableStateOf(true)

    // 개인정보처리방침 체크박스
    val isCheckBoxSocialJoinPrivacyAccept = mutableStateOf(false)

    // 에러 메시지
    val textFieldSocialJoinIdErrorText = mutableStateOf("")
    val textFieldSocialJoinIdError = mutableStateOf(false)

    // 네비게이션 아이콘을 누르면 호출되는 메서드
    fun navigationIconOnClick() {
        // 스택 제거
        carryOnApplication.navHostController.popBackStack(ScreenName.USER_JOIN_SCREEN.name, inclusive = true)
        carryOnApplication.navHostController.navigate(ScreenName.LOGIN_SCREEN.name) {
            launchSingleTop = true
        }
    }

    init {
        updateCheckIdButtonState()
    }

    // 아이디 중복확인 메서드
    fun checkUserId(){
        // 사용자가 입력한 아이디
        val userId = textFieldSocialJoinIdValue.value

        // 사용할 수 있는 아이디인지 검사한다.
        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO) {
                UserService.checkJoinUserId(userId)
            }
            isCheckId.value = work1.await()

            if (isCheckId.value) {
                showDialogIdOk.value = true
                //updateUserJoinButtonState()

                // 아이디 중복확인 성공 시 텍스트필드, 버튼 비활성화
                isButtonSocialJoinIdEnabled.value = false
                isTextFieldSocialJoinIdEnabled.value = false // (새로 추가할 상태값)
            } else {
                showDialogIdNo.value = true
                updateCheckIdButtonState()
                //updateUserJoinButtonState()
            }
        }

    }

    // 중복확인 버튼 활성화 메서드
    fun updateCheckIdButtonState() {
        isButtonSocialJoinIdEnabled.value = textFieldSocialJoinIdValue.value.isNotBlank()
    }

    // 가입 완료 버튼을 눌렀을 때
    fun buttonSocialJoinSubmitOnClick() {
        var isError = false

        // 아이디 입력 여부 확인
        if (textFieldSocialJoinIdValue.value.isBlank()) {
            textFieldSocialJoinIdErrorText.value = "아이디를 입력해주세요."
            textFieldSocialJoinIdError.value = true
            isError = true
        } else if (!isCheckId.value) {
            textFieldSocialJoinIdErrorText.value = "아이디를 중복확인해주세요."
            textFieldSocialJoinIdError.value = true
            isError = true
        } else {
            textFieldSocialJoinIdErrorText.value = ""
            textFieldSocialJoinIdError.value = false
        }

        // 개인정보 처리방침 동의 체크 확인
        if (!isCheckBoxSocialJoinPrivacyAccept.value) {
            showDialogPrivacyDisagree.value = true
            isError = true
        } else {
            showDialogPrivacyDisagree.value = false
        }

        // 에러가 있으면 가입 진행 불가
        if (isError) return

        // 모든 조건 충족 → 사용자 데이터 저장
        saveUserData()
    }

    // 가입 성공하여 입력된 데이터를 UserData에 추가하는 메서드
    fun saveUserData() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                // 카카오 로그인 임시 저장 데이터 가져오기
                val email = carryOnApplication.tempSocialUserEmail
                val profileImage = carryOnApplication.tempSocialUserProfileImage
                val kakaoToken = carryOnApplication.tempSocialAccessToken
                val inputUserId = textFieldSocialJoinIdValue.value

                // 필수값 체크 (혹시 모를 예외 방어)
                if (email.isBlank() || inputUserId.isBlank()) {
                    Toast.makeText(carryOnApplication, "필수 정보를 입력해주세요.", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                // 가입
                val newUserModel = UserService.handleKakaoLogin(
                    email = email,
                    userId = inputUserId,
                    userProfileImage = profileImage,
                    kakaoToken = kakaoToken
                )

                if (newUserModel != null) {
                    // 가입 성공
                    carryOnApplication.loginUserModel = newUserModel
                    carryOnApplication.isLoggedIn.value = true

                    // 메인화면으로 이동
                    carryOnApplication.navHostController.navigate(ScreenName.MAIN_SCREEN.name) {
                        popUpTo(0)
                        launchSingleTop = true
                    }
                    // 자동 로그인 토큰값 갱신
                    UserService.updateUserAutoLoginToken(carryOnApplication, newUserModel.userDocumentId)
                } else {
                    // 가입 실패
                    Toast.makeText(carryOnApplication, "회원가입에 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("SocialJoinViewModel", "회원가입 오류: ${e.localizedMessage}")
                Toast.makeText(carryOnApplication, "회원가입 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 개인정보 처리방침 약관보기 클릭
    fun moveToUserJoinPrivacyWebView(){
        carryOnApplication.navHostController.navigate(ScreenName.PRIVACY_POLICY_SCREEN.name)
    }

    // 가입 완료 후 로그인 화면으로 이동
    fun moveToLoginScreen() {
        carryOnApplication.navHostController.popBackStack(
            ScreenName.SNS_JOIN_SCREEN.name,
            inclusive = true
        )
        carryOnApplication.navHostController.navigate(ScreenName.LOGIN_SCREEN.name) {
            launchSingleTop = true
        }
    }


}