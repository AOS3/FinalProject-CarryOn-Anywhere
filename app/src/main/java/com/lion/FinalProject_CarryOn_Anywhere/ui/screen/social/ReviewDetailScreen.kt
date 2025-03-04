package com.lion.FinalProject_CarryOn_Anywhere.ui.screen.social

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import coil3.compose.rememberAsyncImagePainter
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.DocumentId
import com.lion.FinalProject_CarryOn_Anywhere.CarryOnApplication
import com.lion.FinalProject_CarryOn_Anywhere.R
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionAddPlaceItem
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionAlertDialog
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionDivider
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionLikeButton
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionTopAppBar
import com.lion.FinalProject_CarryOn_Anywhere.component.shimmerEffect
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.ScreenName
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.GrayColor
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.SubColor
import com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.social.ReviewViewModel
import com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.trip.TripInfoViewModel
import androidx.compose.foundation.layout.Spacer as Spacer1
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ReviewDetailScreen(
    reviewViewModel: ReviewViewModel = hiltViewModel(),
    tripInfoViewModel: TripInfoViewModel = hiltViewModel(),
    documentId: String,
    navController: NavController,
    onAddClick: () -> Unit
) {
    // "여행 후기" 목록을 가져오고 선택된 "여행 후기"를 찾음
    val reviews by reviewViewModel.reviews.collectAsState()
    val review = reviews.find { it.documentId == documentId }

    // 로딩 상태 감지
    val isLoading by reviewViewModel.isLoading.collectAsState()

    if (review == null) {
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
    } else {
        val context = LocalContext.current

        // 다이얼로그 상태 변수 (초기값: false)
        val showDialogDeleteState = remember { mutableStateOf(false) }
        // 로그인 유도 다이얼로그 상태
        val showLoginDialog = remember { mutableStateOf(false) }

        // 현재 로그인한 사용자 정보 가져오기 (안전한 null 체크)
        val carryOnApplication = context.applicationContext as? CarryOnApplication
        val loginUserId = try {
            carryOnApplication?.loginUserModel?.userDocumentId ?: "guest"
        } catch (e: UninitializedPropertyAccessException) {
            "guest"
        }
        // 로그인하지 않은 경우 버튼 숨김
        val isAuthor = loginUserId != "guest" && review.author == loginUserId

        // 시스템 바텀바 높이 가져오기
        val systemBarHeight = getNavigationBarHeight().dp

        // 여행 날짜 목록 업데이트
        LaunchedEffect(tripInfoViewModel.startDate.value, tripInfoViewModel.endDate.value) {
            tripInfoViewModel.updateFormattedDates()
            tripInfoViewModel.updateTripDays()
        }

        // 좋아요 상태를 유지하기 위한 변수
        val isLiked =
            remember { mutableStateOf(review.tripReviewLikeUserList.contains(loginUserId)) }
        val likeCount = remember { mutableStateOf(review.likes) }

        // 최신 데이터 반영
        LaunchedEffect(Unit) {
            reviewViewModel.fetchTripReviews()
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // 상단 AppBar
                LikeLionTopAppBar(
                    title = "여행 후기",
                    backColor = Color.White,
                    navigationIconImage = Icons.Default.ArrowBack,
                    navigationIconOnClick = { navController.popBackStack() },
                    menuItems = {
                        if (isAuthor) {
                            Row {
                                IconButton(onClick = {
                                    navController.navigate("modifyScreen/review/$documentId")
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

                // "삭제" 다이얼로그 표시
                LikeLionAlertDialog(
                    showDialogState = showDialogDeleteState,
                    title = "글을 삭제하시겠습니까?",
                    text = "삭제되면 복구할 수 없습니다.",
                    confirmButtonTitle = "삭제",
                    confirmButtonOnClick = {
                        showDialogDeleteState.value = false
                        reviewViewModel.deleteTripReview(
                            review.documentId,
                            onSuccess = {
                                navController.popBackStack()
                            },
                            onError = { errorMessage ->
                                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                            }
                        )
                    },
                    dismissButtonTitle = "취소",
                    dismissButtonOnClick = {
                        showDialogDeleteState.value = false
                    },
                    titleAlign = TextAlign.Center, // 제목 중앙 정렬
                    textAlign = TextAlign.Center, // 본문 텍스트 중앙 정렬
                    titleModifier = Modifier.fillMaxWidth(), // 제목 가로 중앙 정렬
                    textModifier = Modifier.fillMaxWidth(), // 본문 가로 중앙 정렬
                    confirmButtonModifier = Modifier.width(120.dp),
                    dismissButtonModifier = Modifier.width(120.dp)
                )

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
                                text = "${review.nickName} • ${formattedDate(review.postDate)}",
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
                    items(review.imageUrls) { imageUrl ->
                        val isImageLoaded = remember { mutableStateOf(false) }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(RoundedCornerShape(10.dp))
                        ) {
                            if (!isImageLoaded.value) {
                                SkeletonPlaceholder()
                            }

                            Image(
                                painter = rememberAsyncImagePainter(
                                    model = imageUrl,
                                    onSuccess = { isImageLoaded.value = true } // 이미지 로딩 완료 시
                                ),
                                contentDescription = "Review Image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(10.dp))
                            )
                        }
                    }

                    // 일별 일정 목록 출력
                    item {
                        if (review.sharePlan.isNotEmpty()) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp, vertical = 10.dp)
                            ) {
                                // 제목
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 15.dp, bottom = 15.dp),
                                    verticalAlignment = Alignment.Bottom
                                ) {
                                    Text(
                                        text = review.shareTitle.ifEmpty { "제목 없음" },
                                        style = MaterialTheme.typography.titleLarge,
                                        modifier = Modifier.padding(end = 10.dp)
                                    )
                                }

                                // 일정 날짜
                                Text(
                                    text = review.tripDate.ifEmpty { "날짜 없음" },
                                    style = MaterialTheme.typography.bodySmall,
                                    color = GrayColor,
                                    modifier = Modifier.padding(bottom = 15.dp)
                                )

                                // 지역 정보
                                review.sharePlace.forEach { place ->
                                    Text(
                                        text = "📍 여행 지역: $place",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = GrayColor,
                                        modifier = Modifier.padding(bottom = 5.dp)
                                    )
                                }
                            }

                            // "일별 일정 목록"
                            review.sharePlan.groupBy {
                                it["date"] ?: "날짜 없음"
                            }.entries.forEachIndexed { index, (day, places) ->
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 10.dp)
                                ) {
                                    // DayX 표시 + 날짜
                                    Row(
                                        modifier = Modifier.padding(top = 10.dp, bottom = 20.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "Day${index + 1}  $day",
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = Color.Black
                                        )
                                    }

                                    places.forEachIndexed { placeIndex, place ->
                                        val placeName = place["place"] ?: "장소 없음"
                                        val addr1 = place["addr"] ?: "주소 정보 없음"
                                        val addr2 = place["addrDetail"] ?: ""

                                        val mapX =
                                            place["mapx"]?.toString()?.toDoubleOrNull() ?: 0.0
                                        val mapY =
                                            place["mapy"]?.toString()?.toDoubleOrNull() ?: 0.0

                                        val distanceToNext = if (placeIndex < places.lastIndex) {
                                            val nextPlace = places[placeIndex + 1]
                                            val nextMapX =
                                                nextPlace["mapx"]?.toString()?.toDoubleOrNull()
                                                    ?: 0.0
                                            val nextMapY =
                                                nextPlace["mapy"]?.toString()?.toDoubleOrNull()
                                                    ?: 0.0

                                            tripInfoViewModel.calculateDistance(
                                                LatLng(mapY, mapX),
                                                LatLng(nextMapY, nextMapX)
                                            )
                                        } else {
                                            null
                                        }

                                        LikeLionAddPlaceItem(
                                            index = placeIndex,
                                            lastIndex = places.lastIndex,
                                            place = mapOf(
                                                "title" to placeName,
                                                "addr1" to addr1,
                                                "addr2" to addr2
                                            ),
                                            distanceToNext = distanceToNext
                                        )
                                    }

                                    LikeLionDivider(
                                        modifier = Modifier.padding(vertical = 10.dp),
                                        color = Color.LightGray,
                                        thickness = 1.dp
                                    )
                                }
                            }
                        }
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
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            LikeLionLikeButton(
                                size = 30,
                                isLiked = isLiked.value,
                                onClick = {
                                    if (loginUserId == "guest") {
                                        showLoginDialog.value = true
                                    } else {
                                        reviewViewModel.toggleLike(
                                            review.documentId,
                                            loginUserId
                                        )

                                        isLiked.value = !isLiked.value

                                        likeCount.value = if (isLiked.value) {
                                            likeCount.value + 1
                                        } else {
                                            likeCount.value - 1
                                        }
                                    }
                                }
                            )

                            Text(
                                text = likeCount.value.toString(),
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(start = 5.dp)
                            )
                        }

                        Spacer1(modifier = Modifier.width(20.dp))

                        // 댓글 버튼
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = { navController.navigate("commentScreen/${review.documentId}") }) {
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
                    IconButton(
                        onClick = {
                            Toast.makeText(context, "추후 구현 예정입니다.", Toast.LENGTH_SHORT).show()
                        }
                    ) {
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
}

// 날짜 변환
private fun formattedDate(timestamp: Long): String {
    val date = Date(timestamp)
    val format = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    return format.format(date)
}

@Composable
private fun SkeletonPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(10.dp))
            .shimmerEffect(radius = 10.dp) // 기존 shimmer 효과 적용
    )
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
        onAddClick = {},
        documentId = "documentId"
    )
}