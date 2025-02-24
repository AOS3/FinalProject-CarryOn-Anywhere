package com.lion.FinalProject_CarryOn_Anywhere.ui.screen.trip

import android.util.Log
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionFilledButton
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionPlaceListItem
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionSearchTopAppBar
import com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.trip.TripInfoViewModel
import com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.trip.TripSearchPlaceViewModel
import kotlinx.coroutines.delay

@Composable
fun TripSearchPlaceScreen(
    tripSearchPlaceViewModel: TripSearchPlaceViewModel = hiltViewModel(),
    selectedDay: String,
    tripDocumentId: String,
    regionCodes: List<String>,
    subRegionCodes: List<String>
) {

    // 키보드 관리
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        delay(300)
        focusRequester.requestFocus()
        keyboardController?.show()
    }

    LaunchedEffect(Unit) {
        tripSearchPlaceViewModel.dayVal.value = selectedDay
        tripSearchPlaceViewModel.tripDocumentIdVal.value = tripDocumentId
        tripSearchPlaceViewModel.regionCodesParam.value = regionCodes.joinToString(",")
        tripSearchPlaceViewModel.subRegionCodesParam.value = subRegionCodes.joinToString(",")
    }

    LaunchedEffect(regionCodes, subRegionCodes) {
        tripSearchPlaceViewModel.fetchPlaces(regionCodes, subRegionCodes)
    }

    LaunchedEffect(tripSearchPlaceViewModel.searchTextFieldValue.value) {
        tripSearchPlaceViewModel.filterPlaces()
    }

    Scaffold(
        topBar = {
            LikeLionSearchTopAppBar(
                textFieldValue = tripSearchPlaceViewModel.searchTextFieldValue,
                onSearchTextChange = { tripSearchPlaceViewModel.searchTextFieldValue.value = it },
                onSearchClick = {
                    tripSearchPlaceViewModel.filterPlaces()
                },
                onBackClick = {
                    tripSearchPlaceViewModel.tripSearchNavigationOnClick(tripDocumentId)
                },
                focusRequester = focusRequester
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
            if (tripSearchPlaceViewModel.filteredPlaces.isEmpty()) {
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
                            tripSearchPlaceViewModel.requestPlaceOnClick()
                        },
                        modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
                        cornerRadius = 5,
                    )
                }
            } else {
                LazyColumn {
                    items(tripSearchPlaceViewModel.filteredPlaces) { place ->
                        LikeLionPlaceListItem(
                            imageUrl = place.firstimage ?: "",
                            title = place.title ?: "알 수 없는 장소",
                            subtitle = place.addr1 ?: "주소 없음",
                            location = place.addr2 ?: "",
                            onSelectClick = {
                                val placeMap = tripSearchPlaceViewModel.toPlaceMap(place) // 변환 후 전달
                                tripSearchPlaceViewModel.addPlaceToDay(selectedDay, placeMap, tripDocumentId)
                            }
                        )
                    }
                }
            }
        }
    }
}