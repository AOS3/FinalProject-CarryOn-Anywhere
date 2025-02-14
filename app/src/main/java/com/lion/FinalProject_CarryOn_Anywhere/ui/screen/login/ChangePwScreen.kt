package com.lion.FinalProject_CarryOn_Anywhere.ui.screen.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
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
fun ChangePwScreen(changePwViewModel: ChangePwViewModel = hiltViewModel()) {

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
                label = "새 비밀번호",
                modifier = Modifier
                    .fillMaxWidth(),
                inputCondition = "[^a-zA-Z0-9_]",
                inputType = LikeLionOutlinedTextFieldInputType.PASSWORD,
                trailingIconMode = LikeLionOutlinedTextFieldEndIconMode.PASSWORD,
                singleLine = true,
            )

            // 새 비밀번호 확인 텍스트 필드
            LikeLionOutlinedTextField(
                paddingTop = 10.dp,
                textFieldValue = changePwViewModel.textFieldChangePwCheckPwValue,
                label = "새 비밀번호 확인",
                modifier = Modifier
                    .fillMaxWidth(),
                inputCondition = "[^a-zA-Z0-9_]",
                inputType = LikeLionOutlinedTextFieldInputType.PASSWORD,
                trailingIconMode = LikeLionOutlinedTextFieldEndIconMode.PASSWORD,
                singleLine = true,
                onValueChange = {

                },
            )

            // 가입 완료 버튼
            LikeLionFilledButton(
                text = "비밀번호 변경",
                isEnabled = true,
                modifier = Modifier
                    .fillMaxWidth(),
                paddingTop = 10.dp,
                onClick = {
                    changePwViewModel.buttonChangePwDoneOnClick()
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
                .fillMaxWidth()
        )
    }
}