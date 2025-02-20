package com.lion.FinalProject_CarryOn_Anywhere.ui.screen.trip

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.IosShare
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState
import com.lion.FinalProject_CarryOn_Anywhere.R
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionAddPlaceItem
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionAlertDialog
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionBottomSheet
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionFilledButton
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionGoogleMap
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionIconButton
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionTopAppBar
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.GrayColor
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.MainColor
import com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.trip.TripInfoViewModel

@Composable
fun AddTripPlanScreen(
    tripInfoViewModel: TripInfoViewModel = hiltViewModel(),
    tripDocumentId: String
) {
    if (!tripDocumentId.isNullOrEmpty()) {
        tripInfoViewModel.gettingTripData(tripDocumentId)
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(tripInfoViewModel.selectedPlaceLocation.value, 10f)
    }

// selectedPlaceLocation 값이 변경되면 지도 위치 업데이트
    LaunchedEffect(tripInfoViewModel.selectedPlaceLocation.value) {
        cameraPositionState.position = CameraPosition.fromLatLngZoom(tripInfoViewModel.selectedPlaceLocation.value, 10f)
    }

    val selectedDayPlaces = tripInfoViewModel.placesByDay[tripInfoViewModel.selectedDay.value]
        ?.mapNotNull { place ->
            val placeLat = (place["mapy"] as? String)?.toDoubleOrNull()
            val placeLng = (place["mapx"] as? String)?.toDoubleOrNull()
            if (placeLat != null && placeLng != null) LatLng(placeLat, placeLng) else null
        } ?: emptyList()

    // 여행 날짜 목록 업데이트
    LaunchedEffect(tripInfoViewModel.startDate.value, tripInfoViewModel.endDate.value) {
        tripInfoViewModel.updateFormattedDates()
        tripInfoViewModel.updateTripDays()
    }

    Scaffold(
        topBar = {
            LikeLionTopAppBar(
                navigationIconImage = ImageVector.vectorResource(R.drawable.arrow_back_24px),
                navigationIconOnClick = {
                    tripInfoViewModel.addPlanNavigationOnClick()
                },
                menuItems = {
                    LikeLionIconButton(
                        icon = Icons.Filled.IosShare,
                        iconButtonOnClick = {

                        }
                    )

                    LikeLionIconButton(
                        icon = ImageVector.vectorResource(R.drawable.delete_24px),
                        iconButtonOnClick = {
                            tripInfoViewModel.deletePlanDialogState.value = true
                        }
                    )
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
                .padding(it)
                .padding(horizontal = 20.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp, bottom = 15.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = tripInfoViewModel.currentTripName.value,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(end = 10.dp)
                )

                Text(
                    text = "편집",
                    style = MaterialTheme.typography.bodyLarge,
                    color = GrayColor,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable {
                        tripInfoViewModel.showBottomSheet.value = true
                    }
                )
            }

            Text(
                text = if (tripInfoViewModel.formattedEndDate.value == "") {
                    tripInfoViewModel.formattedStartDate.value
                } else {
                    "${tripInfoViewModel.formattedStartDate.value} ~ ${tripInfoViewModel.formattedEndDate.value}"
                },
                style = MaterialTheme.typography.bodySmall,
                color = GrayColor,
                modifier = Modifier.padding(bottom = 15.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 15.dp)
            ) {
                Text(
                    text = tripInfoViewModel.selectRegion.joinToString(" / "),
                    style = MaterialTheme.typography.bodySmall,
                    color = GrayColor,
                    maxLines = Int.MAX_VALUE, // 최대 줄 수 제한 없음
                    overflow = TextOverflow.Clip // 넘치는 텍스트 잘리지 않게
                )
            }

            // Google Map을 감싸는 Box 추가
            Column(
                modifier = Modifier
                    .height(300.dp)
            ) {
                LikeLionGoogleMap(
                    cameraPositionState = cameraPositionState,
                    modifier = Modifier.fillMaxSize().padding(bottom = 10.dp),
                    onMapClick = {
                        tripInfoViewModel.mapOnClick(tripDocumentId)
                    },
                    selectedPlaces = selectedDayPlaces,
                    isAddTripPlan = true
                )
            }

            Column(
                modifier = Modifier
                    .verticalScroll(state = rememberScrollState())
            ) {
                // 최소한 하나의 날짜는 표시하도록 보장
                val tripDays = if (tripInfoViewModel.tripDays.isEmpty()) {
                    listOf(tripInfoViewModel.formattedStartDate.value) // 최소 출발 날짜 1개 유지
                } else {
                    tripInfoViewModel.tripDays
                }

                // 여행 일정 리스트 생성
                tripDays.forEachIndexed { index, day ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(top = 10.dp, bottom = 20.dp)
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Day X 표시
                                Text(
                                    text = "day${index + 1}",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = Color.Black,
                                    modifier = Modifier.padding(end = 10.dp)
                                )

                                // 날짜 표시
                                Text(
                                    text = day,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = GrayColor,
                                )
                            }

                            // 장소가 있을 때만 "편집" 버튼을 표시
                            if (tripInfoViewModel.placesByDay[day]?.isNotEmpty() == true) {
                                Row(
                                    modifier = Modifier.weight(1f),
                                    horizontalArrangement = Arrangement.End,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "편집",
                                        textDecoration = TextDecoration.Underline,
                                        style = MaterialTheme.typography.bodyLarge,
                                        modifier = Modifier
                                            .clickable {
                                                tripInfoViewModel.editPlaceOnClick(day, index, tripDocumentId)
                                            },
                                        textAlign = TextAlign.End,
                                        color = GrayColor,
                                    )
                                }
                            }
                        }

                        tripInfoViewModel.placesByDay[day]?.let { places ->
                            places.forEachIndexed { index, place ->
                                val distanceToNext = if (index < places.lastIndex) {
                                    // 거리 계산 시에도 같은 방식 적용
                                    tripInfoViewModel.calculateDistance(
                                        LatLng(
                                            (place["mapy"] as? String)?.toDoubleOrNull() ?: 0.0,
                                            (place["mapx"] as? String)?.toDoubleOrNull() ?: 0.0
                                        ),
                                        LatLng(
                                            (places[index + 1]["mapy"] as? String)?.toDoubleOrNull() ?: 0.0,
                                            (places[index + 1]["mapx"] as? String)?.toDoubleOrNull() ?: 0.0
                                        )
                                    )
                                } else {
                                    null // 마지막 장소는 거리 표시 X
                                }

                                LikeLionAddPlaceItem(
                                    index = index,
                                    lastIndex = places.lastIndex,
                                    place = place,
                                    distanceToNext = distanceToNext // 거리 정보 전달
                                )
                            }
                        }

                        // 장소 추가 버튼
                        LikeLionFilledButton(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalPadding = 0.dp,
                            border = BorderStroke(1.dp, color = Color.LightGray),
                            text = "장소 추가",
                            containerColor = Color.White,
                            contentColor = Color.Black,
                            cornerRadius = 5,
                            onClick = {
                                tripInfoViewModel.plusPlaceOnClick(day, tripDocumentId) // 해당 날짜를 전달
                            }
                        )
                    }
                }
            }

            if (tripInfoViewModel.showBottomSheet.value) {
                LikeLionBottomSheet(
                    onDismissRequest = { tripInfoViewModel.showBottomSheet.value = false },
                    text1 = "여행 제목 수정",
                    text1OnClick = {
                        tripInfoViewModel.editTripNameDialogState.value = true
                        tripInfoViewModel.showBottomSheet.value = false
                    },
                    text2 = "여행 날짜 수정",
                    text2OnClick = {
                        tripInfoViewModel.showBottomSheet.value = false
                        tripInfoViewModel.dialogEditDateOnClick(tripDocumentId)
                    }
                )
            }

            // 일정 삭제 시 띄우는 다이얼로그
            LikeLionAlertDialog(
                showDialogState = tripInfoViewModel.deletePlanDialogState,
                confirmButtonTitle = "삭제",
                confirmButtonModifier = Modifier
                    .weight(1f)
                    .padding(start = 10.dp),
                confirmButtonOnClick = {
                    tripInfoViewModel.deletePlanOnClick()
                },
                dismissButtonTitle = "취소",
                dismissContainerColor = Color.Transparent,
                dismissButtonModifier = Modifier
                    .weight(1f)
                    .padding(end = 10.dp),
                dismissBorder = BorderStroke(1.dp, MainColor),
                dismissButtonOnClick = {
                    tripInfoViewModel.deletePlanDialogState.value = false
                },
                title = "여행 삭제",
                titleModifier = Modifier.fillMaxWidth().padding(bottom = 5.dp),
                titleAlign = TextAlign.Center,
                text = "일정이 삭제되면 복구할 수 없습니다.",
                textModifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
            )

            // 여행 제목 수정 시 띄우는 다이얼로그
            LikeLionAlertDialog(
                showDialogState = tripInfoViewModel.editTripNameDialogState,
                confirmButtonTitle = "확인",
                confirmButtonModifier = Modifier
                    .weight(1f)
                    .padding(start = 10.dp),
                confirmButtonOnClick = {
                    if (tripInfoViewModel.editTripNameTextFieldValue.value.isNotBlank()) {
                        tripInfoViewModel.currentTripName.value = tripInfoViewModel.editTripNameTextFieldValue.value
                    }
                    tripInfoViewModel.editTripNameTextFieldValue.value = ""
                    tripInfoViewModel.editTripNameDialogState.value = false
                },
                dismissButtonTitle = "취소",
                dismissContainerColor = Color.Transparent,
                dismissButtonModifier = Modifier
                    .weight(1f)
                    .padding(end = 10.dp),
                dismissBorder = BorderStroke(1.dp, MainColor),
                dismissButtonOnClick = {
                    tripInfoViewModel.editTripNameDialogState.value = false
                },
                title = "여행 제목 수정",
                titleModifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
                titleAlign = TextAlign.Center,
                text = "일행의 여행 제목도 함께 수정됩니다.",
                textModifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
                textAlign = TextAlign.Center,
                isEditTripTitle = true,
                textFieldValue = tripInfoViewModel.editTripNameTextFieldValue
            )
        }
    }
}