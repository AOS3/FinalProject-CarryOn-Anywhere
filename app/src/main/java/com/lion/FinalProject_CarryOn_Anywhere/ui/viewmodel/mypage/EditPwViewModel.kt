package com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.mypage

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.lion.FinalProject_CarryOn_Anywhere.CarryOnApplication
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.ScreenName
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class EditPwViewModel @Inject constructor(
    @ApplicationContext context: Context
) : ViewModel() {

    val carryOnApplication = context as CarryOnApplication

    // 현재 비밀번호
    val textFieldCurrentPw =  mutableStateOf("")

    // 새 비밀번호
    val textFieldNewPw =  mutableStateOf("")

    // 새 비밀번호 확인
    val textFieldNewPw2 =  mutableStateOf("")

    // 다이얼로그 제어 변수
    val showDialogPwOk = mutableStateOf(false)

    // ✅ 뒤로 가기 버튼
    fun navigationIconOnClick() {
        carryOnApplication.navHostController.popBackStack(
            ScreenName.EDIT_PW.name,
            inclusive = true
        )
    }


    // ✅ 비밀번호 변경 성공 시 네비게이션
    fun navigationConfirmButtonClick() {
        showDialogPwOk.value = false
        carryOnApplication.navHostController.popBackStack() // 다시 계정 설정 화면으로 이동
    }

    // ✅ 완료 버튼 클릭 시 유효성 검사 후 진행
    fun buttonChangePwDoneOnClick() {
        if (validatePassword()) {
            showDialogPwOk.value = true // 비밀번호 변경 성공 다이얼로그 표시
        }
    }


    // ✅ 유효성 검사 다이얼로그 상태
    val showDialogPwEmpty = mutableStateOf(false) // 현재 비밀번호 미입력
    val showDialogPwShort = mutableStateOf(false) // 새 비밀번호 10자 미만
    val showDialogPwMismatch = mutableStateOf(false) // 새 비밀번호 불일치

    // ✅ 비밀번호 유효성 검사 메서드
    fun validatePassword(): Boolean {
        return when {
            textFieldCurrentPw.value.isBlank() -> {
                showDialogPwEmpty.value = true // 현재 비밀번호가 비어 있으면 다이얼로그 표시
                false
            }
            textFieldNewPw.value.length < 10 -> {
                showDialogPwShort.value = true // 새 비밀번호가 10자 미만이면 다이얼로그 표시
                false
            }
            textFieldNewPw.value != textFieldNewPw2.value -> {
                showDialogPwMismatch.value = true // 새 비밀번호 확인이 일치하지 않으면 다이얼로그 표시
                false
            }
            else -> true
        }
    }

}