package com.lion.FinalProject_CarryOn_Anywhere.ui.screen.mylike

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionDivider
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionEmptyView
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionFilterChip
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionMyLikeItem
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionMyTripPlanItem
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionTopAppBar
import com.lion.FinalProject_CarryOn_Anywhere.data.server.model.myposts.LocationModel
import com.lion.FinalProject_CarryOn_Anywhere.data.server.model.myposts.TripPlanModel
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.SubColor

@Composable
fun MyLikeScreen(navController: NavController) {

    // 샘플 일정 데이터 (실제 데이터는 ViewModel 또는 Repository에서 가져오기)
    var likeList by remember {
        mutableStateOf(
            listOf(
                // model 연결
                LocationModel("경복궁","서울특별시","인문",true),
                LocationModel("강릉 카페거리", "강원도", "음식", true),
                LocationModel("동대문 쇼핑몰", "서울", "쇼핑", true),
                LocationModel("라마다프라자호텔 제주도", "제주도", "숙박", true),
                LocationModel("롯데월드 어드벤처", "서울", "레포츠", true),

            )
        )
    }

    val chipItems = listOf("전체","자연", "인문", "레포츠", "쇼핑", "음식", "숙박","추천코스")
    val scrollState = rememberScrollState()
    val selectedChip = remember { mutableStateOf(chipItems[0]) }

    // 선택된 태그에 따라 필터링된 게시글 목록 생성
    val filteredPosts = if (selectedChip.value == "전체") {
        likeList // 전체 글 보기
    } else {
        likeList.filter { it.locationCategory == selectedChip.value }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // ✅ 상단 앱바
        LikeLionTopAppBar(
            title = "나의 찜",
            backColor = Color.White,
            navigationIconImage = null, // 네비게이션 아이콘 없음
            scrollValue = 0,
            navigationIconOnClick = {},
            menuItems = {},
            isTitleRightAligned = false,
            textOnClick = {}
        )

        // ✅ 카테고리 필터
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
                        selectedChip.value = text // ✅ 선택된 태그 변경
                    },
                    onDeleteButtonClicked = null
                )
            }
        }

        // ✅ 필터링된 데이터가 없을 경우 빈 화면 표시
        if (filteredPosts.isEmpty()) {
            LikeLionEmptyView(message = "선택한 태그에 해당하는 여행지가 없습니다.")
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
//                    .padding(bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()),
//                verticalArrangement = Arrangement.spacedBy(5.dp),
//                contentPadding = PaddingValues(bottom = 5.dp)
            ) {
                items(filteredPosts) { location ->
                    LikeLionMyLikeItem(
                        title = location.locationName,
                        location = location.locationArea,
                        category = location.locationCategory,
                        isFavorite = location.locationLike,
                        onFavoriteClick = {
                            likeList = likeList.map {
                                if (it.locationName == location.locationName) {
                                    it.copy(locationLike = !it.locationLike)
                                } else {
                                    it
                                }
                            }
                        },
                        onItemClick = {
                            // TODO: 상세 페이지 이동 기능 추가
                        }
                    )
                    LikeLionDivider(
                        modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 0.dp),
                        color = Color.LightGray,
                        thickness = 1.dp
                    )
                }
            }
        }
    }
}


// ✅ 미리보기 추가
@Preview(showBackground = true)
@Composable
fun PreviewMyLikeScreen() {
    val navController = rememberNavController() // ✅ 미리보기용 NavController
    MyLikeScreen(navController = navController)
}
