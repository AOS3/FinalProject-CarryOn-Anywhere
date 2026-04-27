package com.lion.FinalProject_CarryOn_Anywhere.ui.screen.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.SubColor
import com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.login.FindPwViewModel

@Composable
fun FindPwScreen(findPwViewModel: FindPwViewModel = hiltViewModel()) {

    val context = LocalContext.current

    Scaffold(
        topBar = {
            LikeLionTopAppBar(
                backColor = Color.Transparent,
                title = "비밀번호 찾기",
                navigationIconImage = Icons.AutoMirrored.Filled.ArrowBack,
                navigationIconOnClick = {
                    findPwViewModel.navigationIconOnClick()
                },
            )
        },

        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent)
                    .padding(
                        horizontal = 15.dp,
                        vertical = 10.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 완료 버튼
                LikeLionFilledButton(
                    text = "확인",
                    isEnabled = findPwViewModel.isButtonFindPwDoneEnabled.value,
                    modifier = Modifier
                        .fillMaxWidth(),
                    paddingTop = 10.dp,
                    paddingBottom = 1.dp,
                    onClick = {
                        findPwViewModel.buttonDoneOnClick()
                    },
                    cornerRadius = 5,
                    containerColor = MainColor,
                    buttonHeight = 60.dp,
                )

                Spacer(modifier = Modifier.height(50.dp))
            }
        }
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
            // 아이디 텍스트 필드
            LikeLionOutlinedTextField(
                textFieldValue = findPwViewModel.textFieldFindPwIdValue,
                label = "아이디",
                placeHolder = "아이디",
                modifier = Modifier,
                paddingTop = 10.dp,
                inputCondition = "[^a-zA-Z0-9_]",
                trailingIconMode = LikeLionOutlinedTextFieldEndIconMode.TEXT,
                singleLine = true,
                onValueChange = {
                    findPwViewModel.textFieldFindPwIdValue.value = it
                    findPwViewModel.updateDoneButtonState()
                },
                isError = findPwViewModel.textFieldUFindPwNotExistUserError,
                supportText = findPwViewModel.textFieldUFindPwNotExistUserErrorText,
                enabled = !findPwViewModel.isInputDisabled.value
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                // 휴대폰 번호 텍스트 필드
                LikeLionOutlinedTextField(
                    textFieldValue = findPwViewModel.textFieldFindPwPhoneValue,
                    label = "핸드폰 번호",
                    placeHolder = "하이픈(-) 없이 숫자만 입력",
                    modifier = Modifier
                        .weight(1f),
                    paddingTop = 10.dp,
                    inputCondition = "[^0-9]",
                    trailingIconMode = LikeLionOutlinedTextFieldEndIconMode.TEXT,
                    inputType = LikeLionOutlinedTextFieldInputType.NUMBER,
                    singleLine = true,
                    onValueChange = {
                        findPwViewModel.textFieldFindPwPhoneValue.value = it
                        findPwViewModel.updateSendAutoButtonState()
                        findPwViewModel.updateDoneButtonState()
                    },
                    isError = findPwViewModel.textFieldUFindPwMismatchUserError,
                    supportText = findPwViewModel.textFieldUFindPwMismatchUserErrorText,
                    enabled = !findPwViewModel.isInputDisabled.value
                )

                // 인증 요청 버튼
                LikeLionFilledButton(
                    text = "인증요청",
                    isEnabled = findPwViewModel.isButtonFindPwPhoneNoEnabled.value && !findPwViewModel.isInputDisabled.value,
                    modifier = Modifier
                        .weight(0.4f)
                        .padding(top = 6.dp, start = 5.dp)
                        .height(56.dp),
                    onClick = {
                        findPwViewModel.buttonFindPwVerificationOnClick(context)
                    },
                    cornerRadius = 5,
                    containerColor = SubColor,
                    buttonHeight = 60.dp,
                )
            }

            // 인증 번호 텍스트 필드
            LikeLionOutlinedTextField(
                textFieldValue = findPwViewModel.textFieldFindPwAuthNumberValue,
                label = "인증번호",
                placeHolder = "인증번호",
                modifier = Modifier,
                paddingTop = 10.dp,
                inputCondition = "[^0-9]",
                trailingIconMode = LikeLionOutlinedTextFieldEndIconMode.TEXT,
                inputType = LikeLionOutlinedTextFieldInputType.NUMBER,
                onTrailingIconClick = {

                },
                singleLine = true,
                onValueChange = {
                    findPwViewModel.textFieldFindPwAuthNumberValue.value = it
                    findPwViewModel.updateCheckAuthButtonState()
                    findPwViewModel.updateDoneButtonState()
                },
                isError = findPwViewModel.textFieldUFindPwAuthNumberError,
                supportText = findPwViewModel.textFieldUFindPwAuthNumberErrorText,
                enabled = !findPwViewModel.isInputDisabled.value
            )

            // 인증 번호 확인 버튼
            LikeLionFilledButton(
                text = "인증 확인",
                isEnabled = findPwViewModel.isButtonFindPwAuthNoEnabled.value && !findPwViewModel.isInputDisabled.value,
                modifier = Modifier
                    .fillMaxWidth(),
                paddingTop = 10.dp,
                onClick = {
                    findPwViewModel.buttonCheckAuthOnClick(context)
                    findPwViewModel.updateDoneButtonState()
                },
                cornerRadius = 5,
                containerColor = SubColor,
                buttonHeight = 60.dp,
            )

            // Dialog - 존재하지 않는 정보
            LikeLionAlertDialog(
                showDialogState = findPwViewModel.showDialogMatchNo,
                title = "존재하지 않는 아이디",
                text = "존재하지 않는 정보입니다.\n다시 확인해주세요",
                confirmButtonTitle = "확인",
                confirmButtonOnClick = {
                    findPwViewModel.showDialogMatchNo.value = false
                },
                titleAlign = TextAlign.Center, // 제목 중앙 정렬
                textAlign = TextAlign.Center, // 본문 텍스트 중앙 정렬
                titleModifier = Modifier.fillMaxWidth(), // 제목 가로 중앙 정렬
                textModifier = Modifier.fillMaxWidth(), // 본문 가로 중앙 정렬
                confirmButtonModifier = Modifier.width(140.dp),
            )

        }
    }
}