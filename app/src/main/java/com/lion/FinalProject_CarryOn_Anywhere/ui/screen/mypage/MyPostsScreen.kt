package com.lion.FinalProject_CarryOn_Anywhere.ui.screen.mypage

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.lion.FinalProject_CarryOn_Anywhere.data.server.model.myposts.TripStoryModel
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.lion.FinalProject_CarryOn_Anywhere.CarryOnApplication
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionDivider
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionEmptyView
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionFilterChip
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionMyLikeItem
import com.lion.FinalProject_CarryOn_Anywhere.data.server.model.ReplyModel
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.SubColor
import com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.social.CommentViewModel

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MyPostsScreen(navController: NavController) {
    val tabTitles = listOf("여행 후기", "여행 이야기", "댓글")
    val pagerState = rememberPagerState(initialPage = 0)
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            LikeLionTopAppBar(
                title = "나의 글",
                backColor = Color.White,
                navigationIconImage = Icons.AutoMirrored.Filled.ArrowBack,
                navigationIconOnClick = { navController.popBackStack() }
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
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
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
            TripStoryDate = "2025-03-01",
            TripStoryViewCount = 26,
            TripStoryImages = listOf(R.drawable.test1.toString()), // ✅ 첫 번째 이미지
            TripStoryTag = "여행 일정"
        ),
        TripStoryModel(
            TripStoryTitle = "대전 빵투어 이야기 1",
            TripStoryContent = "맛있는 빵을 찾아 대전에 오다! 1편입니다",
            TripStoryDate = "2025-01-15",
            TripStoryViewCount = 56,
            TripStoryImages = listOf(R.drawable.test1.toString()), // ✅ 첫 번째 이미지
            TripStoryTag = "맛집"
        ),
        TripStoryModel(
            TripStoryTitle = "대전 빵투어 이야기 2",
            TripStoryContent = "맛있는 빵을 찾아 대전에 다녀왔습니다! 2편입니다",
            TripStoryDate = "2025-01-16",
            TripStoryViewCount = 126,
            TripStoryImages = listOf(R.drawable.test1.toString()), // ✅ 첫 번째 이미지
            TripStoryTag = "맛집"
        ),
        TripStoryModel(
            TripStoryTitle = "대전 빵투어 이야기 3",
            TripStoryContent = "맛있는 빵을 찾아 대전에 다녀왔습니다! 3편입니다",
            TripStoryDate = "2025-01-17",
            TripStoryViewCount = 150,
            TripStoryImages = listOf(R.drawable.test1.toString()), // ✅ 첫 번째 이미지
            TripStoryTag = "맛집"
        )
    )


    //
    val chipItems = listOf("전체", "맛집", "숙소", "여행 일정", "모임")
    val scrollState = rememberScrollState()
    val selectedChip = remember { mutableStateOf(chipItems[0]) }

    // 선택된 태그에 따라 필터링된 게시글 목록 생성
    val filteredPosts = if (selectedChip.value == "전체") {
        tripStories // 전체 글 보기
    } else {
        tripStories.filter { it.TripStoryTag == selectedChip.value }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        // ✅ 카테고리 필터
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
                .padding(bottom = 5.dp)
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
                        selectedChip.value = text // ✅ 선택된 태그 변경
                    },
                    onDeleteButtonClicked = null
                )
            }
        }

        // ✅ 필터링된 데이터가 없을 경우 빈 화면 표시
        if (filteredPosts.isEmpty()) {
            LikeLionEmptyView(message = "선택한 태그에 해당하는 여행 이야기가 없습니다.")
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()

            ) {
                items(filteredPosts) { story ->
                    LikeLionTripStoryList(post = story, onClick = {
                        // ✅ 클릭 시 동작 추가 가능
                    })
                }
            }
        }
    }

}


// ✅ 댓글 화면
@Composable
fun CommentsScreen(
    commentViewModel: CommentViewModel = hiltViewModel(),
) {

    val context = LocalContext.current

    // ✅ 삭제 다이얼로그를 위한 상태 관리
    val showDialog = remember { mutableStateOf(false) }
    val selectedComment = remember { mutableStateOf<ReplyModel?>(null) }

    // ViewModel의 LiveData를 observe하여 실제 댓글 목록 사용
    val tripComments by commentViewModel.myAllReplys.collectAsState()
    Log.d("test100","replyList선언 후 ${tripComments}")

    // 현재 로그인한 사용자 정보 가져오기 (안전한 null 체크)
    val carryOnApplication = context.applicationContext as? CarryOnApplication
    val userId = try {
        carryOnApplication?.loginUserModel?.userId ?: "guest"
    } catch (e: UninitializedPropertyAccessException) {
        "guest"
    }

    // 화면이 구성될 때 댓글을 불러옴
    LaunchedEffect(userId) {
        commentViewModel.getAllReplysByUserId(userId)
    }


    Column(modifier = Modifier
        .fillMaxSize()
        .padding(5.dp)) {
        // ✅ 댓글 리스트 출력
        LikeLionMyCommentList(
            commentList = tripComments,
            onDeleteConfirmed = { comment ->

                commentViewModel.deleteReplyByReplyDocId(comment.replyDocumentId,comment.userId)
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

    val navController = rememberNavController() // ✅ 미리보기용 NavController 생성
    MyPostsScreen(navController = navController)
}
