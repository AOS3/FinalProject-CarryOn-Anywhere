package com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.mypage

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lion.FinalProject_CarryOn_Anywhere.CarryOnApplication
import com.lion.FinalProject_CarryOn_Anywhere.data.server.repository.UserRepository
import com.lion.FinalProject_CarryOn_Anywhere.data.server.service.UserService
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.ScreenName
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditPwViewModel @Inject constructor(
    @ApplicationContext context: Context
) : ViewModel() {

    val carryOnApplication = context as CarryOnApplication

    // 입력값
    // 현재 비밀번호
    val textFieldCurrentPw =  mutableStateOf("")
    // 새 비밀번호
    val textFieldNewPw =  mutableStateOf("")
    // 새 비밀번호 확인
    val textFieldNewPw2 =  mutableStateOf("")

    // 다이얼로그 제어 변수
    var showDialogPwOk = mutableStateOf(false)


    // 현재 비밀번호
    var currentPw =  mutableStateOf("")



    // ✅ 뒤로 가기 버튼
    fun navigationIconOnClick() {
        carryOnApplication.navHostController.popBackStack(
            ScreenName.EDIT_PW.name,
            inclusive = true
        )
    }


    // ✅ 비밀번호 변경 성공 시 화면 이동
    fun navigationConfirmButtonClick() {
        showDialogPwOk.value = false
        carryOnApplication.navHostController.popBackStack() // 다시 계정 설정 화면으로 이동
    }

    fun buttonChangePwDoneOnClick() {
        viewModelScope.launch {
            if (validatePassword()) {
                UserService.updateUserPwData(
                    carryOnApplication.loginUserModel,
                    textFieldNewPw.value.toString()
                )
                showDialogPwOk.value = true // 비밀번호 변경 성공 다이얼로그 표시
            }
        }
    }


    // ✅ 유효성 검사 다이얼로그 상태
    val showDialogPwEmpty = mutableStateOf(false) // 현재 비밀번호 미입력
    val showDialogPw1Mismatch = mutableStateOf(false) // 현재 비밀번호 비밀번호 불일치
    val showDialogPwShort = mutableStateOf(false) // 새 비밀번호 10자 미만
    val showDialogPw2Mismatch = mutableStateOf(false) // 새 비밀번호 불일치

    // ✅ 비밀번호 유효성 검사 메서드
    suspend fun validatePassword(): Boolean {

        return when {
            textFieldCurrentPw.value.isBlank() -> {
                showDialogPwEmpty.value = true // 현재 비밀번호가 비어 있으면 다이얼로그 표시
                false
            }
            textFieldCurrentPw.value != selectUserPasswordByUserId() -> {
                showDialogPw1Mismatch.value = true // 현재 비밀번호와 일치하지 않으면 다이얼로그 표시
                false
            }
            textFieldNewPw.value.length < 10 -> {
                showDialogPwShort.value = true // 새 비밀번호가 10자 미만이면 다이얼로그 표시
                false
            }
            textFieldNewPw.value != textFieldNewPw2.value -> {
                showDialogPw2Mismatch.value = true // 새 비밀번호 확인이 일치하지 않으면 다이얼로그 표시
                false
            }
            else -> true
        }
    }

    // 현재 비밀번호 가져오기
    suspend fun selectUserPasswordByUserId(): String? {
        return UserService.selectUserPasswordByUserId(carryOnApplication.loginUserModel.userId)
    }

}