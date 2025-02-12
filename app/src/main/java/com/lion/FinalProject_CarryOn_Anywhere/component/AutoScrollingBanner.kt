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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lion.FinalProject_CarryOn_Anywhere.R

@Composable
fun AutoScrollingBanner(
    bannerImages: List<Any>,
    bannerHeight: Dp = 350.dp,
    modifier: Modifier = Modifier,
    onBannerClick: (Int) -> Unit = {}
) {
    val safeBannerImages = if (bannerImages.isEmpty()) {
        listOf(R.drawable.banner3, R.drawable.banner3, R.drawable.banner3) // 🔹 기본 더미 이미지 추가
    } else {
        bannerImages
    }

    val pagerState = rememberPagerState(pageCount = { safeBannerImages.size })

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(bannerHeight)
    ) {
        Column(modifier = modifier.fillMaxWidth()) {
            // HorizontalPager로 배너 표시
            HorizontalPager(
                //count = bannerImages.size,
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(bannerHeight)
            ) { page ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { onBannerClick(page) }, // 클릭 이벤트 처리
                    contentAlignment = Alignment.Center
                ) {
                    when (val item = bannerImages[page]) {
                        is Bitmap -> {
                            Image(
                                bitmap = item.asImageBitmap(),
                                contentDescription = "Banner $page",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.FillBounds
                            )
                        }

                        is Int -> { // Drawable 리소스 ID일 경우
                            Image(
                                painter = painterResource(id = item),
                                contentDescription = "Banner $page",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.FillBounds
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        CustomPagerIndicator(
            pagerState = pagerState,
            pageCount = bannerImages.size,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp),
            activeColor = Color.Gray,
            inactiveColor = Color.LightGray,
            activeIndicatorWidth = 16.dp, // 활성화된 인디케이터 너비
            inactiveIndicatorWidth = 8.dp, // 비활성화된 인디케이터 너비
            indicatorHeight = 8.dp,
            spacing = 6.dp
        )

    }
}

@Composable
fun CustomPagerIndicator(
    pagerState: PagerState,
    pageCount: Int,
    modifier: Modifier = Modifier,
    activeColor: Color = Color.Gray,
    inactiveColor: Color = Color.LightGray,
    activeIndicatorWidth: Dp = 24.dp, // 현재 페이지의 너비
    inactiveIndicatorWidth: Dp = 8.dp, // 다른 페이지의 너비
    indicatorHeight: Dp = 8.dp,
    spacing: Dp = 4.dp
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pageCount) { index ->
            val isActive = pagerState.currentPage == index
            Box(
                modifier = Modifier
                    .padding(horizontal = spacing / 2)
                    .width(if (isActive) activeIndicatorWidth else inactiveIndicatorWidth)
                    .height(indicatorHeight)
                    .clip(CircleShape)
                    .background(if (isActive) activeColor else inactiveColor)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAutoScrollingBanner() {
    AutoScrollingBanner(
        bannerImages = listOf(
            R.drawable.banner3,
            R.drawable.banner3,
            R.drawable.banner3
        )
    )
}