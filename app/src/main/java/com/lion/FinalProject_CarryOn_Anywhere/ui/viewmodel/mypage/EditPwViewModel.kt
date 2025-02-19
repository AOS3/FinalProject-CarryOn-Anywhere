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

    // Back 버튼 동작 메서드
    fun navigationIconOnClick() {
        carryOnApplication.navHostController.popBackStack(
            ScreenName.EDIT_PW.name,
            inclusive = true
        )

    }


    // 비밀번호 변경 다이얼로그 확인 버튼 눌렀을 때
    fun navigationConfirmButtonClick() {
        showDialogPwOk.value = false
        carryOnApplication.navHostController.popBackStack() // 다시 계정 설정 화면으로 이동
    }

    // 완료 버튼 눌렀을 때
    fun buttonChangePwDoneOnClick() {
        showDialogPwOk.value = true
    }



}