package com.lion.FinalProject_CarryOn_Anywhere.ui.screen.social

import LikeLionFixedTabs
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionTopAppBar
import com.lion.FinalProject_CarryOn_Anywhere.ui.screen.review.ReviewScreen
import com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.social.SocialViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SocialScreen(
    navController: NavController,
    onAddClick: () -> Unit,
    socialViewModel: SocialViewModel = hiltViewModel()
) {
    Column {
        LikeLionTopAppBar(
            title = "캐리 톡",
            backColor = Color.White,
            navigationIconImage = null, // 왼쪽 네비게이션 아이콘 없음
            menuItems = {
                IconButton(onClick = { onAddClick() }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "추가",
                        tint = Color.Black
                    )
                }
            }
        )

        // StateFlow 값 감지
        val selectedTabIndex by socialViewModel.selectedTabIndex.collectAsState()

        LikeLionFixedTabs(
            tabTitleWithCounts = listOf("여행 후기", "여행 이야기"),
            selectedTabIndex = selectedTabIndex,
            onTabSelected = {
                socialViewModel.updateTabIndex(it)
            },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
        )

        // 선택된 탭에 따라 다른 콘텐츠 표시
        when (selectedTabIndex) {
            0 -> ReviewScreen(
                navController = navController
            )
            1 -> StoryScreen(
                navController = navController
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun CarryTalkTopAppBarPreview() {
    SocialScreen(
        navController = NavController(LocalContext.current),
        onAddClick = {}
    )
}