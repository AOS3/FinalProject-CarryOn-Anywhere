package com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.login

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.lion.FinalProject_CarryOn_Anywhere.CarryOnApplication
import com.lion.FinalProject_CarryOn_Anywhere.data.server.service.UserService
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.ScreenName
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
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

    // 인증 여부
    val isCheckAuth = mutableStateOf(false)

    // 에러 메시지
    val textFieldUFindPwAuthNumberErrorText = mutableStateOf("")
    val textFieldUFindPwAuthNumberError = mutableStateOf(false)

    // 다이얼로그 제어 상태 변수
    val showDialogMatchNo = mutableStateOf(false)

    // 휴대폰 인증
    lateinit var auth: FirebaseAuth
    var verificationId = ""

    val isButtonFindPwIdEnabled = mutableStateOf(false)
    val isButtonFindPwPhoneNoEnabled = mutableStateOf(false) // 인증 요청 버튼 활성화
    val isButtonFindPwAuthNoEnabled = mutableStateOf(false) //인증 확인 버튼 활성화
    val isButtonFindPwJoinEnabled = mutableStateOf(false)
    val isButtonFindPwDoneEnabled = mutableStateOf(false)

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
        // 필드 입력, 번호 인증 완료했을 경우 활성화
        isButtonFindPwDoneEnabled.value =
            textFieldFindPwIdValue.value.isNotBlank() &&
                    textFieldFindPwPhoneValue.value.isNotBlank() &&
                    textFieldFindPwAuthNumberValue.value.isNotBlank() &&
                    isCheckAuth.value

    }

//    // 휴대폰 인증 요청 버튼을 눌렀을 때
//    fun buttonFindPwVerificationOnClick() {
//
//    }

    // 완료 버튼을 눌렀을 때
    fun buttonDoneOnClick() {
        viewModelScope.launch {
            // ID과 폰 번호로 User 정보를 가져오기
            val userModel = UserService.getUserDocumentIdByNameAndPhone(
                textFieldFindPwIdValue.value,
                textFieldFindPwPhoneValue.value
            )

            if (userModel != null) {
                // 유저 정보를 담아 화면 이동
                val userId = userModel.userId
                carryOnApplication.navHostController.navigate(
                    "${ScreenName.CHANGE_PW_SCREEN}/$userId"
                )
            } else {
                showDialogMatchNo.value = true
            }
        }
    }



    fun signInWithCredential(credential: PhoneAuthCredential, context: Context) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                } else {

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
                Toast.makeText(context, "${e.message}", Toast.LENGTH_SHORT).show()
            }

            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                this@FindPwViewModel.verificationId = verificationId
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

    // 인증 확인 버튼을 눌렀을 때
    fun buttonCheckAuthOnClick(context: Context) {
        val code = textFieldFindPwAuthNumberValue.value // 사용자가 입력한 인증번호

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
                    Toast.makeText(context, "인증이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                    updateDoneButtonState()
                } else {
                    // 인증 실패
                    textFieldFindPwAuthNumberValue.value = ""
                    isCheckAuth.value = false

                    textFieldUFindPwAuthNumberError.value = true
                    textFieldUFindPwAuthNumberErrorText.value = "인증번호가 올바르지 않습니다. 다시 입력해주세요."

                    updateDoneButtonState()
                }
            }
    }


}