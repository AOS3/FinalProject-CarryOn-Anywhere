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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionFilledButton
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionOutlinedTextField
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionOutlinedTextFieldEndIconMode
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionOutlinedTextFieldInputType
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionTopAppBar
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.MainColor
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.SubColor
import com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.login.FindIdViewModel

@Composable
fun FindIdScreen(findIdViewModel: FindIdViewModel = hiltViewModel()) {

    Scaffold(
        topBar = {
            LikeLionTopAppBar(
                backColor = Color.Transparent,
                title = "아이디 찾기",
                navigationIconImage = Icons.AutoMirrored.Filled.ArrowBack,
                navigationIconOnClick = {
                    findIdViewModel.navigationIconOnClick()
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
                    text = "완료",
                    //isEnabled = userJoinViewModel.isButtonJoinEnabled.value,
                    isEnabled = true,
                    modifier = Modifier
                        .fillMaxWidth(),
                    paddingTop = 10.dp,
                    paddingBottom = 1.dp,
                    onClick = {
                        findIdViewModel.buttonDoneOnClick()
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
                textFieldValue = findIdViewModel.textFieldFindIdNameValue,
                label = "이름",
                placeHolder = "이름",
                modifier = Modifier,
                paddingTop = 10.dp,
                inputCondition = "[^a-zA-Z0-9_]",
                trailingIconMode = LikeLionOutlinedTextFieldEndIconMode.TEXT,
                singleLine = true,
                onValueChange = {

                },
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                // 휴대폰 번호 텍스트 필드
                LikeLionOutlinedTextField(
                    textFieldValue = findIdViewModel.textFieldFindIdPhoneValue,
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
                        findIdViewModel.updateSendAutoButtonState()
                    },
                )

                // 인증 요청 버튼
                LikeLionFilledButton(
                    text = "인증요청",
                    isEnabled = findIdViewModel.isButtonFindIdPhoneNoEnabled.value,
                    modifier = Modifier
                        .weight(0.4f)
                        .padding(top = 6.dp, start = 5.dp)
                        .height(56.dp),
                    onClick = {

                    },
                    cornerRadius = 5,
                    containerColor = SubColor,
                    buttonHeight = 60.dp,
                )
            }

            // 인증 번호 텍스트 필드
            LikeLionOutlinedTextField(
                textFieldValue = findIdViewModel.textFieldFindIdAuthNumberValue,
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
                    findIdViewModel.updateSendAutoButtonState()
                },
            )

            // 인증 번호 확인 버튼
            LikeLionFilledButton(
                text = "인증 확인",
                isEnabled = findIdViewModel.isButtonFindIdAuthNoEnabled.value,
                modifier = Modifier
                    .fillMaxWidth(),
                paddingTop = 10.dp,
                onClick = {

                },
                cornerRadius = 5,
                containerColor = SubColor,
                buttonHeight = 60.dp,
            )

        }
    }
}