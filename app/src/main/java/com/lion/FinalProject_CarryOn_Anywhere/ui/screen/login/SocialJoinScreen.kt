package com.lion.FinalProject_CarryOn_Anywhere.ui.screen.login

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowInsetsControllerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionAlertDialog
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionCheckBox
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionFilledButton
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionOutlinedTextField
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionOutlinedTextFieldEndIconMode
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionTopAppBar
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.MainColor
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.SubColor
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.Typography
import com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.login.SocialJoinViewModel

@Composable
fun SocialJoinScreen(
    windowInsetsController: WindowInsetsControllerCompat,
    socialJoinViewModel: SocialJoinViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val activity = context as? Activity ?: return

    Scaffold(
        topBar = {
            LikeLionTopAppBar(
                backColor = Color.Transparent,
                title = "아이디 입력",
                navigationIconImage = Icons.AutoMirrored.Filled.ArrowBack,
                navigationIconOnClick = {
                    socialJoinViewModel.navigationIconOnClick()
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
                    text = "가입하기",
                    //isEnabled = socialJoinViewModel.isButtonUserSocialJoinEnabled.value,
                    isEnabled = true,
                    modifier = Modifier
                        .fillMaxWidth(),
                    paddingTop = 10.dp,
                    paddingBottom = 1.dp,
                    onClick = {
                        socialJoinViewModel.buttonSocialJoinSubmitOnClick()
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
                    textFieldValue = socialJoinViewModel.textFieldSocialJoinIdValue,
                    label = "아이디",
                    placeHolder = "아이디",
                    enabled = socialJoinViewModel.isTextFieldSocialJoinIdEnabled.value,
                    modifier = Modifier
                        .weight(1f),
                    paddingTop = 10.dp,
                    inputCondition = "[^a-zA-Z0-9_]",
                    trailingIconMode = if (socialJoinViewModel.isCheckId.value) {
                        LikeLionOutlinedTextFieldEndIconMode.NONE
                    } else {
                        LikeLionOutlinedTextFieldEndIconMode.TEXT
                    },
                    onTrailingIconClick = {
                        socialJoinViewModel.updateCheckIdButtonState()
                    },
                    singleLine = true,
                    isError = socialJoinViewModel.textFieldSocialJoinIdError,
                    supportText = socialJoinViewModel.textFieldSocialJoinIdErrorText,
                    onValueChange = {
                        if (socialJoinViewModel.isTextFieldSocialJoinIdEnabled.value) {
                            socialJoinViewModel.textFieldSocialJoinIdValue.value = it
                            socialJoinViewModel.updateCheckIdButtonState()
                        }
                    },
                )

                // 아이디 중복확인 버튼
                LikeLionFilledButton(
                    text = "중복확인",
                    isEnabled = socialJoinViewModel.isButtonSocialJoinIdEnabled.value,
                    modifier = Modifier
                        .weight(0.4f)
                        .padding(top = 6.dp, start = 5.dp)
                        .height(56.dp),
                    onClick = {
                        socialJoinViewModel.checkUserId()
                    },
                    cornerRadius = 5,
                    containerColor = SubColor,
                    buttonHeight = 60.dp,
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 개인정보 처리방침 체크박스
                LikeLionCheckBox(
                    text = "개인정보처리방침 동의",
                    checkedValue = socialJoinViewModel.isCheckBoxSocialJoinPrivacyAccept,
                    checkedColor = MainColor,
                    uncheckedColor = Color.LightGray,
                    modifier = Modifier,
                    textModifier = Modifier,
                )
                Text(
                    text = "(약관보기)",
                    color = MainColor,
                    style = Typography.bodySmall,
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .clickable {
                            socialJoinViewModel.moveToUserJoinPrivacyWebView()
                        },
                )
            }

            // ---------------------------- Dialog ------------------------

            // Dialog - 사용할 수 있는 아이디인 경우
            LikeLionAlertDialog(
                showDialogState = socialJoinViewModel.showDialogIdOk,
                title = "중복 확인",
                text = "사용할 수 있는 아이디 입니다.",
                confirmButtonTitle = "확인",
                confirmButtonOnClick = {
                    socialJoinViewModel.showDialogIdOk.value = false
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
                showDialogState = socialJoinViewModel.showDialogIdNo,
                title = "중복 확인",
                text = "사용할 수 없는 아이디 입니다.",
                confirmButtonTitle = "닫기",
                confirmButtonOnClick = {
                    socialJoinViewModel.showDialogIdNo.value = false
                },
                titleAlign = TextAlign.Center, // 제목 중앙 정렬
                textAlign = TextAlign.Center, // 본문 텍스트 중앙 정렬
                titleModifier = Modifier.fillMaxWidth(), // 제목 가로 중앙 정렬
                textModifier = Modifier.fillMaxWidth(), // 본문 가로 중앙 정렬
                confirmButtonModifier = Modifier.width(140.dp),
                dismissButtonModifier = Modifier.width(140.dp)
            )

            // 개인정보 처리방침 미동의
            LikeLionAlertDialog(
                showDialogState = socialJoinViewModel.showDialogPrivacyDisagree,
                title = "약관 동의 ",
                text = "개인정보처리방침에 동의해야 회원가입을 진행할 수 있어요.",
                confirmButtonTitle = "닫기",
                confirmButtonOnClick = {
                    socialJoinViewModel.showDialogPrivacyDisagree.value = false
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
                showDialogState = socialJoinViewModel.showDialogSocialJoinOk,
                title = "가입 완료",
                text = "가입이 완료되었습니다." +
                        "로그인을 진행해주세요 ",
                confirmButtonTitle = "확인",
                confirmButtonOnClick = {
                    socialJoinViewModel.moveToLoginScreen()
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