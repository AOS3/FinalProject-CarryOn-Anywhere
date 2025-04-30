package com.lion.FinalProject_CarryOn_Anywhere.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.rememberAsyncImagePainter
import com.lion.FinalProject_CarryOn_Anywhere.R
import com.lion.FinalProject_CarryOn_Anywhere.component.AutoScrollingBanner
import com.lion.FinalProject_CarryOn_Anywhere.component.BestTripReviewCard
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionFilledButton
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionIconButton
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionTopAppBar
import com.lion.FinalProject_CarryOn_Anywhere.component.shimmerEffect
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.ScreenName
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.MainColor
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.SubColor
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.Typography
import com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.home.MainViewModel

@Composable
fun MainScreen(
    mainViewModel: MainViewModel = hiltViewModel()
) {
    // 인기 후기
    val topTripReviews by mainViewModel.topTripReviews.observeAsState(emptyList())

    // 배너 리스트
    val bannerList by mainViewModel.bannerList.observeAsState(emptyList())
    // 배너 로딩
    val isLoading by mainViewModel.isBannerLoading.collectAsState()

    // 로그인 여부
    val isLoggedIn by mainViewModel.isLoggedIn.collectAsState()
    val showDialog = remember { mutableStateOf(false) }

    Scaffold(
        //contentWindowInsets = WindowInsets.systemBars.only(WindowInsetsSides.Bottom),
        topBar = {
            LikeLionTopAppBar(
                title = "Carry On",
                backColor = Color.White,
                navigationIconImage = null,
                navigationIconOnClick = {},
                menuItems = {
                    LikeLionIconButton(
                        icon = ImageVector.vectorResource(id = R.drawable.search_24px),
                        color = Color.Transparent,
                        iconBackColor = Color.Transparent,
                        iconButtonOnClick = {
                            mainViewModel.searchOnClick()
                        },
                        borderNull = true,
                    )
                }
            )
        }
    ) { paddingValues ->
        //val context = LocalContext.current
        // 현재 디바이스 전체 화면 높이를 가져온다.
        // 재구성 시에도 screenHeightPx 값이 유지되도록 한다.
//        val screenHeightPx = remember {
//            context.resources.displayMetrics.heightPixels.toFloat()
//        }

        Column(
            modifier = Modifier
                // 상단바, 하단바 공간을 고려한 패딩 적용 (TopBar, BottomBar 겹치는 문제 해결)
                .padding(paddingValues)
                .fillMaxSize()
                .background(Color.White)
                .verticalScroll(rememberScrollState())
        ) {
            // 메인 배너 (지역 축제 홍보)
            AutoScrollingBanner(
                bannerList = bannerList,
                cornerRadius = 16.dp,
                isLoading = isLoading,
                onBannerClick = {deepLink ->
                    mainViewModel.navigateToDeepLink(deepLink)}
            )

            Spacer(modifier = Modifier.height(10.dp))

            // 일정 버튼 로딩 적용
            if (isLoading) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                ) {
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .height(90.dp)
                            .shimmerEffect(radius = 10.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .height(90.dp)
                            .shimmerEffect(radius = 10.dp)
                    )
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    LikeLionFilledButton(
                        text = "내 일정보기",
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        paddingTop = 10.dp,
                        onClick = {
                            mainViewModel.buttonMainUserTripList {
                                showDialog.value = true
                            }
                        },
                        icon = painterResource(id = R.drawable.calendar),
                        cornerRadius = 5,
                        containerColor = MainColor,
                        buttonHeight = 90.dp,
                    )

                    LikeLionFilledButton(
                        text = "일정 등록",
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        paddingTop = 10.dp,
                        onClick = {
                            mainViewModel.buttonMainAddTrip {
                                showDialog.value = true
                            }
                        },
                        icon = painterResource(id = R.drawable.add_event),
                        cornerRadius = 5,
                        containerColor = MainColor,
                        buttonHeight = 90.dp,
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))


            // 인기 여행 후기 타이틀 및 설명 텍스트
            if (isLoading) {
                Column(modifier = Modifier.padding(start = 20.dp)) {
                    Spacer(
                        modifier = Modifier
                            .width(200.dp)
                            .height(30.dp)
                            .shimmerEffect(radius = 10.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Spacer(
                        modifier = Modifier
                            .width(250.dp)
                            .height(20.dp)
                            .shimmerEffect(radius = 10.dp)
                    )
                }
            } else {
                Text(
                    text = "🔥 인기 여행 후기",
                    color = SubColor,
                    style = Typography.headlineMedium,
                    modifier = Modifier
                        .padding(start = 20.dp)
                )

                Text(
                    text = "✈️ 많은 여행자들이 좋아한 인기 여행 후기예요!🔥",
                    color = Color(0xFFADADAD),
                    style = Typography.bodyLarge,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .padding(start = 20.dp, top = 10.dp)
                )
            }

            // Top5 여행 후기
            if (isLoading) {
                Column(
                    modifier = Modifier.padding(horizontal = 15.dp, vertical = 15.dp)
                ) {
                    repeat(3) {
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                                .padding(vertical = 10.dp)
                                .shimmerEffect(radius = 10.dp)
                        )
                    }
                }
            } else {
                Column(
                    modifier = Modifier.padding(horizontal = 15.dp, vertical = 15.dp)
                ) {
                    topTripReviews.forEach { (review, userId) ->
                        val firstImage = review.tripReviewImage.firstOrNull()
                        BestTripReviewCard(
                            image = if (firstImage != null) {
                                rememberAsyncImagePainter(firstImage)
                            } else {
                                painterResource(id = R.drawable.sample_tripreview)
                            },
                            title = review.tripReviewTitle,
                            writer = userId,
                            content = review.tripReviewContent,
                            modifier = Modifier
                                .padding(horizontal = 20.dp, vertical = 20.dp),
                            onClick = {
                                mainViewModel.showTripReviewDetailScreen(review.tripReviewDocumentId)
                            }
                        )
                    }
                }
            }
        }
    }

    // 🔹 로그인 유도 다이얼로그
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text("로그인이 필요합니다") },
            text = { Text("이 기능을 사용하려면 로그인해야 합니다.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDialog.value = false
                        mainViewModel.carryOnApplication.navHostController.navigate(ScreenName.LOGIN_SCREEN.name)
                    }
                ) {
                    Text("로그인하기")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDialog.value = false }
                ) {
                    Text("취소")
                }
            }
        )
    }
}