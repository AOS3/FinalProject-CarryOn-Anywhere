package com.lion.FinalProject_CarryOn_Anywhere.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.lion.FinalProject_CarryOn_Anywhere.R
import com.lion.FinalProject_CarryOn_Anywhere.component.AutoScrollingBanner
import com.lion.FinalProject_CarryOn_Anywhere.component.BestTripReviewCard
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionFilledButton
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionIconButton
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionTopAppBar
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.MainColor
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.SubColor
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.Typography
import com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.home.MainViewModel

@Composable
fun MainScreen(
    //windowInsetsController: WindowInsetsControllerCompat,
    mainViewModel: MainViewModel = hiltViewModel()
) {

    // 시스템 바 표시 여부
    // windowInsetsController가 변경될 때 실행
//    LaunchedEffect(windowInsetsController) {
//        windowInsetsController?.show(WindowInsetsCompat.Type.systemBars())
//    }
//    // 화면이 처음 생성될 때 (최초 1회만 실행)
//    LaunchedEffect(Unit) {
//        windowInsetsController?.show(WindowInsetsCompat.Type.systemBars())
//    }

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
                bannerImages = listOf(
                    R.drawable.banner1,
                    R.drawable.banner2,
                    R.drawable.banner3
                ),
                cornerRadius = 16.dp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 10.dp
                    ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                LikeLionFilledButton(
                    text = "내 일정보기",
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    paddingTop = 10.dp,
                    onClick = {

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
                        mainViewModel.buttonMainAddTrip()
                    },
                    icon = painterResource(id = R.drawable.add_event),
                    cornerRadius = 5,
                    containerColor = MainColor,
                    buttonHeight = 90.dp,
                )

            }

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "\uFE0F 인기 여행후기",
                color = SubColor,
                style = Typography.headlineMedium,
                modifier = Modifier
                    .padding(start = 20.dp)
            )

            BestTripReviewCard(
                image = painterResource(id = R.drawable.sample_tripreview),
                title = "즐거웠던 3박 4일 제주 여행",
                writer = "joker911",
                content = "캐리온으로 여행 계획을 짜고 다녀왔던 제주 여행 후기를 들고왔습니다 ~",
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            )


        }

    }
}