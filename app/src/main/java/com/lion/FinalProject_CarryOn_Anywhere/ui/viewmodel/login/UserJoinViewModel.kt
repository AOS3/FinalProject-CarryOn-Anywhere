package com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.login

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.auth
import com.lion.FinalProject_CarryOn_Anywhere.CarryOnApplication
import com.lion.FinalProject_CarryOn_Anywhere.data.server.model.UserModel
import com.lion.FinalProject_CarryOn_Anywhere.data.server.service.UserService
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.AppPushState
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.ScreenName
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.UserState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class UserJoinViewModel
    @Inject constructor(
    @ApplicationContext context: Context,
) : ViewModel() {

    val carryOnApplication = context as CarryOnApplication

    // 아이디 입력 요소
    val textFieldUserJoinIdValue = mutableStateOf("")
    // 비밀번호 입력 요소
    val textFieldUserJoinPwValue = mutableStateOf("")
    // 비밀번호 확인 입력 요소
    val textFieldUserJoinCheckPwValue = mutableStateOf("")
    // 이름 입력 요소
    val textFieldUserJoinNameValue = mutableStateOf("")
    // 연락처 입력 요소
    val textFieldUserJoinPhoneValue = mutableStateOf("")
    // 인증번호 입력 요소
    val textFieldUserJoinAuthNumberValue = mutableStateOf("")

    // 아이디 중복 확인 여부
    val isCheckId = mutableStateOf(false)
    // 인증 여부
    val isCheckAuth = mutableStateOf(false)

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

    // 다이얼로그 제어 상태 변수
    val showDialogIdOk = mutableStateOf(false)
    val showDialogIdNo = mutableStateOf(false)
    val showDialogAuthOk = mutableStateOf(false)
    val showDialogAuthNo = mutableStateOf(false)
    val showDialogJoinOk = mutableStateOf(false)

    // 버튼 활성화 상태 변수
    val isButtonUserJoinIdEnabled = mutableStateOf(false)
    val isButtonUserJoinPhoneNoEnabled = mutableStateOf(false)
    val isButtonUserJoinAuthNoEnabled = mutableStateOf(false)
    val isButtonUserJoinJoinEnabled = mutableStateOf(false)

    // 에러 메시지
    val textFieldUserJoinIdErrorText = mutableStateOf("")
    val textFieldUserJoinPwErrorText = mutableStateOf("")
    val textFieldUserJoinCheckPwErrorText = mutableStateOf("")
    val textFieldUserJoinNameErrorText = mutableStateOf("")
    val textFieldUserJoinPhoneNoErrorText = mutableStateOf("")
    val textFieldUserJoinPhoneAuthNumberErrorText = mutableStateOf("")

    val textFieldUserJoinIdError = mutableStateOf(false)
    val textFieldUserJoinPwError = mutableStateOf(false)
    val textFieldUserJoinNameError = mutableStateOf(false)
    val textFieldUserJoinCheckPwError = mutableStateOf(false)
    val textFieldUserJoinPhoneNoError = mutableStateOf(false)
    val textFieldUserJoinPhoneAuthNumberError = mutableStateOf(false)

    // 휴대폰 인증
    lateinit var auth: FirebaseAuth
    var verificationId = ""

    // 네비게이션 아이콘을 누르면 호출되는 메서드
    fun navigationIconOnClick() {
        // 회원 가입 스택 제거
        carryOnApplication.navHostController.popBackStack(ScreenName.USER_JOIN_SCREEN.name, inclusive = true)
        carryOnApplication.navHostController.navigate(ScreenName.LOGIN_SCREEN.name) {
            launchSingleTop = true
        }
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
        isButtonUserJoinIdEnabled.value = textFieldUserJoinIdValue.value.isNotBlank()
    }

    // 아이디 중복확인 메서드
    fun checkUserId(){
        // 사용자가 입력한 아이디
        val userId = textFieldUserJoinIdValue.value

        // 사용할 수 있는 아이디인지 검사한다.
        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO) {
                UserService.checkJoinUserId(userId)
            }
            isCheckId.value = work1.await()

            if (isCheckId.value) {
                showDialogIdOk.value = true
                updateUserJoinButtonState()
            } else {
                showDialogIdNo.value = true
                updateCheckIdButtonState()
                updateUserJoinButtonState()
            }
        }

    }

    // 인증요청 버튼 활성화 메서드
    fun updateSendAutoButtonState() {
        isButtonUserJoinPhoneNoEnabled.value = textFieldUserJoinPhoneValue.value.isNotBlank()
    }

    fun signInWithCredential(credential: PhoneAuthCredential, context: Context) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    showDialogAuthOk.value = true
                } else {
                    showDialogAuthNo.value = true
                }
            }
    }

    // 인증 코드 전송 메서드
    fun sendVerificationCode(phoneNumber: String, context: Context) {
        val formattedPhoneNumber = if (phoneNumber.startsWith("0")) {
            "+82" + phoneNumber.substring(1) // 01012345678 → +821012345678
        } else {
            phoneNumber
        }

        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                signInWithCredential(credential, context)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(context, "인증 실패: ${e.message}", Toast.LENGTH_SHORT).show()
            }

            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                this@UserJoinViewModel.verificationId = verificationId
                Toast.makeText(context, "인증 코드가 전송되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        // FirebaseAuth 객체 초기화
        auth = FirebaseAuth.getInstance()

        val activity = context as? Activity ?: run {
            Log.e("FirebaseAuth", "Activity를 찾을 수 없음")
            return
        }

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(formattedPhoneNumber) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(activity) // Activity (for callback binding)
            .setCallbacks(callbacks) // Callbacks
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    // 인증요청 버튼 활성화 메서드
    fun updateCheckAuthButtonState() {
        isButtonUserJoinAuthNoEnabled.value = textFieldUserJoinAuthNumberValue.value.isNotBlank()
    }

    // 인증 확인 버튼을 눌렀을 때
    fun buttonCheckAuthOnClick(context: Context) {
        val code = textFieldUserJoinAuthNumberValue.value // 사용자가 입력한 인증번호

        if (verificationId.isEmpty()) {
            Toast.makeText(context, "인증 요청을 진행해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        val credential = PhoneAuthProvider.getCredential(verificationId, code)

        // signInWithCredential(credential, context)

        // Firebase 인증 진행 (이전 signInWithCredential 함수 대체)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // 인증 성공
                    isCheckAuth.value = true
                    showDialogAuthOk.value = true
                } else {
                    // 인증 실패
                    textFieldUserJoinAuthNumberValue.value = ""
                    isCheckAuth.value = false
                    showDialogAuthNo.value = true
                }
            }
    }


    // 가입 완료 버튼을 눌렀을 때
    fun buttonUserJoinSubmitOnClick() {
        var isError = false

        // 아이디 입력 여부 확인
        if (textFieldUserJoinIdValue.value.isBlank()) {
            textFieldUserJoinIdErrorText.value = "아이디를 입력해주세요."
            textFieldUserJoinIdError.value = true
            isError = true
        } else if (!isCheckId.value) {
            textFieldUserJoinIdErrorText.value = "아이디를 중복확인해주세요."
            textFieldUserJoinIdError.value = true
            isError = true
        } else {
            textFieldUserJoinIdErrorText.value = ""
            textFieldUserJoinIdError.value = false
        }

        // 비밀번호 검증
        val password = textFieldUserJoinPwValue.value
        val confirmPassword = textFieldUserJoinCheckPwValue.value

        if (password.isBlank()) {
            textFieldUserJoinPwErrorText.value = "비밀번호를 입력해주세요."
            textFieldUserJoinPwError.value = true
            isError = true
        } else if (password.length < 8) {
            textFieldUserJoinPwErrorText.value = "비밀번호는 8자 이상이어야 합니다."
            textFieldUserJoinPwError.value = true
            isError = true
        } else {
            textFieldUserJoinPwErrorText.value = ""
            textFieldUserJoinPwError.value = false
        }

        // 비밀번호 확인 검증
        if (confirmPassword.isBlank()) {
            textFieldUserJoinCheckPwErrorText.value = "비밀번호 확인을 입력해주세요."
            textFieldUserJoinCheckPwError.value = true
            isError = true
        } else if (password != confirmPassword) {
            textFieldUserJoinCheckPwErrorText.value = "비밀번호를 동일하게 입력해주세요."
            textFieldUserJoinCheckPwError.value = true
            isError = true
        } else {
            textFieldUserJoinCheckPwErrorText.value = ""
            textFieldUserJoinCheckPwError.value = false
        }

        // 이름 입력 여부 확인
        if (textFieldUserJoinNameValue.value.isBlank()) {
            textFieldUserJoinNameErrorText.value = "이름을 입력해주세요."
            textFieldUserJoinNameError.value = true
            isError = true
        } else {
            textFieldUserJoinNameErrorText.value = ""
            textFieldUserJoinNameError.value = false
        }

        // 휴대폰 입력 여부 확인
        if (textFieldUserJoinPhoneValue.value.isBlank()) {
            textFieldUserJoinPhoneNoErrorText.value = "핸드폰 번호를 입력해주세요."
            textFieldUserJoinPhoneNoError.value = true
            isError = true
        } else {
            textFieldUserJoinPhoneNoErrorText.value = ""
            textFieldUserJoinPhoneNoError.value = false
        }

        // 인증번호 입력 여부 확인
        if (textFieldUserJoinAuthNumberValue.value.isBlank()) {
            textFieldUserJoinPhoneAuthNumberErrorText.value = "인증번호를 입력해주세요."
            textFieldUserJoinPhoneAuthNumberError.value = true
            isError = true
        } else {
            textFieldUserJoinPhoneAuthNumberErrorText.value = ""
            textFieldUserJoinPhoneAuthNumberError.value = false
        }

        // 에러가 있으면 가입 진행 불가
        if (isError) return

        // 모든 조건 충족 → 사용자 데이터 저장
        saveUserData()
    }

    // 가입 성공하여 입력된 데이터를 UserData에 추가하는 메서드
    fun saveUserData() {
        // 저장할 데이터를 추출
        val userModel = UserModel()

        userModel.userId = textFieldUserJoinIdValue.value
        userModel.userPw = textFieldUserJoinPwValue.value
        userModel.userName = textFieldUserJoinNameValue.value
        userModel.userPhoneNumber = textFieldUserJoinPhoneValue.value
        userModel.userTimeStamp = System.nanoTime()
        userModel.userImage = "none"
        userModel.userState = UserState.USER_STATE_NORMAL
        userModel.userAppPushAgree = AppPushState.APP_PUSH_DISABLE

        // 저장
        // 저장한다.
        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO){
                UserService.addUserData(userModel)
            }
            work1.join()

            // 다이얼로그 표시
            showDialogJoinOk.value = true

            if (showDialogJoinOk.value == false) {
                carryOnApplication.navHostController.popBackStack()
                carryOnApplication.navHostController.popBackStack(ScreenName.USER_JOIN_SCREEN.name, inclusive = true)
                carryOnApplication.navHostController.navigate("login")
            }

        }
    }

    // 가입 완료 후 로그인 화면으로 이동
    fun moveToLoginScreen() {
        carryOnApplication.navHostController.popBackStack(
            ScreenName.USER_JOIN_SCREEN.name,
            inclusive = true
        )
        carryOnApplication.navHostController.navigate(ScreenName.LOGIN_SCREEN.name) {
            launchSingleTop = true
        }
    }
}