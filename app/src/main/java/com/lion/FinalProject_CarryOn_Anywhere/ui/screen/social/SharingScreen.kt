package com.lion.FinalProject_CarryOn_Anywhere.ui.screen.social

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionEmptyView
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionFilledButton
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionTopAppBar
import com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.social.Share
import com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.social.SharingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SharingScreen(
    sharingViewModel: SharingViewModel = hiltViewModel(),
    navController: NavController,
) {
    val shares by sharingViewModel.shares.collectAsState()

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White) // 배경색을 흰색으로 설정
    ) {
        // 상단 AppBar
        LikeLionTopAppBar(
            title = "일정 공유",
            backColor = Color.White,
            navigationIconImage = Icons.Default.ArrowBack,
            navigationIconOnClick = { navController.popBackStack() },
            menuItems = {
            }
        )

        if (shares.isEmpty()) {
            LikeLionEmptyView(message = "저장된 일정이 없습니다.")
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 20.dp)
            ) {
                items(shares.size) { index ->
                    ShareItem(shares[index], navController, index)
                }
            }
        }
    }
}

@Composable
private fun ShareItem(share: Share, navController: NavController, index: Int) {
    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp, horizontal = 15.dp)
            .clickable {
                navController.navigate("storyDetail/$index")
            }
    ) {
        Row(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 왼쪽 Column (태그, 제목, 내용, 작성자 정보)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(), // Row의 높이를 상속
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // 제목
                Text(
                    text = share.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                // 작성자 · 작성 날짜
                Text(
                    text = share.date,
                    fontSize = 12.sp,
                    color = Color.LightGray
                )
            }

            // 오른쪽 Column (선택 버튼)
            Column(
                modifier = Modifier
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                LikeLionFilledButton(
                    text = "선택",
                    cornerRadius = 100,
                    fillWidth = false, // 가로 길이를 자동 조정하도록 설정
                    modifier = Modifier
                        .wrapContentSize(), // 버튼 크기를 내부 컨텐츠(텍스트) 크기에 맞춤
                    onClick = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SharingScreenPreview() {
    SharingScreen(
        navController = NavController(LocalContext.current),
    )
}