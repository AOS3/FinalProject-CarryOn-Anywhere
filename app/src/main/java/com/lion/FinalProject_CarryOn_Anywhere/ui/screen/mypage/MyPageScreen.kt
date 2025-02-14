package com.lion.FinalProject_CarryOn_Anywhere.ui.screen.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.lion.FinalProject_CarryOn_Anywhere.component.BottomNavigationItemData
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionBottomNavigation
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionProfileImg
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionTopAppBar


import com.lion.FinalProject_CarryOn_Anywhere.component.*

@Composable
fun MyPageScreen(navController: NavController) {
    Scaffold(
        bottomBar = {
            LikeLionBottomNavigation(
                navController = navController,
                items = LikeLionBottomNavItems(isLoggedIn = true) // ✅ 로그인 상태 전달
            )
        }
    ) { _ ->
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
                    MenuItem("계정 설정")
                    MenuItem("내가 쓴 글")
                    MenuItem("내 일정")
                    MenuItem("로그아웃")

                    Spacer(modifier = Modifier.height(20.dp))

                    SectionTitle("서비스 약관")
                    MenuItem("서비스 이용 약관")
                    MenuItem("개인정보 처리방침")
                }
            }
        }
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

// ✅ 메뉴 아이템
@Composable
fun MenuItem(text: String) {
    Text(
        text = text,
        fontSize = 14.sp,
        color = Color.Black,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .clickable { /* TODO: 메뉴 클릭 처리 */ }
    )
}
