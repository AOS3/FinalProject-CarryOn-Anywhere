package com.lion.FinalProject_CarryOn_Anywhere.ui.screen.mylike

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionEmptyView
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionFilterChip
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionPlaceSearchList
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionTopAppBar
import com.lion.FinalProject_CarryOn_Anywhere.component.PlaceSearchListItem
import com.lion.FinalProject_CarryOn_Anywhere.data.api.TourAPI.TourApiHelper
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.ScreenName
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.GrayColor
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.MainColor
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.SubColor
import com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.mypage.MyLikePageViewModel

@Composable
fun MyLikeScreen(
    navController: NavController,
    myLikePageViewModel: MyLikePageViewModel = hiltViewModel()
) {

    // 칩 목록
    val chipItems = listOf("전체") + TourApiHelper.contentTypeMap.values.toList()

    val scrollState = rememberScrollState()
    // 스크롤 위치를 저장, List의 정보를 포함
    val listState = rememberLazyListState()

    // 선택한 카테고리 칩
    val selectedChip by myLikePageViewModel.selectedCategory.collectAsState()

    // 사용자 찜 목록
    val userLikeList by myLikePageViewModel.userLikeList.collectAsState()
    // 장소 정보
    val placeInfo by myLikePageViewModel.placeInfo.collectAsState()
    // 필터링 된 장소
    val filteredPlace = if (selectedChip == "전체") placeInfo else myLikePageViewModel.filteredLikeList.collectAsState().value

    // 로딩 상태
    val isLoading = myLikePageViewModel.isLoading.collectAsState().value

    val context = LocalContext.current

    // 찜 목록 불러오기
    LaunchedEffect(Unit) {
        myLikePageViewModel.gettingUserLikeList()
    }

    LaunchedEffect(placeInfo) {
        Log.d("MY_LIKE_DEBUG", "화면에서 받아온 placeInfo: $placeInfo") // ✅ UI 반영 확인
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // ✅ 상단 앱바
        LikeLionTopAppBar(
            title = "내 장소",
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
                .horizontalScroll(scrollState)
                .padding(10.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            chipItems.forEach { chipText ->
                LikeLionFilterChip(
                    text = chipText,
                    selected = selectedChip == chipText,
                    selectedColor = SubColor,
                    unselectedColor = Color.White,
                    borderColor = Color(0xFFD8D0D0),
                    chipTextStyle = TextStyle(
                        color = if (selectedChip == chipText) Color.White else SubColor,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    ),
                    selectedTextColor = Color.White,
                    unselectedTextColor = Color(0xFFADADAD),
                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp),
                    chipModifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth(),
                    cornerRadius = 10,
                    onChipClicked = { text, _ ->
                        val typeId = TourApiHelper.contentTypeMap.entries.find { it.value == text }?.key ?: "전체"
                        myLikePageViewModel.filterByCategory(typeId)
                    },
                    onDeleteButtonClicked = null,
                )
            }
        }

        // 로딩 중일 때
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                color = MainColor,
            )
        } else {
            // 필터링 된 데이터가 없을 경우
            if (filteredPlace.isEmpty()) {
                LikeLionEmptyView(
                    message = "선택한 태그에 해당하는 장소가 없습니다.",
                )
            } else {
                // 있을 경우 찜 목록 표시
                // 검색 리스트
                LikeLionPlaceSearchList(
                    dataList = filteredPlace.toMutableList(),
                    isLoading = isLoading,
                    listState = listState,
                    rowComposable = { place ->
                        val contentId = place["contentid"].toString()
                        val contentTypeId = place["contenttypeid"].toString()
                        val isLiked = userLikeList.any { it["contentid"] == contentId }

                        PlaceSearchListItem(
                            place = place,
                            iconColor = Color(0xFFFF5255),
                            iconBackColor = Color.Transparent,
                            isLiked = isLiked,
                            onLikeClick = { id, typeId ->
                                myLikePageViewModel.toggleFavorite(id, typeId) { isAdded ->
                                    Toast.makeText(
                                        context,
                                        if (isAdded) "내 장소에 추가되었습니다" else "내 장소에서 삭제되었습니다",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        )
                    },
                    onRowClick = { place ->
                        try {
                            val mapPlace = place as? Map<String, Any>
                                ?: throw IllegalArgumentException("place is not a Map")
                            val contentId = place["contentid"]
                            val contentTypeId = mapPlace["contenttypeid"] as? String ?: ""
                            Log.d(
                                "PLACE_DEBUG",
                                "contentId: $contentId, contentTypeId: $contentTypeId"
                            )

                            navController.navigate("${ScreenName.PLACE_INFO_SCREEN.name}/$contentId/$contentTypeId")
                        } catch (e: Exception) {
                            Log.e("PLACE_DEBUG", "데이터 변환 오류: ${e.message}")
                        }
                    },
                )
            }

        }
    }
}


//// ✅ 미리보기 추가
//@Preview(showBackground = true)
//@Composable
//fun PreviewMyLikeScreen() {
//    val navController = rememberNavController() // ✅ 미리보기용 NavController
//    MyLikeScreen(navController = navController)
//}
