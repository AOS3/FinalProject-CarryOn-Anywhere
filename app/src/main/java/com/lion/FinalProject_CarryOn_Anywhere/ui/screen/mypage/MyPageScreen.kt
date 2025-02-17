package com.lion.FinalProject_CarryOn_Anywhere.ui.screen.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.lion.FinalProject_CarryOn_Anywhere.component.*
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.ScreenName

@Composable
fun MyPageScreen(navController: NavController) {
    val showLogoutDialog = remember { mutableStateOf(false) } // 로그아웃 다이얼로그 상태

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        LikeLionTopAppBar(
            title = "마이 페이지",
            backColor = Color.White,
            navigationIconImage = null, // 네비게이션 아이콘 없음
            scrollValue = 0,
            navigationIconOnClick = {},
            menuItems = {},
            isTitleRightAligned = false,
            textOnClick = {}
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp) // ✅ 20dp 패딩 추가
        ) {
            // 프로필 정보
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LikeLionProfileImg(
                    imgUrl = "", // 이미지 URL
                    iconTint = Color.Gray,
                    profileBack = Color.LightGray,
                    profileSize = 80.dp
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "triponandon 님",
                    fontSize = 20.sp,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            // 메뉴 리스트
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                SectionTitle("내 정보 설정")
                MenuItem("계정 설정") { navController.navigate(ScreenName.EDIT_MY_INFO.name) }
                MenuItem("내가 쓴 글") { navController.navigate(ScreenName.MY_POSTS.name) }
                MenuItem("내 일정") { navController.navigate(ScreenName.MY_TRIP_PLAN.name)  }
                MenuItem("로그아웃") { showLogoutDialog.value = true }

                Spacer(modifier = Modifier.height(20.dp))

                SectionTitle("서비스 약관")
                MenuItem("서비스 이용 약관") { /* TODO: 약관 화면 이동 */ }
                MenuItem("개인정보 처리방침") { /* TODO: 개인정보 화면 이동 */ }
            }
        }
    }

    // 로그아웃 다이얼로그
    if (showLogoutDialog.value) {
        LikeLionAlertDialog(
            showDialogState = showLogoutDialog,
            title = "로그아웃",
            text = "로그아웃 하시겠습니까?",
            confirmButtonTitle = "로그아웃 하기",
            dismissButtonTitle = "취소",
            confirmButtonOnClick = {
                showLogoutDialog.value = false
                // TODO: 로그아웃 처리 (예: 로그인 화면으로 이동)
                navController.navigate(ScreenName.LOGIN_SCREEN.name) {
                    popUpTo(ScreenName.MY_PAGE.name) { inclusive = true }
                }
            }
        )
    }
}

// ✅ 섹션 타이틀
@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 20.sp,
        color = Color.DarkGray,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
fun MenuItem(text: String, onClick: () -> Unit) {
    Text(
        text = text,
        fontSize = 14.sp,
        color = Color.Black,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .clickable { onClick() }
    )
}
