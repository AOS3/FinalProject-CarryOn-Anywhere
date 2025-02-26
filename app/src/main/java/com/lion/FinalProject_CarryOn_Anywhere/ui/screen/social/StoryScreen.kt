package com.lion.FinalProject_CarryOn_Anywhere.ui.screen.social

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.rememberAsyncImagePainter
import com.lion.FinalProject_CarryOn_Anywhere.R
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionEmptyView
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionFilterChip
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionLikeButton
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.SubColor
import com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.social.Post
import com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.social.StoryViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoryScreen(
    storyViewModel: StoryViewModel = hiltViewModel(),
    navController: NavController
) {
    val posts by storyViewModel.posts.collectAsState()
    // 로딩 상태 감지
    val isLoading by storyViewModel.isLoading.collectAsState()

    // 태그 목록
    val chipItems = listOf("전체", "맛집", "숙소", "여행 일정", "모임")
    val scrollState = rememberScrollState()
    val selectedChip = remember { mutableStateOf(chipItems[0]) }

    // 선택된 태그에 따라 필터링된 게시글 목록 생성
    val filteredPosts = if (selectedChip.value == "전체") {
        posts // 전체 글 보기
    } else {
        posts.filter { it.tag == selectedChip.value }
    }

    // 최신 데이터 반영
    LaunchedEffect(Unit) {
        storyViewModel.fetchCarryTalkPosts()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(5.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState),
            horizontalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            chipItems.forEach { chipText ->
                LikeLionFilterChip(
                    text = chipText,
                    selected = selectedChip.value == chipText,
                    selectedColor = SubColor,
                    unselectedColor = Color.White,
                    borderColor = SubColor,
                    chipTextStyle = TextStyle(
                        color = if (selectedChip.value == chipText) Color.White else SubColor,
                        textAlign = TextAlign.Center
                    ),
                    selectedTextColor = Color.White,
                    unselectedTextColor = SubColor,
                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp),
                    chipModifier = Modifier
                        .padding(4.dp)
                        .width(60.dp),
                    cornerRadius = 100,
                    onChipClicked = { text, _ ->
                        selectedChip.value = text // 선택된 태그 변경
                    },
                    onDeleteButtonClicked = null
                )
            }
        }

        // Firestore에서 데이터를 가져오는 동안 로딩 표시
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
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

        // 필터링된 게시글이 없을 경우
        if (filteredPosts.isEmpty()) {
            LikeLionEmptyView(message = "선택한 태그에 해당하는 이야기가 없습니다.")
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 20.dp)
            ) {
                items(filteredPosts.size) { index ->
                    PostItem(filteredPosts[index], navController, index)
                }
            }
        }
    }
}

@Composable
private fun PostItem(post: Post, navController: NavController, index: Int) {
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
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // 태그
                Text(
                    text = post.tag,
                    fontSize = 12.sp,
                    color = Color.White,
                    modifier = Modifier
                        .background(SubColor, RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                )

                Spacer(modifier = Modifier.height(6.dp))

                // 제목
                Text(
                    text = post.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                // 내용
                Text(
                    text = post.content,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(10.dp))

                // 작성자 · 작성 날짜
                Text(
                    text = "${post.nickName} · ${formattedDate(post.postDate)}",
                    fontSize = 12.sp,
                    color = Color.LightGray
                )
            }

            Spacer(modifier = Modifier.width(10.dp)) // 좌우 간격 유지

            // 오른쪽 Column (이미지 + 좋아요 & 댓글)
            Column(
                modifier = Modifier
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // 이미지
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .padding(bottom = 8.dp)
                ) {
                    // Firebase Storage URL을 위한 Coil 이미지 로더 사용
                    post.imageUrls.firstOrNull()?.let { imageUrl ->
                        Image(
                            painter = rememberAsyncImagePainter(imageUrl),
                            contentDescription = "Post Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(10.dp))
                        )
                    }
                }

                // 좋아요 & 댓글
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 좋아요
                    Row(verticalAlignment = Alignment.CenterVertically) {
//                        LikeLionLikeButton()
                        Text(
                            text = " ${post.likes}",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 1.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    // 댓글
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(R.drawable.chat_24px),
                            contentDescription = "Comments",
                            tint = Color.Black,
                            modifier = Modifier.size(20.dp),
                        )
                        Text(
                            text = " ${post.comments}",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 1.dp)
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


@Preview(showBackground = true)
@Composable
private fun ReviewScreenPreview() {
    StoryScreen(
        navController = NavController(LocalContext.current)
    )
}