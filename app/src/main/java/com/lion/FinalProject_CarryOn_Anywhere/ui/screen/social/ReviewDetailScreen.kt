package com.lion.FinalProject_CarryOn_Anywhere.ui.screen.social

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.IosShare
import androidx.compose.material.icons.filled.ModeEdit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.lion.FinalProject_CarryOn_Anywhere.R
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionAlertDialog
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionDivider
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionLikeButton
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionTopAppBar
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.ScreenName
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.GrayColor
import com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.social.ReviewViewModel
import androidx.compose.foundation.layout.Spacer as Spacer1

@Composable
fun ReviewDetailScreen(
    reviewViewModel: ReviewViewModel = hiltViewModel(),
    reviewIndex: Int,
    navController: NavController,
    onAddClick: () -> Unit
) {
    val reviews by reviewViewModel.reviews.collectAsState()
    val review = reviews.getOrNull(reviewIndex) ?: return

    // 다이얼로그 상태 변수 (초기값: false)
    val showDialogDeleteState = remember { mutableStateOf(false) }

    // 시스템 바텀바 높이 가져오기
    val systemBarHeight = getNavigationBarHeight().dp

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // AppBar
            LikeLionTopAppBar(
                title = "여행 후기",
                backColor = Color.White,
                navigationIconImage = Icons.Default.ArrowBack,
                navigationIconOnClick = { navController.popBackStack() },
                menuItems = {
                    Row {
                        IconButton(onClick = {
                            navController.navigate("modifyScreen/${reviewIndex}")
                        }) {
                            Icon(
                                imageVector = Icons.Default.ModeEdit,
                                contentDescription = "수정",
                                tint = Color.Black
                            )
                        }
                        IconButton(onClick = { showDialogDeleteState.value = true }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "삭제",
                                tint = Color.Black
                            )
                        }
                    }
                }
            )

            // 다이얼로그 표시
            LikeLionAlertDialog(
                showDialogState = showDialogDeleteState,
                title = "글을 삭제하시겠습니까?",
                text = "삭제되면 복구할 수 없습니다.",
                confirmButtonTitle = "삭제",
                confirmButtonOnClick = {
                    showDialogDeleteState.value = false
                    navController.popBackStack()
                },
                dismissButtonTitle = "취소",
                dismissButtonOnClick = {
                    showDialogDeleteState.value = false
                },
                titleAlign = TextAlign.Center, // 제목 중앙 정렬
                textAlign = TextAlign.Center, // 본문 텍스트 중앙 정렬
                titleModifier = Modifier.fillMaxWidth(), // 제목 가로 중앙 정렬
                textModifier = Modifier.fillMaxWidth(), // 본문 가로 중앙 정렬
                confirmButtonModifier = Modifier.width(140.dp),
                dismissButtonModifier = Modifier.width(140.dp)
            )

            // 본문 내용 (LazyColumn)
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 60.dp + systemBarHeight)
            ) {
                item {
                    // 제목
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = review.title,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            maxLines = 2
                        )
                    }

                    Spacer1(modifier = Modifier.height(10.dp))

                    // 작성자 정보
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = "${review.author} • ${review.postDate}",
                            style = MaterialTheme.typography.bodySmall,
                            color = GrayColor
                        )
                    }

                    Spacer1(modifier = Modifier.height(10.dp))

                    LikeLionDivider(
                        modifier = Modifier.padding(),
                        color = Color.LightGray,
                        thickness = 1.dp
                    )

                    Spacer1(modifier = Modifier.height(10.dp))

                    // 내용
                    Text(
                        text = review.content,
                        style = MaterialTheme.typography.bodyLarge,
                    )

                    Spacer1(modifier = Modifier.height(10.dp))
                }

                // 이미지 리스트
                items(review.imageRes) { imageRes ->
                    Image(
                        painter = painterResource(imageRes),
                        contentDescription = "Review Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(10.dp))
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp + systemBarHeight)
                .background(Color.White.copy(alpha = 0.7f))
                .align(Alignment.BottomCenter),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = systemBarHeight),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // 좋아요 & 댓글
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 좋아요 버튼
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        LikeLionLikeButton(
                            size = 30
                        )
                        Text(
                            text = review.likes.toString(),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(start = 3.dp)
                        )
                    }

                    Spacer1(modifier = Modifier.width(20.dp))

                    // 댓글 버튼
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { navController.navigate(ScreenName.COMMENT_SCREEN.name) }) {
                            Icon(
                                painter = painterResource(R.drawable.chat_24px),
                                contentDescription = "댓글",
                                modifier = Modifier.size(30.dp)
                            )
                        }
                        Text(
                            text = review.comments.toString(),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(start = 3.dp)
                        )
                    }
                }

                // 공유 버튼
                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.Default.IosShare,
                        contentDescription = "Share",
                        modifier = Modifier.size(30.dp),
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
private fun ReviewDetailScreenPreview() {
    ReviewDetailScreen(
        navController = NavController(LocalContext.current),
        reviewIndex = 0,
        onAddClick = {}
    )
}