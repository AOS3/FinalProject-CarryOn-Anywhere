package com.lion.FinalProject_CarryOn_Anywhere.ui.screen.login

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.lion.FinalProject_CarryOn_Anywhere.R
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionAlertDialog
import com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.login.LoginViewModel
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionCheckBox
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionDivider
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionFilledButton
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionImage
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionOutlinedTextField
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionOutlinedTextFieldEndIconMode
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionOutlinedTextFieldInputType
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionTopAppBar
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.MainColor
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.SubColor
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.SubTextColor
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.Typography
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.nanumSquareExtraBold

@Composable
fun LoginScreen(
    windowInsetsController:WindowInsetsControllerCompat,
    loginViewModel: LoginViewModel = hiltViewModel()) {

    val context = LocalContext.current

    // 키보드 포커스 상태
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    val isLoading by loginViewModel.isLoading.collectAsState()

    LaunchedEffect(windowInsetsController) {
        windowInsetsController?.show(WindowInsetsCompat.Type.systemBars())
    }
    // 화면이 처음 생성될 때 (최초 1회만 실행)
//    LaunchedEffect(Unit) {
//        windowInsetsController?.show(WindowInsetsCompat.Type.systemBars())
//    }

    val activity = LocalContext.current as Activity

    Scaffold(

        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 16.dp,
                        vertical = 8.dp
                    ),
            ) {
                LikeLionTopAppBar(
                    backColor = Color.Transparent,
                )

                Text(
                    text = "둘러보기",
                    color = SubTextColor,
                    style = Typography.bodySmall,
                    fontSize = 13.sp,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .align(Alignment.CenterEnd)
                        .clickable {
                            loginViewModel.buttonAroundClick()
                        },
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(it)
                .padding(horizontal = 20.dp)
                .imePadding(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Spacer(modifier = Modifier.height(50.dp))

            // 로고 이미지
            LikeLionImage(
                painter = painterResource(id = R.drawable.carryonslogan),
                modifier = Modifier
                    .width(300.dp),
                contentScale = ContentScale.Fit,
                isCircular = false,
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 홍보 문구
            Text(
                text = buildAnnotatedString {
                    append("✈️ 지금 ")
                    withStyle(style = SpanStyle(fontFamily = nanumSquareExtraBold)) {
                        append("CarryOn")
                    }
                    append("에서 ")
                    withStyle(style = SpanStyle(fontFamily = nanumSquareExtraBold)) {
                        append("일정을 공유")
                    }
                    append("해보세요!")
                },
                //style = Typography.headlineMedium,
                color = SubColor,
                modifier = Modifier
                    .padding(top = 20.dp),
            )


            // 아이디 텍스트 필드
            LikeLionOutlinedTextField(
                textFieldValue = loginViewModel.textFieldLoginIdValue,
                label = "아이디",
                placeHolder = "아이디",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                paddingTop = 10.dp,
                inputCondition = "[^a-zA-Z0-9_]",
                trailingIconMode = LikeLionOutlinedTextFieldEndIconMode.TEXT,
                onTrailingIconClick = {
                    loginViewModel.isButtonEnabled
                },
                singleLine = true,
                onValueChange = {
                    loginViewModel.isButtonEnabled
                },
                isError = loginViewModel.textFieldLoginIdError,
                supportText = loginViewModel.textFieldLoginIdErrorText,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions.Default
            )

            // 비밀번호 텍스트 필드
            LikeLionOutlinedTextField(
                paddingTop = 10.dp,
                textFieldValue = loginViewModel.textFieldLoginPwValue,
                label = "비밀번호",
                placeHolder = "비밀번호",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                inputCondition = "[^a-zA-Z0-9_]",
                inputType = LikeLionOutlinedTextFieldInputType.PASSWORD,
                trailingIconMode = LikeLionOutlinedTextFieldEndIconMode.PASSWORD,
                singleLine = true,
                onValueChange = {
                    loginViewModel.updateButtonState()
                },
                isError = loginViewModel.textFieldLoginPwError,
                supportText = loginViewModel.textFieldLoginPwErrorText,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                ),
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 자동 로그인 체크박스
                LikeLionCheckBox(
                    text = "자동 로그인",
                    checkedValue = loginViewModel.isAutoLoginEnabled,
                    checkedColor = MainColor,
                    uncheckedColor = Color.LightGray,
                    modifier = Modifier,
                    textModifier = Modifier,
                )
            }

            LikeLionFilledButton(
                text = "로그인",
                //isEnabled = loginViewModel.isButtonEnabled.value,
                modifier = Modifier
                    .fillMaxWidth(),
                paddingTop = 10.dp,
                onClick = {
                    loginViewModel.buttonLoginClick()
                },
                cornerRadius = 5,
                containerColor = MainColor,
                buttonHeight = 60.dp,
            )

            // 회원가입, 아이디, 비밀번호 찾기
            Row(
                modifier = Modifier
                    .padding(
                        top = 30.dp,
                        bottom = 10.dp)
            ) {
                Text(
                    text = "회원가입",
                    color = SubTextColor,
                    style = Typography.bodySmall,
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .clickable {
                            loginViewModel.buttonUserJoinClick()
                        },
                )

                Text(
                    text = "|",
                    color = SubTextColor,
                    style = Typography.bodySmall,
                )

                Text(
                    text = "아이디 찾기",
                    color = SubTextColor,
                    style = Typography.bodySmall,
                    modifier = Modifier
                        .padding(
                            start = 10.dp,
                            end = 10.dp
                        )
                        .clickable {
                            loginViewModel.buttonFindIdClick()
                        },
                )

                Text(
                    text = "|",
                    color = SubTextColor,
                    style = Typography.bodySmall,
                )

                Text(
                    text = "비밀번호 찾기",
                    color = SubTextColor,
                    style = Typography.bodySmall,
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .clickable {
                            loginViewModel.buttonFindPwClick()
                        },
                )
            }

            LikeLionDivider(
                paddingTop = 5.dp,
                modifier = Modifier
                    .fillMaxWidth()

            )

            Text(
                text = "소셜 로그인",
                color = Color.Black,
                modifier = Modifier
                    .padding(top = 20.dp),
                style = Typography.bodySmall,
            )

            Spacer(
                modifier = Modifier
                    .height(10.dp)
            )

            // 카카오 로그인 버튼
            LikeLionImage(
                painter = painterResource(id = R.drawable.kakao_login_large_wide),
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clickable {
                        loginViewModel.loginWithKakao(activity)
                    }
                    .padding(horizontal = 10.dp),
            )

            // ------------------------- Dialog --------------------
            // Dialog - 탈퇴한 회원
            LikeLionAlertDialog(
                showDialogState = loginViewModel.alertDialogLoginSignOutError,
                title = "로그인 실패",
                text = "탈퇴한 회원입니다.\n문의해주세요.",
                confirmButtonTitle = "확인",
                confirmButtonOnClick = {
                    loginViewModel.alertDialogLoginSignOutError.value = false
                },
                titleAlign = TextAlign.Center, // 제목 중앙 정렬
                textAlign = TextAlign.Center, // 본문 텍스트 중앙 정렬
                titleModifier = Modifier.fillMaxWidth(), // 제목 가로 중앙 정렬
                textModifier = Modifier.fillMaxWidth(), // 본문 가로 중앙 정렬
                confirmButtonModifier = Modifier.width(140.dp),
                dismissButtonModifier = Modifier.width(140.dp),
            )
        }

        // 로딩 UI (isLoading이 true일 때만 표시)
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(SubColor.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = MainColor,
                    strokeWidth = 8.dp,
                    modifier = Modifier
                        .size(100.dp),
                )
            }
        }
    }
}