package com.lion.FinalProject_CarryOn_Anywhere.ui.screen.trip

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionFilledButton
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionPlaceListItem
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionSearchTopAppBar
import com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.trip.TripInfoViewModel

@Composable
fun TripSearchPlaceScreen(
    tripInfoViewModel: TripInfoViewModel = hiltViewModel(),
    selectedDay: String
) {
    LaunchedEffect(tripInfoViewModel.searchTextFieldValue.value) {
        tripInfoViewModel.filterPlaces()
    }

    Scaffold(
        topBar = {
            LikeLionSearchTopAppBar(
                textFieldValue = tripInfoViewModel.searchTextFieldValue,
                onSearchTextChange = { tripInfoViewModel.searchTextFieldValue.value = it },
                onSearchClick = {
                    tripInfoViewModel.filterPlaces()
                },
                onBackClick = {
                    tripInfoViewModel.tripSearchNavigationOnClick()
                }
            )
        }
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(it)
            .imePadding()
            .padding(horizontal = 20.dp)
        ) {
            if (tripInfoViewModel.filteredPlaces.isEmpty()) {
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
                            tripInfoViewModel.requestPlaceOnClick()
                        },
                        modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
                        cornerRadius = 5,
                    )
                }
            } else {
                // 검색 결과 리스트
                LazyColumn {
                    items(tripInfoViewModel.filteredPlaces) { place ->
                        LikeLionPlaceListItem(
                            imageUrl = place.imageUrl,
                            title = place.title,
                            subtitle = place.subtitle,
                            location = place.location,
                            onSelectClick = {
                                tripInfoViewModel.addPlaceToDay(selectedDay, place) // selectedDay를 인자로 전달
                            }
                        )
                    }
                }
            }
        }
    }
}

// 장소 데이터 클래스
data class Place(
    val imageUrl: String,
    val title: String,
    val subtitle: String,
    val location: String,
    val latitude: Double,
    val longitude: Double,
)