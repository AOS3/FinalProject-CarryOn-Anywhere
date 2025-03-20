package com.lion.FinalProject_CarryOn_Anywhere.ui.screen.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionAlertDialog
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionFilledButton
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionOutlinedTextField
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionOutlinedTextFieldEndIconMode
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionOutlinedTextFieldInputType
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionTopAppBar
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.MainColor
import com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.login.ChangePwViewModel

@Composable
fun ChangePwScreen(
    userId:String,
    changePwViewModel: ChangePwViewModel = hiltViewModel()
) {

    // userId를 사용하여 UserModel을 가져오는 로직 추가
    LaunchedEffect(userId) {
        changePwViewModel.gettingUserModel(userId)
    }

    Scaffold(
        topBar = {
            LikeLionTopAppBar(
                backColor = Color.Transparent,
                title = "비밀번호 변경",
                navigationIconImage = Icons.AutoMirrored.Filled.ArrowBack,
                navigationIconOnClick = {
                    changePwViewModel.navigationIconOnClick()
                },
            )
        },

        ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(it)
                .imePadding()
                .padding(horizontal = 15.dp)
                .verticalScroll(state = rememberScrollState()),
            verticalArrangement = Arrangement.Top,
        ) {
            // 새 비밀번호 텍스트 필드
            LikeLionOutlinedTextField(
                paddingTop = 10.dp,
                textFieldValue = changePwViewModel.textFieldChangePwPwValue,
                onValueChange = {
                    changePwViewModel.updateDoneButtonState()
                },
                label = "새 비밀번호",
                modifier = Modifier
                    .fillMaxWidth(),
                inputCondition = "[^a-zA-Z0-9_]",
                inputType = LikeLionOutlinedTextFieldInputType.PASSWORD,
                trailingIconMode = LikeLionOutlinedTextFieldEndIconMode.PASSWORD,
                singleLine = true,
                isError = changePwViewModel.textFieldChangePwConditionError,
                supportText = changePwViewModel.textFieldChangePwConditionErrorText
            )

            if (!changePwViewModel.textFieldChangePwConditionError.value) {
                Row(modifier = Modifier.padding(bottom = 10.dp)) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Check",
                        modifier = Modifier
                            .size(20.dp)
                            .padding(end = 4.dp)
                    )
                    Text(
                        text = "영문 숫자 포함 8자 이상",
                        fontSize = 14.sp
                    )
                }
            }

            // 새 비밀번호 확인 텍스트 필드
            LikeLionOutlinedTextField(
                paddingTop = 10.dp,
                textFieldValue = changePwViewModel.textFieldChangePwCheckPwValue,
                onValueChange = {
                    changePwViewModel.updateDoneButtonState()
                },
                label = "새 비밀번호 확인",
                modifier = Modifier
                    .fillMaxWidth(),
                inputCondition = "[^a-zA-Z0-9_]",
                inputType = LikeLionOutlinedTextFieldInputType.PASSWORD,
                trailingIconMode = LikeLionOutlinedTextFieldEndIconMode.PASSWORD,
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done // "완료" 버튼 활성화
                     ),
                isError = changePwViewModel.textFieldChangePwMismatchError,
                supportText = changePwViewModel.textFieldChangePwMismatchErrorText

            )

            // 비밀번호 변경 버튼
            LikeLionFilledButton(
                text = "비밀번호 변경",
                isEnabled = changePwViewModel.isButtonChangePwDoneEnabled.value,
                modifier = Modifier
                    .fillMaxWidth(),
                paddingTop = 20.dp,
                onClick = {
                    if (changePwViewModel.validatePassword()) {
                        changePwViewModel.buttonChangePwDoneOnClick()
                    }
                },
                cornerRadius = 5,
                containerColor = MainColor,
                buttonHeight = 60.dp,
            )
        }

        // 비밀번호 변경 성공 다이얼로그
        LikeLionAlertDialog(
            showDialogState = changePwViewModel.showDialogPwOk,
            confirmButtonTitle = "확인",
            confirmButtonOnClick = {
                changePwViewModel.navigationConfirmButtonClick()
            },
            title = "비밀번호 변경",
            titleAlign = TextAlign.Center,
            text = "비밀번호 변경에 성공하셨습니다.",
            textAlign = TextAlign.Center,
            titleModifier = Modifier
                .fillMaxWidth(),
            textModifier = Modifier
                .fillMaxWidth(),
            containerColor = Color.White
        )

//        // 유효성 검사 다이얼로그
//        if (changePwViewModel.showDialogPwShort.value) {
//            LikeLionAlertDialog(
//                showDialogState = changePwViewModel.showDialogPwShort,
//                title = "입력 오류",
//                text = "새 비밀번호는 8자 이상 입력해야 합니다.",
//                confirmButtonTitle = "확인"
//            )
//        }
//
//        if (changePwViewModel.showDialogPwMismatch.value) {
//            LikeLionAlertDialog(
//                showDialogState = changePwViewModel.showDialogPwMismatch,
//                title = "입력 오류",
//                text = "새 비밀번호가 일치하지 않습니다.",
//                confirmButtonTitle = "확인"
//            )
//        }
    }
}