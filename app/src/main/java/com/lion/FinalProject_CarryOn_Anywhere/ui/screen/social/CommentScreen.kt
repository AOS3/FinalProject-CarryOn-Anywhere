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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionDivider
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionEmptyView
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionTopAppBar
import com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.social.Comment
import com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.social.CommnetViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentScreen(
    navController: NavController,
    commentViewModel: CommnetViewModel = hiltViewModel()
) {
    // 시스템 바텀바 높이 가져오기
    val systemBarHeight = getNavigationBarHeight().dp

    val commnets by commentViewModel.comments.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White) // 배경색을 흰색으로 설정
    ) {
        LikeLionTopAppBar(
            title = "댓글",
            backColor = Color.White,
            navigationIconImage = Icons.Default.ArrowBack,
            navigationIconOnClick = { navController.popBackStack() },
        )

        if (commnets.isEmpty()) {
            LikeLionEmptyView(message = "저장된 일정이 없습니다.")
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        bottom = WindowInsets.navigationBars.asPaddingValues()
                            .calculateBottomPadding()
                    ),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 20.dp),
            ) {
                items(commnets.size) { index ->
                    CommentItem(commnets[index], navController, index)

                    LikeLionDivider(
                        modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 5.dp),
                        color = Color.LightGray,
                        thickness = 1.dp
                    )
                }
            }
        }
    }
}

@Composable
private fun CommentItem(comment: Comment, navController: NavController, index: Int) {

    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
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
                .fillMaxHeight(), // Row의 높이를 자동으로 맞추도록 설정
            verticalAlignment = Alignment.CenterVertically // 세로 중앙 정렬 추가
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
                    text = comment.author,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                // 내용
                Text(
                    text = comment.content,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                // 작성 날짜
                Text(
                    text = comment.commentDate,
                    fontSize = 12.sp,
                    color = Color.LightGray
                )
            }

            // 오른쪽 Column (선택 버튼)
            // 오른쪽 Column (선택 버튼 + More 아이콘)
            Column(
                modifier = Modifier
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // More 아이콘 추가
                IconButton(
                    onClick = {  },
                    modifier = Modifier
                        .align(Alignment.End)
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More Options",
                        tint = Color.Black
                    )
                }
            }
        }
    }
}

// 시스템 바텀바 높이를 가져오는 함수
@Composable
private fun getNavigationBarHeight(): Float {
    return WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding().value
}

@Preview(showBackground = true)
@Composable
private fun ReviewScreenPreview() {
    CommentScreen(
        navController = NavController(LocalContext.current)
    )
}