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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.hilt.navigation.compose.hiltViewModel
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

@Composable
fun LoginScreen(
    windowInsetsController:WindowInsetsControllerCompat,
    loginViewModel: LoginViewModel = hiltViewModel()) {

    LaunchedEffect(windowInsetsController) {
        windowInsetsController?.show(WindowInsetsCompat.Type.systemBars())
    }
    // 화면이 처음 생성될 때 (최초 1회만 실행)
    LaunchedEffect(Unit) {
        windowInsetsController?.show(WindowInsetsCompat.Type.systemBars())
    }

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
                    style = Typography.bodyLarge,
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
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 로고 이미지
            LikeLionImage(
                painter = painterResource(id = R.drawable.carryonlogo),
                modifier = Modifier
                    .size(150.dp)
                    .padding(vertical = 5.dp),
                contentScale = ContentScale.Fit,
                isCircular = false,
            )

            // 홍보 문구
            Text(
                text = "지금 CarryOn에서\n일정을 공유해보세요!",
                style = Typography.headlineMedium,
                color = SubColor,
                modifier = Modifier
                    .padding(
                        top = 5.dp,
                        bottom = 5.dp
                    )
            )

            // 아이디 텍스트 필드
            LikeLionOutlinedTextField(
                textFieldValue = loginViewModel.textFieldLoginIdValue,
                label = "아이디",
                placeHolder = "아이디",
                modifier = Modifier
                    .fillMaxWidth(),
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
                supportText = loginViewModel.textFieldLoginIdErrorText
            )

            // 비밀번호 텍스트 필드
            LikeLionOutlinedTextField(
                paddingTop = 10.dp,
                textFieldValue = loginViewModel.textFieldLoginPwValue,
                label = "비밀번호",
                placeHolder = "비밀번호",
                modifier = Modifier
                    .fillMaxWidth(),
                inputCondition = "[^a-zA-Z0-9_]",
                inputType = LikeLionOutlinedTextFieldInputType.PASSWORD,
                trailingIconMode = LikeLionOutlinedTextFieldEndIconMode.PASSWORD,
                singleLine = true,
                onValueChange = {
                    loginViewModel.updateButtonState()
                },
                isError = loginViewModel.textFieldLoginPwError,
                supportText = loginViewModel.textFieldLoginPwErrorText
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 0.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 자동 로그인 체크박스
                LikeLionCheckBox(
                    text = "자동 로그인",
                    checkedValue = loginViewModel.isAutoLoginEnabled,
                    checkedColor = MainColor,
                    uncheckedColor = Color.LightGray,
                    modifier = Modifier,
                    textModifier = Modifier
                        .padding(start = 5.dp),
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
                        bottom = 20.dp)
            ) {
                Text(
                    text = "회원가입",
                    color = SubTextColor,
                    style = Typography.bodyLarge,
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .clickable {
                            loginViewModel.buttonUserJoinClick()
                        },
                )

                Text(
                    text = "|",
                    color = SubTextColor,
                    style = Typography.bodyLarge,
                )

                Text(
                    text = "아이디 찾기",
                    color = SubTextColor,
                    style = Typography.bodyLarge,
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
                    style = Typography.bodyLarge,
                )

                Text(
                    text = "비밀번호 찾기",
                    color = SubTextColor,
                    style = Typography.bodyLarge,
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .clickable {
                            loginViewModel.buttonFindPwClick()
                        },
                )
            }

            LikeLionDivider(
                paddingTop = 10.dp,
                modifier = Modifier
                    .fillMaxWidth()

            )

            Text(
                text = "소셜 로그인",
                color = Color.Black,
                modifier = Modifier
                    .padding(top = 20.dp),
                style = Typography.bodyLarge,
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
                        // TODO : 카카오 로그인 이벤트 처리
                        loginViewModel.loginWithKakao(activity)
                    },
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
    }
}