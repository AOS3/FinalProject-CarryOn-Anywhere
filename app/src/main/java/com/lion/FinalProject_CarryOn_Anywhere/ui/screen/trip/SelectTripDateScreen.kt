package com.lion.FinalProject_CarryOn_Anywhere.ui.screen.trip

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lion.FinalProject_CarryOn_Anywhere.R
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionFilledButton
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionTopAppBar
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.GrayColor
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.SubColor
import com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.trip.TripInfoViewModel
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectTripDateScreen(
    tripInfoViewModel: TripInfoViewModel = hiltViewModel(),
    tripDocumentId: String?
) {
    if (!tripDocumentId.isNullOrEmpty()) {
        tripInfoViewModel.gettingTripData(tripDocumentId)
    }

    val calendar = Calendar.getInstance()

    // 현재 월의 시작과 끝 설정
    val startOfMonthMillis = calendar.apply { set(Calendar.DAY_OF_MONTH, 1) }.timeInMillis

    // `DateRangePicker`에서 현재 월까지만 선택 가능하도록 설정
    val dateRangePickerState = rememberDateRangePickerState(
        initialDisplayedMonthMillis = startOfMonthMillis, // 현재 월부터 시작
        initialSelectedStartDateMillis = System.currentTimeMillis(), // 출발 날짜 초기화
    )

    // 선택한 날짜가 변경될 때 `ViewModel` 업데이트
    LaunchedEffect(dateRangePickerState.selectedStartDateMillis, dateRangePickerState.selectedEndDateMillis) {
        tripInfoViewModel.startDate.value = dateRangePickerState.selectedStartDateMillis
        tripInfoViewModel.endDate.value = dateRangePickerState.selectedEndDateMillis
        tripInfoViewModel.updateFormattedDates()
    }

    Scaffold(
        topBar = {
            LikeLionTopAppBar(
                title = "여행 일정 선택",
                navigationIconImage = ImageVector.vectorResource(R.drawable.arrow_back_24px),
                navigationIconOnClick = {
                    tripInfoViewModel.tripDateNavigationOnClick(tripDocumentId ?: "")
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(it)
                .padding(horizontal = 20.dp)
        ) {
            Text(
                text = "여행 할 날짜를 선택해주세요.",
                style = MaterialTheme.typography.bodyLarge,
                color = GrayColor,
                modifier = Modifier.padding(top = 50.dp)
            )

            // `DateRangePicker`에서 현재 월까지만 선택 가능하도록 설정
            DateRangePicker(
                title = { Text("") },
                headline = {
                    Text(
                        text = if (tripInfoViewModel.endDate.value == null || tripInfoViewModel.formattedStartDate == tripInfoViewModel.formattedEndDate) {
                            tripInfoViewModel.formattedStartDate.value
                        } else {
                            "${tripInfoViewModel.formattedStartDate.value} ~ ${tripInfoViewModel.formattedEndDate.value}"
                        },
                        textAlign = TextAlign.Start,
                        modifier = Modifier.padding(bottom = 15.dp)
                    )
                },
                showModeToggle = false,
                modifier = Modifier.weight(1f).padding(bottom = 10.dp),
                state = dateRangePickerState,
                colors = DatePickerDefaults.colors(
                    containerColor = Color.White,
                    selectedDayContentColor = Color.White,
                    selectedDayContainerColor = SubColor,
                    headlineContentColor = Color.Black,
                    todayContentColor = Color.Black,
                    todayDateBorderColor = Color.Black,
                    dayInSelectionRangeContentColor = Color.White,
                    dayInSelectionRangeContainerColor = Color.LightGray
                )
            )

            if (tripDocumentId.isNullOrEmpty()) {
                // 새 여행 등록
                LikeLionFilledButton(
                    text = if (tripInfoViewModel.endDate.value == null || tripInfoViewModel.formattedStartDate.value == tripInfoViewModel.formattedEndDate.value) {
                        "${tripInfoViewModel.formattedStartDate.value} 등록완료"
                    } else {
                        "${tripInfoViewModel.formattedStartDate.value} ~ ${tripInfoViewModel.formattedEndDate.value} 등록완료"
                    },
                    onClick = {
                        tripInfoViewModel.completeDateOnClick()
                    },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 5.dp),
                    cornerRadius = 5
                )
            } else {
                // 기존 여행 일정 수정
                LikeLionFilledButton(
                    text = if (tripInfoViewModel.endDate.value == null || tripInfoViewModel.formattedStartDate.value == tripInfoViewModel.formattedEndDate.value) {
                        "${tripInfoViewModel.formattedStartDate.value} 수정완료"
                    } else {
                        "${tripInfoViewModel.formattedStartDate.value} ~ ${tripInfoViewModel.formattedEndDate.value} 등록완료"
                    },
                    onClick = {
                        tripInfoViewModel.updateDateOnClick(tripDocumentId)
                    },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 5.dp),
                    cornerRadius = 5
                )
            }
        }
    }
}