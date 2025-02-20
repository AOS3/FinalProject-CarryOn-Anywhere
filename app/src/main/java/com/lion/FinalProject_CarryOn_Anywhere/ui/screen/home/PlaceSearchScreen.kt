package com.lion.FinalProject_CarryOn_Anywhere.ui.screen.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionDivider
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionPlaceSearchList
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionSearchTopAppBar
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionSearchTopAppBarTextFieldEndIconMode
import com.lion.FinalProject_CarryOn_Anywhere.component.PlaceSearchListItem
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.ScreenName
import com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.home.PlaceSearchViewModel
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun PlaceSearchScreen(
    navController: NavController,
    placeSearchViewModel: PlaceSearchViewModel = hiltViewModel()
) {

    // 키보드 컨트롤러
    val keyboardController = LocalSoftwareKeyboardController.current
    val listState = rememberLazyListState()

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

    // 검색어 입력 전
    //val isSearchEmpty = placeSearchViewModel.searchValue.value.isEmpty()

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

            // 검색 리스트
            LikeLionPlaceSearchList(
                dataList = placeSearchViewModel.placeSearchList.collectAsState().value.toMutableList(),
                listState = listState,
                isLoading = placeSearchViewModel.isLoading.collectAsState().value,
                rowComposable = { place ->
                    PlaceSearchListItem(
                        place = place,
                        icon = if (placeSearchViewModel.isFavoriteEnable.value)
                            Icons.Filled.Favorite
                        else
                            Icons.Filled.FavoriteBorder,
                        iconColor = Color(0xFFFF5255),
                        iconBackColor = Color.Transparent,
                        iconButtonOnClick = {
                            placeSearchViewModel.toggleFavorite()
                        }
                    )
                },
                onRowClick = { place ->
                    val contentId = (place as Map<String, Any>)["contentid"] as String
                    navController.navigate("${ScreenName.PLACE_INFO_SCREEN.name}/$contentId")
                }
            )
        }

    }
}