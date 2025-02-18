package com.lion.FinalProject_CarryOn_Anywhere.ui.screen.mypage

import android.annotation.SuppressLint
import android.graphics.drawable.GradientDrawable
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.lion.FinalProject_CarryOn_Anywhere.R
import com.lion.FinalProject_CarryOn_Anywhere.component.*
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.ScreenName
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.MainColor
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.SubColor
import com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.UserSettingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditMyInfoScreen(
    navController: NavController,
    userSettingViewModel: UserSettingViewModel = hiltViewModel()
) {
    val showDialogWithdrawal = remember { mutableStateOf(false) } // 회원 탈퇴 다이얼로그 상태

    // ✅ 테스트용 사용자 정보
    var testUserId = remember { mutableStateOf("triponandon") }
    var testUserName = remember { mutableStateOf("엄근전") }
    var testUserPhone = remember { mutableStateOf("010-2222-2222") }
    var selectedOption = remember { mutableStateOf("미동의") }

    Scaffold(
        topBar = {
            LikeLionTopAppBar(
                title = "마이 페이지",
                backColor = Color.White,
                navigationIconImage = null,
                scrollValue = 0,
                navigationIconOnClick = {},
                menuItems = {},
                isTitleRightAligned = false,
                textOnClick = {}
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
        ) {
            // ✅ 상단 콘텐츠 (스크롤 가능)
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 80.dp) // 하단 버튼 공간 확보
                    .padding(20.dp)
            ) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        LikeLionProfileImg(
                            imgUrl = "", // 이미지 URL
                            iconTint = Color.Gray,
                            profileBack = Color.LightGray,
                            profileSize = 80.dp
                        )

                        Text(
                            text = "5MB 이내의 이미지 파일을 업로드 해주세요.\n(이미지 형식 : JPG, JPEG, PNG)",
                            color = Color.Gray,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    // ✅ SNS 계정 연결
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp)
                    ) {
                        LikeLionImage(
                            painter = painterResource(id = R.drawable.kakao_login_logo),
                            modifier = Modifier.size(40.dp),
                            isCircular = true,
                            onClick = {}
                        )
                    }

                    // ✅ 비밀번호 변경
                    Text(
                        text = "비밀번호",
                        color = Color.Black,
                        fontSize = 14.sp,
                        modifier = Modifier.fillMaxWidth()
                    )

                    LikeLionFilledButton(
                        text = "비밀번호 변경",
                        cornerRadius = 5,
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { navController.navigate(ScreenName.CHANGE_PW_SCREEN.name) }
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // ✅ 사용자 정보 입력 필드
                    LikeLionOutlinedTextField(
                        textFieldValue = testUserId,
                        label = "아이디",
                        readOnly = true,
                        singleLine = true
                    )

                    LikeLionOutlinedTextField(
                        textFieldValue = testUserName,
                        label = "이름",
                        singleLine = true,
                        onValueChange = { testUserName.value = it }
                    )

                    LikeLionOutlinedTextField(
                        textFieldValue = testUserPhone,
                        label = "연락처",
                        readOnly = true,
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // ✅ 앱 푸시 수신 동의
                    Text("앱 푸쉬 수신 동의", fontSize = 16.sp, color = Color.Black)
                    Spacer(modifier = Modifier.height(10.dp))

                    LikeLionRadioGroup(
                        options = listOf("동의", "미동의"),
                        selectedOption = selectedOption.value,
                        onOptionSelected = { selectedOption.value = it },
                        fontSize = 14.sp,
                        orientation = Orientation.Horizontal,
                        itemSpacing = 10,
                        modifier = Modifier.padding(top = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // ✅ 회원 탈퇴
                    Text(
                        text = "회원 탈퇴",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.clickable { showDialogWithdrawal.value = true }
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }

            // ✅ 하단 버튼 (화면 하단에 고정)
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(Color.White) // 버튼과 콘텐츠 분리
                    .padding(16.dp)
            ) {
                LikeLionFilledButton(
                    text = "저장하기",
                    modifier = Modifier.fillMaxWidth(),
                    cornerRadius = 5
                )
            }
        }
    }

    // ✅ 회원 탈퇴 다이얼로그
    if (showDialogWithdrawal.value) {
        LikeLionAlertDialog(
            showDialogState = showDialogWithdrawal,
            title = "회원탈퇴",
            titleModifier = Modifier.fillMaxWidth(),
            titleAlign = TextAlign.Center,
            text = "회원탈퇴 시 회원 정보 및 서비스 이용기록이 모두 삭제되며, 삭제한 데이터는 복구가 불가능합니다.",
            textModifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            confirmButtonTitle = "확인",
            confirmButtonOnClick = {
                showDialogWithdrawal.value = false
                // 회원 탈퇴 로직 추가
                navController.navigate(ScreenName.LOGIN_SCREEN.name) {
                    popUpTo(ScreenName.MY_PAGE.name) { inclusive = true }
                }
            },
            confirmButtonModifier = Modifier.padding(horizontal = 5.dp),
            dismissButtonTitle = "취소",
            dismissButtonOnClick = { showDialogWithdrawal.value = false },
            dismissButtonModifier = Modifier.padding(horizontal = 5.dp),
            dismissBorder = BorderStroke(1.dp, Color.LightGray),
        )
    }
}

// ✅ 미리보기
@Preview(showBackground = true)
@Composable
fun PreviewEditMyInfoScreen() {
    val navController = rememberNavController()
    EditMyInfoScreen(navController = navController)
}
