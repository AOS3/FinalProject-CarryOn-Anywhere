package com.lion.FinalProject_CarryOn_Anywhere.ui.screen.login

import android.app.Activity
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
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionDivider
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionFilledButton
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionOutlinedTextField
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionOutlinedTextFieldEndIconMode
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionOutlinedTextFieldInputType
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionTopAppBar
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.MainColor
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.SubColor
import com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.login.UserJoinViewModel

@Composable
fun UserJoinScreen(userJoinViewModel: UserJoinViewModel = hiltViewModel()) {

    val context = LocalContext.current
    val activity = context as? Activity ?: return

    Scaffold(
        topBar = {
            LikeLionTopAppBar(
                backColor = Color.Transparent,
                title = "회원가입",
                navigationIconImage = Icons.AutoMirrored.Filled.ArrowBack,
                navigationIconOnClick = {
                    userJoinViewModel.navigationIconOnClick()
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
                // 가입 완료 버튼
                LikeLionFilledButton(
                    text = "가입 완료",
                    //isEnabled = userJoinViewModel.isButtonJoinEnabled.value,
                    isEnabled = true,
                    modifier = Modifier
                        .fillMaxWidth(),
                    paddingTop = 10.dp,
                    paddingBottom = 1.dp,
                    onClick = {
                        userJoinViewModel.buttonUserJoinSubmitOnClick()
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
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                // 아이디 텍스트 필드
                LikeLionOutlinedTextField(
                    textFieldValue = userJoinViewModel.textFieldUserJoinIdValue,
                    label = "아이디",
                    placeHolder = "아이디",
                    modifier = Modifier
                        .weight(1f),
                    paddingTop = 10.dp,
                    inputCondition = "[^a-zA-Z0-9_]",
                    trailingIconMode = LikeLionOutlinedTextFieldEndIconMode.TEXT,
                    onTrailingIconClick = {
                        userJoinViewModel.updateCheckIdButtonState()
                    },
                    singleLine = true,
                    onValueChange = {
                        userJoinViewModel.updateCheckIdButtonState()
                    },
                )

                // 아이디 중복확인 버튼
                LikeLionFilledButton(
                    text = "중복확인",
                    isEnabled = userJoinViewModel.isButtonUserJoinIdEnabled.value,
                    modifier = Modifier
                        .weight(0.4f)
                        .padding(top = 6.dp, start = 5.dp)
                        .height(56.dp),
                    onClick = {
                        userJoinViewModel.checkUserId()
                    },
                    cornerRadius = 5,
                    containerColor = SubColor,
                    buttonHeight = 60.dp,
                )
            }

            // 비밀번호 텍스트 필드
            LikeLionOutlinedTextField(
                paddingTop = 10.dp,
                textFieldValue = userJoinViewModel.textFieldUserJoinPwValue,
                label = "비밀번호",
                placeHolder = "비밀번호",
                modifier = Modifier
                    .fillMaxWidth(),
                inputCondition = "[^a-zA-Z0-9_]",
                inputType = LikeLionOutlinedTextFieldInputType.PASSWORD,
                trailingIconMode = LikeLionOutlinedTextFieldEndIconMode.PASSWORD,
                singleLine = true,
                onValueChange = {
                    userJoinViewModel.updateUserJoinButtonState()
                },
            )

            // 비밀번호 확인 텍스트 필드
            LikeLionOutlinedTextField(
                paddingTop = 10.dp,
                textFieldValue = userJoinViewModel.textFieldUserJoinCheckPwValue,
                label = "비밀번호 확인",
                placeHolder = "비밀번호 확인",
                modifier = Modifier
                    .fillMaxWidth(),
                inputCondition = "[^a-zA-Z0-9_]",
                inputType = LikeLionOutlinedTextFieldInputType.PASSWORD,
                trailingIconMode = LikeLionOutlinedTextFieldEndIconMode.PASSWORD,
                singleLine = true,
                onValueChange = {
                    userJoinViewModel.updateUserJoinButtonState()
                },
            )

            LikeLionDivider(
                paddingTop = 10.dp,
                modifier = Modifier
                    .fillMaxWidth()

            )

            // 이름 텍스트 필드
            LikeLionOutlinedTextField(
                textFieldValue = userJoinViewModel.textFieldUserJoinNameValue,
                label = "이름",
                placeHolder = "이름",
                modifier = Modifier,
                paddingTop = 10.dp,
                inputCondition = "[^ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z]",
                trailingIconMode = LikeLionOutlinedTextFieldEndIconMode.TEXT,
                onTrailingIconClick = {
                    userJoinViewModel.updateUserJoinButtonState()
                },
                singleLine = true,
                onValueChange = {
                    userJoinViewModel.updateCheckIdButtonState()
                },
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                // 휴대폰 번호 텍스트 필드
                LikeLionOutlinedTextField(
                    textFieldValue = userJoinViewModel.textFieldUserJoinPhoneValue,
                    label = "핸드폰 번호",
                    placeHolder = "하이픈(-) 없이 숫자만 입력",
                    modifier = Modifier
                        .weight(1f),
                    paddingTop = 10.dp,
                    inputCondition = "[^0-9]",
                    trailingIconMode = LikeLionOutlinedTextFieldEndIconMode.TEXT,
                    inputType = LikeLionOutlinedTextFieldInputType.NUMBER,
                    onTrailingIconClick = {
                        userJoinViewModel.updateSendAutoButtonState()
                    },
                    singleLine = true,
                    onValueChange = {
                        userJoinViewModel.updateSendAutoButtonState()
                    },
                )

                // 인증 요청 버튼
                LikeLionFilledButton(
                    text = "인증요청",
                    isEnabled = userJoinViewModel.isButtonUserJoinPhoneNoEnabled.value,
                    modifier = Modifier
                        .weight(0.4f)
                        .padding(top = 6.dp, start = 5.dp)
                        .height(56.dp),
                    onClick = {
                        userJoinViewModel.sendVerificationCode(userJoinViewModel.textFieldUserJoinPhoneValue.value, activity)
                    },
                    cornerRadius = 5,
                    containerColor = SubColor,
                    buttonHeight = 60.dp,
                )
            }

            // 인증 번호 텍스트 필드
            LikeLionOutlinedTextField(
                textFieldValue = userJoinViewModel.textFieldUserJoinAuthNumberValue,
                label = "인증번호",
                placeHolder = "인증번호",
                modifier = Modifier,
                paddingTop = 10.dp,
                inputCondition = "[^0-9]",
                trailingIconMode = LikeLionOutlinedTextFieldEndIconMode.TEXT,
                inputType = LikeLionOutlinedTextFieldInputType.NUMBER,
                onTrailingIconClick = {
                    userJoinViewModel.updateCheckIdButtonState()
                },
                singleLine = true,
                onValueChange = {
                    userJoinViewModel.updateCheckIdButtonState()
                },
            )

            // 인증 번호 확인 버튼
            LikeLionFilledButton(
                text = "인증 확인",
                isEnabled = userJoinViewModel.isButtonUserJoinAuthNoEnabled.value,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                paddingTop = 10.dp,
                onClick = {
                    userJoinViewModel.buttonCheckAuthOnClick(context)
                },
                cornerRadius = 5,
                containerColor = SubColor,
                buttonHeight = 60.dp,
            )

            // ---------------------------- Dialog ------------------------

            // Dialog - 사용할 수 있는 아이디인 경우
            LikeLionAlertDialog(
                showDialogState = userJoinViewModel.showDialogIdOk,
                title = "중복 확인",
                text = "사용할 수 있는 아이디 입니다.",
                confirmButtonTitle = "뒤로 가기",
                confirmButtonOnClick = {
                    userJoinViewModel.showDialogIdOk.value = false
                },
                titleAlign = TextAlign.Center, // 제목 중앙 정렬
                textAlign = TextAlign.Center, // 본문 텍스트 중앙 정렬
                titleModifier = Modifier.fillMaxWidth(), // 제목 가로 중앙 정렬
                textModifier = Modifier.fillMaxWidth(), // 본문 가로 중앙 정렬
                confirmButtonModifier = Modifier.width(140.dp),
                dismissButtonModifier = Modifier.width(140.dp)
            )

            // Dialog - 사용할 수 없는 아이디인 경우
            LikeLionAlertDialog(
                showDialogState = userJoinViewModel.showDialogIdOk,
                title = "중복 확인",
                text = "사용할 수 없는 아이디 입니다.",
                confirmButtonTitle = "확인",
                confirmButtonOnClick = {
                    userJoinViewModel.showDialogIdOk.value = false
                },
                titleAlign = TextAlign.Center, // 제목 중앙 정렬
                textAlign = TextAlign.Center, // 본문 텍스트 중앙 정렬
                titleModifier = Modifier.fillMaxWidth(), // 제목 가로 중앙 정렬
                textModifier = Modifier.fillMaxWidth(), // 본문 가로 중앙 정렬
                confirmButtonModifier = Modifier.width(140.dp),
                dismissButtonModifier = Modifier.width(140.dp)
            )

            // Dialog - 잘못된 인증 번호일 경우
            LikeLionAlertDialog(
                showDialogState = userJoinViewModel.showDialogAuthNo,
                title = "인증 실패",
                text = "인증 번호가 올바르지 않습니다. \n다시 확인해주세요.",
                confirmButtonTitle = "확인",
                confirmButtonOnClick = {
                    userJoinViewModel.showDialogAuthNo.value = false
                },
                titleAlign = TextAlign.Center, // 제목 중앙 정렬
                textAlign = TextAlign.Center, // 본문 텍스트 중앙 정렬
                titleModifier = Modifier.fillMaxWidth(), // 제목 가로 중앙 정렬
                textModifier = Modifier.fillMaxWidth(), // 본문 가로 중앙 정렬
                confirmButtonModifier = Modifier.width(140.dp),
                dismissButtonModifier = Modifier.width(140.dp)
            )

            // Dialog - 가입 완료
            LikeLionAlertDialog(
                showDialogState = userJoinViewModel.showDialogJoinOk,
                title = "가입 완료",
                text = "가입이 완료되었습니다.\n로그인을 진행해주세요 ",
                confirmButtonTitle = "확인",
                confirmButtonOnClick = {
                    userJoinViewModel.moveToLoginScreen()
                },
                titleAlign = TextAlign.Center, // 제목 중앙 정렬
                textAlign = TextAlign.Center, // 본문 텍스트 중앙 정렬
                titleModifier = Modifier.fillMaxWidth(), // 제목 가로 중앙 정렬
                textModifier = Modifier.fillMaxWidth(), // 본문 가로 중앙 정렬
                confirmButtonModifier = Modifier.width(140.dp),
                dismissButtonModifier = Modifier.width(140.dp)
            )

            LikeLionAlertDialog(
                showDialogState = userJoinViewModel.showDialogAuthOk,
                title = "인증 성공",
                text = "인증이 완료되었습니다.",
                confirmButtonTitle = "확인",
                confirmButtonOnClick = {
                    userJoinViewModel.showDialogAuthOk.value = false
                },
                titleAlign = TextAlign.Center, // 제목 중앙 정렬
                textAlign = TextAlign.Center, // 본문 텍스트 중앙 정렬
                titleModifier = Modifier.fillMaxWidth(), // 제목 가로 중앙 정렬
                textModifier = Modifier.fillMaxWidth(), // 본문 가로 중앙 정렬
                confirmButtonModifier = Modifier.width(140.dp),
                dismissButtonModifier = Modifier.width(140.dp)
            )
        }
    }
}