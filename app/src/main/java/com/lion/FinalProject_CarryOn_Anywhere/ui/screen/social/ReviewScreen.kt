package com.lion.FinalProject_CarryOn_Anywhere.ui.screen.review

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.rememberAsyncImagePainter
import com.lion.FinalProject_CarryOn_Anywhere.CarryOnApplication
import com.lion.FinalProject_CarryOn_Anywhere.R
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionEmptyView
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionLikeButton
import com.lion.FinalProject_CarryOn_Anywhere.component.shimmerEffect
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.ScreenName
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.SubColor
import com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.social.Review
import com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.social.ReviewViewModel

@Composable
fun ReviewScreen(
    reviewViewModel: ReviewViewModel = hiltViewModel(),
    navController: NavController
) {
    val reviews by reviewViewModel.reviews.collectAsState()
    // 로딩 상태 감지
    val isLoading by reviewViewModel.isLoading.collectAsState()

    val context = LocalContext.current

    // 현재 로그인한 사용자 정보 가져오기 (안전한 null 체크)
    val carryOnApplication = context.applicationContext as? CarryOnApplication
    val loginUserId = try {
        carryOnApplication?.loginUserModel?.userDocumentId ?: "guest"
    } catch (e: UninitializedPropertyAccessException) {
        "guest"
    }

    // 로그인 유도 다이얼로그 상태
    val showLoginDialog = remember { mutableStateOf(false) }

    // 최신 데이터 반영
    LaunchedEffect(Unit) {
        reviewViewModel.fetchTripReviews()
    }

    // Firestore에서 데이터를 가져오는 동안 로딩 표시
    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator(color = SubColor)
                Spacer(modifier = Modifier.height(10.dp))
                Text("데이터를 불러오는 중...", color = Color.Gray)
            }
        }
        return
    }

    if (reviews.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            LikeLionEmptyView(message = "작성된 후기가 없습니다.")
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(5.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(bottom = 20.dp)
        ) {
            items(reviews.size) { index ->
                val review = reviews[index]

                ReviewCard(
                    review = review,
                    isLiked = review.tripReviewLikeUserList.contains(loginUserId),
                    onLikeClick = {
                        if (loginUserId == "guest") {
                            showLoginDialog.value = true
                        } else {
                            reviewViewModel.toggleLike(review.documentId, loginUserId)
                        }
                    },
                    onClick = { navController.navigate("reviewDetail/${review.documentId}") }
                )
            }
        }
    }

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
                        navController.navigate(ScreenName.LOGIN_SCREEN.name) // 로그인 화면 이동
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
}

@Composable
private fun ReviewCard(
    review: Review,
    isLiked: Boolean,
    onLikeClick: () -> Unit,
    onClick: () -> Unit
) {
    // Firestore에서 가져온 첫 번째 이미지 로딩 상태 감지
    val imageUrl = review.imageUrls.firstOrNull()
    val isImageLoaded = remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(10.dp)) {

            if (imageUrl != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(10.dp))
                ) {
                    if (!isImageLoaded.value) {
                        SkeletonPlaceholder()
                    }

                    Image(
                        painter = rememberAsyncImagePainter(
                            model = imageUrl,
                            onSuccess = { isImageLoaded.value = true } // 로딩 완료 시 상태 변경
                        ),
                        contentDescription = "여행 후기 이미지",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(10.dp))
                    )
                }
            }

            // 제목
            Text(
                text = review.title,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(vertical = 5.dp)
            )

            // 작성자
            Text(
                text = review.nickName,
                fontSize = 12.sp,
                color = Color.Gray
            )

            val tripDate = review.tripDate
            val modifiedTripDate = tripDate.replace(Regex("\\b20(\\d{2}-\\d{2}-\\d{2})\\b"), "$1")

            // 여행 기간
            val displayedTripDate = if (modifiedTripDate.isNullOrBlank() || modifiedTripDate.trim() == "~") {
                "일정 없음"
            } else {
                modifiedTripDate
            }

            Text(
                text = displayedTripDate,
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(vertical = 5.dp)
            )


            // 좋아요 및 댓글 UI
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    LikeLionLikeButton(
                        isLiked = isLiked,
                        onClick = onLikeClick
                    )
                    Text(
                        text = " ${review.likes}",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 1.dp)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // 댓글 아이콘 & 숫자
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(R.drawable.chat_24px),
                        contentDescription = "Comments",
                        modifier = Modifier.size(20.dp),
                        colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(Color.Black)
                    )
                    Text(
                        text = "${review.comments}",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 1.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun SkeletonPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(10.dp))
            .shimmerEffect(radius = 10.dp)
    )
}

@Preview(showBackground = true)
@Composable
private fun ReviewScreenPreview() {
    ReviewScreen(
        navController = NavController(LocalContext.current)
    )
}