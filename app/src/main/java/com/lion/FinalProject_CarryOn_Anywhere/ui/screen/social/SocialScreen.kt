package com.lion.FinalProject_CarryOn_Anywhere.ui.screen.social

import LikeLionFixedTabs
import android.widget.Toast
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
import com.lion.FinalProject_CarryOn_Anywhere.CarryOnApplication
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
    // StateFlow 값 감지
    val selectedTabIndex by socialViewModel.selectedTabIndex.collectAsState()

    val context = LocalContext.current

    // 현재 로그인한 사용자 정보 가져오기 (안전한 null 체크)
    val carryOnApplication = context.applicationContext as? CarryOnApplication
    val loginUserId = try {
        carryOnApplication?.loginUserModel?.userDocumentId ?: "guest"
    } catch (e: UninitializedPropertyAccessException) {
        "guest"
    }
    // 로그인하지 않은 경우 버튼 숨김
    val isAuthor = loginUserId != "guest"

    Column {
        LikeLionTopAppBar(
            title = "캐리 톡",
            backColor = Color.White,
            navigationIconImage = null,
            menuItems = {
                IconButton(onClick = {
                    if (isAuthor) {
                        onAddClick()
                    } else {
                        Toast.makeText(
                            context,
                            "로그인을 먼저 진행해 주세요!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "추가",
                        tint = Color.Black
                    )
                }
            }
        )

        // "여행 후기", "여행 이야기" 선택 탭
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