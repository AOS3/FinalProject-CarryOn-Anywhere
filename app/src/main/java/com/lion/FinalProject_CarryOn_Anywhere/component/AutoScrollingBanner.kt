package com.lion.FinalProject_CarryOn_Anywhere.component

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.lion.FinalProject_CarryOn_Anywhere.R
import com.lion.FinalProject_CarryOn_Anywhere.data.server.model.BannerModel

// AutoScrolling 적용 안함
@Composable
fun AutoScrollingBanner(
    bannerList: List<BannerModel>,
    bannerHeight: Dp = 400.dp,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 16.dp,
    // 배너 클릭 시 이동
    onBannerClick: (String) -> Unit = {},
    // 로딩 상태
    isLoading: Boolean,
) {

    val pagerState = rememberPagerState(pageCount = {bannerList.size})

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(bannerHeight)
            .padding(
                horizontal = 20.dp,
                vertical = 10.dp
            )
    ) {
        if (isLoading) {
            // 로딩 중 스켈레톤 표시
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(bannerHeight)
                    .shimmerEffect(radius = 10.dp)
            )
        } else {
            Column(modifier = modifier.fillMaxWidth()) {
                // HorizontalPager로 배너 표시
                HorizontalPager(
                    //count = bannerImages.size,
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(bannerHeight)
                ) { page ->
                    val banner = bannerList[page]
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(cornerRadius))
                            .clickable { onBannerClick(banner.bannerDeepLink) }, // 클릭 이벤트 처리
                        contentAlignment = Alignment.Center,
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(banner.bannerImage),
                            contentDescription = "Banner $page",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.FillBounds,
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

//        CustomPagerIndicator(
//            pagerState = pagerState,
//            pageCount = bannerImages.size,
//            modifier = Modifier
//                .align(Alignment.BottomCenter)
//                .padding(bottom = 16.dp),
//            activeColor = Color.Gray,
//            inactiveColor = Color.LightGray,
//            activeIndicatorWidth = 16.dp, // 활성화된 인디케이터 너비
//            inactiveIndicatorWidth = 8.dp, // 비활성화된 인디케이터 너비
//            indicatorHeight = 8.dp,
//            spacing = 6.dp
//        )

    }
}

//@Composable
//fun CustomPagerIndicator(
//    pagerState: PagerState,
//    pageCount: Int,
//    modifier: Modifier = Modifier,
//    activeColor: Color = Color.Gray,
//    inactiveColor: Color = Color.LightGray,
//    activeIndicatorWidth: Dp = 24.dp, // 현재 페이지의 너비
//    inactiveIndicatorWidth: Dp = 8.dp, // 다른 페이지의 너비
//    indicatorHeight: Dp = 8.dp,
//    spacing: Dp = 4.dp
//) {
//    Row(
//        modifier = modifier,
//        horizontalArrangement = Arrangement.Center,
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        repeat(pageCount) { index ->
//            val isActive = pagerState.currentPage == index
//            Box(
//                modifier = Modifier
//                    .padding(horizontal = spacing / 2)
//                    .width(if (isActive) activeIndicatorWidth else inactiveIndicatorWidth)
//                    .height(indicatorHeight)
//                    .clip(CircleShape)
//                    .background(if (isActive) activeColor else inactiveColor)
//            )
//        }
//    }
//}