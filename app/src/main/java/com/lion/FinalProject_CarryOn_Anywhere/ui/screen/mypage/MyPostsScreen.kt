package com.lion.FinalProject_CarryOn_Anywhere.ui.screen.mypage

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.*
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionProductList
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionTopAppBar
import com.lion.FinalProject_CarryOn_Anywhere.data.server.model.myposts.ProductModel
import kotlinx.coroutines.launch
import com.lion.FinalProject_CarryOn_Anywhere.R // ✅ drawable 리소스 추가
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionMyCommentList
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionTripStoryList
import com.lion.FinalProject_CarryOn_Anywhere.data.server.model.myposts.ReplyModel
import com.lion.FinalProject_CarryOn_Anywhere.data.server.model.myposts.TripStoryModel
import androidx.compose.runtime.remember

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MyPostsScreen() {
    val tabTitles = listOf("여행 후기", "여행 이야기", "댓글")
    val pagerState = rememberPagerState(initialPage = 0)
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            LikeLionTopAppBar(
                title = "나의 글",
                backColor = Color.White,
                navigationIconImage = Icons.AutoMirrored.Filled.ArrowBack,
                navigationIconOnClick = { /* TODO: 뒤로 가기 기능 추가 */ },
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            TabRow(
                selectedTabIndex = pagerState.currentPage,
                backgroundColor = Color.White,
                contentColor = Color(0xFF0064FF)
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        text = { Text(title) },
                        selected = pagerState.currentPage == index,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        }
                    )
                }
            }

            HorizontalPager(
                state = pagerState,
                count = tabTitles.size,
               // modifier = Modifier.fillMaxSize()
            ) { page ->
                when (page) {
                    0 -> TravelReviewScreen()
                    1 -> TravelStoryScreen()
                    2 -> CommentsScreen()
                }
            }
        }
    }
}

// ✅ 여행 후기 화면 (테스트용 `ProductModel` 리스트 포함)
@Composable
fun TravelReviewScreen() {
    val testProductList = listOf(
        ProductModel(
            productTitleName = "제주도 여행 후기",
            productPeriod = "25-01-07 ~ 25-01-10",
            productImages = listOf(R.drawable.test1.toString()),
            productReviewCount = 15,
            productLikeCount = 6,
            ),
        ProductModel(
            productTitleName = "부산 여행 후기",
            productPeriod = "25-01-07 ~ 25-01-10",
            productImages = listOf(R.drawable.test1.toString()),
            productReviewCount = 20,
            productLikeCount = 6,
            ),
        ProductModel(
            productTitleName = "강릉 여행 후기",
            productPeriod = "25-01-07 ~ 25-01-10",
            productImages = listOf(R.drawable.test1.toString()),
            productReviewCount = 30,
            productLikeCount = 6,
        )
    )

    Column(
        modifier = Modifier.fillMaxSize().padding(10.dp)
    ) {

        Spacer(modifier = Modifier.height(5.dp))

        LikeLionProductList(
            productList = testProductList,
            onCreatorNameClick = {},
            onLikeClick = {},
            onItemClick = {},
            columns = 2
        )
    }
}


@Composable
fun TravelStoryScreen() {
    // ✅ 테스트용 여행 이야기 데이터 리스트
    val tripStories = listOf(
        TripStoryModel(
            TripStoryTitle = "자운드 여행 이야기",
            TripStoryContent = "자운드에 다녀왔습니다! 날씨가 너무 좋았어요~",
            TripStoryData = "2025-03-01",
            TripStoryViewCount = 26,
            TripStoryImages = listOf(R.drawable.test1.toString()), // ✅ 첫 번째 이미지
            TripStoryTag = listOf("여행 일정")
        ),
        TripStoryModel(
            TripStoryTitle = "대전 빵투어 이야기 1",
            TripStoryContent = "맛있는 빵을 찾아 대전에 오다! 1편입니다",
            TripStoryData = "2025-01-15",
            TripStoryViewCount = 56,
            TripStoryImages = listOf(R.drawable.test1.toString()), // ✅ 첫 번째 이미지
            TripStoryTag = listOf("맛집")
        ),
        TripStoryModel(
            TripStoryTitle = "대전 빵투어 이야기 2",
            TripStoryContent = "맛있는 빵을 찾아 대전에 다녀왔습니다! 2편입니다",
            TripStoryData = "2025-01-16",
            TripStoryViewCount = 126,
            TripStoryImages = listOf(R.drawable.test1.toString()), // ✅ 첫 번째 이미지
            TripStoryTag = listOf("맛집")
        ),
        TripStoryModel(
            TripStoryTitle = "대전 빵투어 이야기 3",
            TripStoryContent = "맛있는 빵을 찾아 대전에 다녀왔습니다! 3편입니다",
            TripStoryData = "2025-01-17",
            TripStoryViewCount = 150,
            TripStoryImages = listOf(R.drawable.test1.toString()), // ✅ 첫 번째 이미지
            TripStoryTag = listOf("맛집")
        )
    )

    Column(modifier = Modifier.fillMaxSize().padding(5.dp)) {
      //  Text("여행 이야기 리스트!!", style = MaterialTheme.typography.h6)


        // ✅ 여행 이야기 리스트 출력 (LazyColumn 사용)
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(tripStories) { story ->
                LikeLionTripStoryList(post = story, onClick = {
                    // ✅ 클릭 시 동작 추가 가능
                })
            }
        }
    }
}


// ✅ 댓글 화면
@Composable
fun CommentsScreen() {

    // ✅ 삭제 다이얼로그를 위한 상태 관리
    val showDialog = remember { mutableStateOf(false) }
    val selectedComment = remember { mutableStateOf<ReplyModel?>(null) }

    // ✅ 테스트용 여행 이야기 데이터 리스트
    val tripComments = listOf(
        ReplyModel(
            userDocumentId = "토토로",
            replyContent = "날씨가 좋아서 다행이에요. 너무 부럽습니다. 진짜 좋아보여요.",
            replyTimeStamp = "2025-02-14 10:25:55"
        ),
        ReplyModel(
            userDocumentId = "토토로",
            replyContent = "터질 것만 같은 행복한 기분으로 틀에 박힌 관념 다 버리고 이제 또 맨 주먹 정신 다시 또 시작하면 나 이루리라 다 나 바라는대로",
            replyTimeStamp = "2023-04-25 14:15:22"
        ),
        ReplyModel(
            userDocumentId = "토토로",
            replyContent = "파란 하늘위로 훨훨 날아가겠죠\n" +
                    "어려서 꿈꾸었던 비행기 타고\n" +
                    "기다리는 동안 아무말도 못해요 내 생각 말할 순 없어요",
            replyTimeStamp = "2025-02-14 10:25:55"
        ),
        ReplyModel(
            userDocumentId = "토토로",
            replyContent = "저 오늘 떠나요 공항으로 핸드폰 꺼 놔요 제발 날 찾진 말아줘 시끄럽게 소리를 질러도 어쩔 수 없어 나가볍게 손을 흔들며 bye bye-",
            replyTimeStamp = "2025-02-14 10:25:55"
        ),
    )

    Column(modifier = Modifier.fillMaxSize().padding(5.dp)) {
        // ✅ 댓글 리스트 출력
        LikeLionMyCommentList(
            commentList = tripComments,
            onDeleteConfirmed = { comment ->
                selectedComment.value = comment
                showDialog.value = true
            }
        )
    }

}


// ✅ 미리보기
@Preview(showBackground = true)
@Composable
fun PreviewMyPostsScreen() {
    MyPostsScreen()
}
