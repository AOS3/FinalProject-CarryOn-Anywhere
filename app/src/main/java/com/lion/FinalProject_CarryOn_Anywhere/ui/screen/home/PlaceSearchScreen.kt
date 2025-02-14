package com.lion.FinalProject_CarryOn_Anywhere.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.hilt.navigation.compose.hiltViewModel
import com.lion.FinalProject_CarryOn_Anywhere.R
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionDivider
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionPlaceSearchList
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionSearchTopAppBar
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionSearchTopAppBarTextFieldEndIconMode
import com.lion.FinalProject_CarryOn_Anywhere.component.PlaceSearchListItem
import com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.home.PlaceSearchViewModel

@Composable
fun PlaceSearchScreen(placeSearchViewModel: PlaceSearchViewModel = hiltViewModel()) {

    // 키보드 컨트롤러
    val keyboardController = LocalSoftwareKeyboardController.current

    // 검색 화면 진입 시 자동으로 키보드 올리기
    LaunchedEffect(Unit) {
        keyboardController?.show()
    }

    // 검색어 입력 전
    val isSearchEmpty = placeSearchViewModel.searchValue.value.isEmpty()

    Scaffold(
        topBar = {
            // 검색바
            LikeLionSearchTopAppBar(
                textFieldValue = placeSearchViewModel.searchValue,
                onSearchTextChange = {
                    placeSearchViewModel.searchValue.value = it
                    placeSearchViewModel.performSearch()
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

            Row(

            ) {

            }

            // 검색 리스트
            LikeLionPlaceSearchList(
                dataList = placeSearchViewModel.placeSearchList,
                rowComposable = { place ->
                    // 검색 아이템
                    PlaceSearchListItem(
                        place = place as Map<String, Any>,
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
                onRowClick = {
                    // TODO : 상세 화면으로 이동
                }
            )
        }

    }
}