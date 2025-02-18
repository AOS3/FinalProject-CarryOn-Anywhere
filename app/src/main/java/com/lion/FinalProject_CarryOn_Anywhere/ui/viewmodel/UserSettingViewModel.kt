package com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel
import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.lion.FinalProject_CarryOn_Anywhere.CarryOnApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext

import javax.inject.Inject

data class UserModel(
    var textFieldModifyIdValue: String
)


@HiltViewModel
class UserSettingViewModel @Inject constructor(
    @ApplicationContext context: Context,
   // val customerService: CustomerService
) : ViewModel() {

    val textFieldModifyIdValue = mutableStateOf("")
    val textFieldModifyNameValue = mutableStateOf("")
    val textFieldModifyPhoneValue = mutableStateOf("")

    val selectedPushAgree = mutableStateOf("")

    // 탈퇴하기 이동

    // 비밀번호 변경 이동
    // 비밀번호 변경을 눌렀을때
    fun modifyPwOnClick() {
      //  CarryOnApplication.navHostController.navigate("modifyUserPw")
    }


}