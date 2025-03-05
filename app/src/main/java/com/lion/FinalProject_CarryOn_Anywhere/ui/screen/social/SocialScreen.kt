package com.lion.FinalProject_CarryOn_Anywhere.ui.screen.social

import LikeLionFixedTabs
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.lion.FinalProject_CarryOn_Anywhere.CarryOnApplication
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionTopAppBar
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.ScreenName
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

    // 로그인 유도 다이얼로그 상태
    val showLoginDialog = remember { mutableStateOf(false) }

    val context = LocalContext.current

    // 로그인 여부를 State로 관리
    val carryOnApplication = context.applicationContext as? CarryOnApplication
    val isLoggedIn = carryOnApplication?.isLoggedIn?.collectAsState()?.value ?: false

    // 로그인 여부에 따라 버튼 활성화
    val isAuthor = isLoggedIn

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
                        showLoginDialog.value = true
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

        // 로그인 유도 다이얼로그
        if (showLoginDialog.value) {
            AlertDialog(
                onDismissRequest = { showLoginDialog.value = false },
                title = { Text("로그인이 필요합니다") },
                text = { Text("이 기능을 사용하려면 로그인해야 합니다.") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showLoginDialog.value = false
                            navController.navigate(ScreenName.LOGIN_SCREEN.name)
                        }
                    ) {
                        Text("로그인하기")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showLoginDialog.value = false }
                    ) {
                        Text("취소")
                    }
                }
            )
        }

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