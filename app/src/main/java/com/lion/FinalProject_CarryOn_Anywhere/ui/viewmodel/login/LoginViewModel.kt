package com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.login

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import com.lion.FinalProject_CarryOn_Anywhere.CarryOnApplication
import com.lion.FinalProject_CarryOn_Anywhere.data.server.service.UserService
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.LoginResult
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.ScreenName
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel
@Inject constructor(
    @ApplicationContext context: Context,
) : ViewModel() {

    val carryOnApplication = context as CarryOnApplication

    // 로그인, 비밀번호 - textField
    val textFieldLoginIdValue = mutableStateOf("")
    val textFieldLoginPwValue = mutableStateOf("")

    // 자동 로그인 - checkBox
    val isAutoLoginEnabled = mutableStateOf(false)

    // 로그인 버튼 - button
    val isButtonEnabled = mutableStateOf(false)

    // 에러 상태 변수
    val textFieldLoginIdErrorText = mutableStateOf("")
    val textFieldLoginPwErrorText = mutableStateOf("")

    val textFieldLoginIdError= mutableStateOf(false)
    val textFieldLoginPwError= mutableStateOf(false)
    val alertDialogLoginSignOutError = mutableStateOf(false) // 탈퇴 회원 다이얼로그

    // 로딩 상태 관리
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

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

        var isError = false

        val loginUserId = textFieldLoginIdValue.value
        val loginUserPw = textFieldLoginPwValue.value

        // 아이디 입력 여부 검증
        when {
            loginUserId.isEmpty() -> {
                textFieldLoginIdErrorText.value = "아이디를 입력해주세요."
                textFieldLoginIdError.value = true
                isError = true
            }
            else -> {
                textFieldLoginIdErrorText.value = ""
                textFieldLoginIdError.value = false
            }
        }

        // 비밀번호 입력 여부 검증
        when {
            loginUserPw.isEmpty() -> {
                textFieldLoginPwErrorText.value = "비밀번호를 입력해주세요."
                textFieldLoginPwError.value = true
                isError = true
            }
            else -> {
                textFieldLoginPwErrorText.value = ""
                textFieldLoginPwError.value = false
            }
        }

        // 에러가 있으면 로그인 진행 중단
        if (isError) return

        viewModelScope.launch {

            _isLoading.value = true

            val loginResult = withContext(Dispatchers.IO) {
                UserService.checkLogin(loginUserId, loginUserPw)
            }

            when(loginResult) {
                LoginResult.LOGIN_RESULT_ID_NOT_EXIST -> {
                    textFieldLoginIdError.value = true
                    textFieldLoginIdErrorText.value = "존재하지 않는 아이디입니다."
                }
                LoginResult.LOGIN_RESULT_PASSWORD_INCORRECT -> {
                    textFieldLoginPwError.value = true
                    textFieldLoginPwErrorText.value = "잘못된 비밀번호 입니다."
                }
                LoginResult.LOGIN_RESULT_SIGN_OUT_MEMBER -> {
                    alertDialogLoginSignOutError.value = true
                }
                LoginResult.LOGIN_RESULT_SUCCESS -> {
                    // 로그인 한 사용자 정보를 가져온다.
                    val work2 = async(Dispatchers.IO) {
                        UserService.selectUserDataByUserIdOne(loginUserId)
                    }
                    val loginUserModel = work2.await()

                    // 자동 로그인 체크 시
                    if (isAutoLoginEnabled.value) {
                        CoroutineScope(Dispatchers.Main).launch{
                            val work1 = async(Dispatchers.IO){
                                UserService.updateUserAutoLoginToken(carryOnApplication, loginUserModel.userDocumentId)
                            }
                            work1.join()
                        }
                    }

                    // Application 객체에 로그인 한 사용자의 정보를 담고 메인 화면으로 이동
                    carryOnApplication.loginUserModel = loginUserModel
                    // 로그인 상태 업데이트
                    carryOnApplication.isLoggedIn.value = true

//                    carryOnApplication.navHostController.popBackStack(ScreenName.LOGIN_SCREEN.name, true)
//                    carryOnApplication.navHostController.navigate(ScreenName.MAIN_SCREEN.name)

                    // 백스택을 완전히 제거하고 메인 화면으로 이동
                    carryOnApplication.navHostController.navigate(ScreenName.MAIN_SCREEN.name) {
                        popUpTo(0) // 모든 백스택 제거
                        launchSingleTop = true
                    }
                }
            }

            _isLoading.value = false
        }

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

    // 카카오 로그인 메서드
    fun loginWithKakao(activity: Activity) {
        val context = carryOnApplication.applicationContext

        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                if (error != null) {
                    Log.e("KakaoLogin", "카카오톡 로그인 실패, 계정 로그인 시도", error)
                    UserApiClient.instance.loginWithKakaoAccount(activity) { token, error ->
                        handleLoginResult(token, error)
                    }
                } else {
                    handleLoginResult(token, error)
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(activity) { token, error ->
                handleLoginResult(token, error)
            }
        }
    }

    fun handleLoginResult(token: OAuthToken?, error: Throwable?) {
        if (error != null) {
            Log.e("KakaoLogin", "로그인 실패", error)
        } else if (token != null) {
            Log.d("KakaoLogin", "로그인 성공 ${token.accessToken}")
            fetchUserInfo(token.accessToken)
        }

    }

    fun fetchUserInfo(kakaoToken:String) {
        viewModelScope.launch {
            UserApiClient.instance.me { user, error ->
                if (error != null) {
                    Log.e("KakaoLogin", "사용자 정보 요청 실패", error)
                    return@me
                }

                if (user == null) {
                    Log.e("KakaoLogin", "사용자 정보가 null입니다.")
                    return@me
                }

                Log.d("KakaoLogin", "사용자 전체 정보: $user")

                val email = user.kakaoAccount?.email ?: "이메일 없음"
                val nickname = user.kakaoAccount?.profile?.nickname ?: "닉네임 없음"
                val profileImage = user.kakaoAccount?.profile?.thumbnailImageUrl ?: ""

                val kakaoToken = kakaoToken

                viewModelScope.launch(Dispatchers.IO) {
                    val userModel = UserService.handleKakaoLogin(email, nickname, profileImage, kakaoToken)

                    withContext(Dispatchers.Main) {
                        if (userModel != null) {
                            Log.d("KakaoLogin", "Firestore에서 가져온 유저: ${userModel.userId}")

                            // CarryOnApplication에 로그인 유저 정보 저장
                            carryOnApplication.loginUserModel = userModel

                            // 메인 화면으로 이동
                            carryOnApplication.navHostController.navigate(ScreenName.MAIN_SCREEN.name)
                        } else {
                            Log.e("KakaoLogin", "Firestore 유저 정보를 가져오는 데 실패했음")
                        }
                    }
                }
            }
        }
    }

}