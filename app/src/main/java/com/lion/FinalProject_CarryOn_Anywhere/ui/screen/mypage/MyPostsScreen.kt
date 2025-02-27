package com.lion.FinalProject_CarryOn_Anywhere.ui.screen.mypage

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
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.*
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionProductList
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionTopAppBar
import kotlinx.coroutines.launch
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionMyCommentList
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionTripStoryList
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.lion.FinalProject_CarryOn_Anywhere.CarryOnApplication
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionEmptyView
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionFilterChip
import com.lion.FinalProject_CarryOn_Anywhere.data.server.model.ReplyModel
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.SubColor
import com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.mypage.MyPostsViewModel

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
                    0 -> TravelReviewScreen(navController)
                    1 -> TravelStoryScreen(navController)
                    2 -> CommentsScreen()
                }
            }
        }
    }
}

// ✅ 여행 후기 화면 -> 진행중
@Composable
fun TravelReviewScreen(
    navController: NavController,
    myPostsViewModel: MyPostsViewModel = hiltViewModel(),
) {
    val context = LocalContext.current

    // ViewModel의 LiveData를 observe하여 실제 댓글 목록 사용
    val tripReviews by myPostsViewModel.myTripReviews.collectAsState()


    // 현재 로그인한 사용자 정보 가져오기 (안전한 null 체크)
    val carryOnApplication = context.applicationContext as? CarryOnApplication

    val userDocumentId = try {
        carryOnApplication?.loginUserModel?.userDocumentId ?: ""
    } catch (e: UninitializedPropertyAccessException) {
        ""
    }

    LaunchedEffect(Unit) {
            myPostsViewModel.getMyTripReviews(userDocumentId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        Spacer(modifier = Modifier.height(5.dp))


        // ✅ 필터링된 데이터가 없을 경우 빈 화면 표시
        if (tripReviews.isEmpty()) {
            LikeLionEmptyView(message = "선택한 태그에 해당하는 여행 후기가 없습니다.")
        } else {
            LikeLionProductList(
                productList = tripReviews,
                onCreatorNameClick = { /* 작성자 클릭 처리 */ },
                onLikeClick = { /* 좋아요 클릭 처리 */ },
                onItemClick = { product ->
                    // 리스트에서 해당 제품의 인덱스를 구하여 reviewDetail 화면으로 이동
                    val index = tripReviews.indexOf(product)
                    navController.navigate("reviewDetail/$index")
                },
                columns = 2
            )
        }
    }
}


@Composable
fun TravelStoryScreen(
    navController: NavController,
    myPostsViewModel: MyPostsViewModel = hiltViewModel(),
) {

    val context = LocalContext.current

    // ViewModel의 LiveData를 observe하여 실제 댓글 목록 사용
    val tripStories by myPostsViewModel.myCarryTalk.collectAsState()

    val chipItems = listOf("전체", "맛집", "숙소", "여행 일정", "모임")
    val scrollState = rememberScrollState()
    val selectedChip = remember { mutableStateOf(chipItems[0]) }

    // 선택된 태그에 따라 필터링된 게시글 목록 생성
    val filteredPosts = if (selectedChip.value == "전체") {
        tripStories // 전체 글 보기
    } else {
        tripStories.filter { it.talkTag.str == selectedChip.value }
    }

    val carryOnApplication = context.applicationContext as? CarryOnApplication
    val userDocumentId = try {
        carryOnApplication?.loginUserModel?.userDocumentId ?: ""
    } catch (e: UninitializedPropertyAccessException) {
        ""
    }

    LaunchedEffect(Unit) {
        myPostsViewModel.getMyCarryTalk(userDocumentId)
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
                    LikeLionTripStoryList(
                        post = story,
                        onClick = {
                        // 클릭 시 해당 여행 이야기로 이동
                            val index = filteredPosts.indexOf(story)
                            navController.navigate("storyDetail/$index")

                            myPostsViewModel.getMyCarryTalk(userDocumentId)
                        }
                    )
                }
            }
        }
    }
}


// ✅ 댓글 화면 -> DB 연결 완료
@Composable
fun CommentsScreen(
    myPostsViewModel: MyPostsViewModel = hiltViewModel(),
) {

    val context = LocalContext.current

    // ✅ 삭제 다이얼로그를 위한 상태 관리
    val showDialog = remember { mutableStateOf(false) }
    val selectedComment = remember { mutableStateOf<ReplyModel?>(null) }

    // ViewModel의 LiveData를 observe하여 실제 댓글 목록 사용
    val tripComments by myPostsViewModel.myAllReplys.collectAsState()


    // 현재 로그인한 사용자 정보 가져오기 (안전한 null 체크)
    val carryOnApplication = context.applicationContext as? CarryOnApplication
    val userId = try {
        carryOnApplication?.loginUserModel?.userId ?: "guest"
    } catch (e: UninitializedPropertyAccessException) {
        "guest"
    }

    // 화면이 구성될 때 댓글을 불러옴
    LaunchedEffect(userId) {
        myPostsViewModel.getAllReplysByUserId(userId)
    }


    Column(modifier = Modifier
        .fillMaxSize()
        .padding(5.dp)) {
        // ✅ 댓글 리스트 출력
        LikeLionMyCommentList(
            commentList = tripComments,
            onDeleteConfirmed = { comment ->

                myPostsViewModel.deleteReplyByReplyDocId(comment.replyDocumentId,comment.userId)
                selectedComment.value = comment
                showDialog.value = true
            }
        )
    }
}

//
//// ✅ 미리보기
//@Preview(showBackground = true)
//@Composable
//fun PreviewMyPostsScreen() {
//
//    val navController = rememberNavController() // ✅ 미리보기용 NavController 생성
//    MyPostsScreen(navController = navController)
//}
