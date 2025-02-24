package com.lion.FinalProject_CarryOn_Anywhere.ui.screen.home

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionDivider
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionFilledButton
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionPlaceSearchList
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionSearchTopAppBar
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionSearchTopAppBarTextFieldEndIconMode
import com.lion.FinalProject_CarryOn_Anywhere.component.PlaceSearchListItem
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.ScreenName
import com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.home.PlaceSearchViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun PlaceSearchScreen(
    navController: NavController,
    placeSearchViewModel: PlaceSearchViewModel = hiltViewModel()
) {

    // 키보드 컨트롤러
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    // 스크롤 위치를 저장, List의 정보를 포함
    val listState = rememberLazyListState()

    // 검색 결과
    val searchResults by placeSearchViewModel.placeSearchList.collectAsState()
    // 사용자 찜 목록
    val userLikeList by placeSearchViewModel.userLikeList.collectAsState()

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        delay(300)
        focusRequester.requestFocus()
        keyboardController?.show()
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex + listState.layoutInfo.visibleItemsInfo.size }
            .distinctUntilChanged()
            .collect { lastVisibleItemIndex ->
                val totalItemCount = listState.layoutInfo.totalItemsCount
                Log.d("SCROLL_EVENT", "스크롤 감지됨: 마지막 인덱스 = $lastVisibleItemIndex, 전체 개수 = $totalItemCount")

                if (totalItemCount > 0 && lastVisibleItemIndex >= totalItemCount - 3) {
                    placeSearchViewModel.fetchNextPage()
                }
            }
    }

    Scaffold(
        topBar = {
            // 검색바
            LikeLionSearchTopAppBar(
                textFieldValue = placeSearchViewModel.searchValue,
                onSearchTextChange = {
                    placeSearchViewModel.searchValue.value = it
                },
                onSearchClick = {
                    placeSearchViewModel.searchAndHideKeyboard(keyboardController)
                },
                onBackClick = {
                    placeSearchViewModel.navigationBackIconOnClick()
                },
                onTrailingIconClick = {
                    placeSearchViewModel.clearSearch()
                    keyboardController?.show()
                },
                trailingIconMode = LikeLionSearchTopAppBarTextFieldEndIconMode.TEXT,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Search
                ),
                focusRequester = focusRequester
            )
        }
    ) {paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Color.White)
        ) {
            LikeLionDivider(
                modifier = Modifier
                    .fillMaxWidth()
            )
            if(searchResults.isNotEmpty()) {
                // 검색 리스트
                LikeLionPlaceSearchList(
                    dataList = placeSearchViewModel.placeSearchList.collectAsState().value.toMutableList(),
                    listState = listState,
                    isLoading = placeSearchViewModel.isLoading.collectAsState().value,
                    rowComposable = { place ->
                        val contentId = place["contentid"].toString()
                        val isLiked = userLikeList.any { it["contentid"] == contentId }

                        PlaceSearchListItem(
                            place = place,
                            iconColor = Color(0xFFFF5255),
                            iconBackColor = Color.Transparent,
                            isLiked = isLiked,
                            onLikeClick = { id, typeId ->
                                placeSearchViewModel.toggleFavorite(id, typeId) { isAdded ->
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
                            val mapPlace = place as? Map<String, Any> ?: throw IllegalArgumentException("place is not a Map")
                            val contentId = place["contentid"]
                            val contentTypeId = mapPlace["contenttypeid"] as? String ?: ""
                            Log.d("PLACE_DEBUG", "contentId: $contentId, contentTypeId: $contentTypeId")

                            navController.navigate("${ScreenName.PLACE_INFO_SCREEN.name}/$contentId/$contentTypeId")
                        } catch (e: Exception) {
                            Log.e("PLACE_DEBUG", "데이터 변환 오류: ${e.message}")
                        }
                    }
                )
            } else {
                // 검색 결과 없을 때
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "찾으시는 장소가 없으신가요?",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(bottom = 20.dp),
                        color = Color.Black
                    )

                    Text(
                        text = "장소 등록을 요청하거나 직접 입력할 수 있어요.",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(bottom = 20.dp),
                        color = Color.Black
                    )

                    LikeLionFilledButton(
                        text = "장소 등록 요청하기",
                        onClick = {
                            placeSearchViewModel.requestPlaceOnClick()
                        },
                        modifier = Modifier
                            .width(300.dp)
                            .height(60.dp)
                            .padding(bottom = 20.dp),
                        cornerRadius = 5,
                    )
                }
            }
        }
    }
}