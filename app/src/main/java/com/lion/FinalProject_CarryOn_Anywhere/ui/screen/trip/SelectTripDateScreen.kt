package com.lion.FinalProject_CarryOn_Anywhere.ui.screen.trip

import android.util.Log
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
import com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.trip.AddTripInfoViewModel
import com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.trip.TripInfoViewModel
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectTripDateScreen(
    addTripInfoViewModel: AddTripInfoViewModel,
    tripDocumentId: String?
) {
    LaunchedEffect(tripDocumentId) {
        if (!tripDocumentId.isNullOrEmpty()) {
            try {
                Log.d("SelectTripDateScreen", "📡 서버에서 데이터 가져오기: tripDocumentId = $tripDocumentId")
                addTripInfoViewModel.gettingTripData(tripDocumentId)
            } catch (e: Exception) {
                Log.e("SelectTripDateScreen", "🚨 데이터 가져오기 실패: ${e.message}")
            }
        }
    }

    // ✅ 서버에서 데이터를 가져온 후 UI를 최신화
    LaunchedEffect(addTripInfoViewModel.serverStartDate.value, addTripInfoViewModel.serverEndDate.value) {
        if (addTripInfoViewModel.serverStartDate.value != null) {
            Log.d("SelectTripDateScreen", "✅ 서버 데이터 로드 완료: ${addTripInfoViewModel.serverStartDate.value} ~ ${addTripInfoViewModel.serverEndDate.value}")

            // 서버에서 받아온 날짜를 화면의 선택 날짜로 설정
            addTripInfoViewModel.startDate.value = addTripInfoViewModel.serverStartDate.value
            addTripInfoViewModel.endDate.value = addTripInfoViewModel.serverEndDate.value
            addTripInfoViewModel.updateFormattedDates()
        }
    }

    val calendar = Calendar.getInstance()
    val startOfMonthMillis = calendar.apply { set(Calendar.DAY_OF_MONTH, 1) }.timeInMillis

    // ✅ 서버에서 받아온 값이 변경될 때 `DateRangePickerState`를 강제로 다시 생성
    var dateRangePickerState by remember {
        mutableStateOf(
            DateRangePickerState(
                initialDisplayedMonthMillis = startOfMonthMillis,
                initialSelectedStartDateMillis = addTripInfoViewModel.serverStartDate.value ?: System.currentTimeMillis(),
                initialSelectedEndDateMillis = addTripInfoViewModel.serverEndDate.value,
                locale = Locale.KOREA
            )
        )
    }

    // ✅ 서버 값이 변경될 때 `DateRangePickerState`를 재생성
    LaunchedEffect(addTripInfoViewModel.serverStartDate.value, addTripInfoViewModel.serverEndDate.value) {
        dateRangePickerState = DateRangePickerState(
            initialDisplayedMonthMillis = startOfMonthMillis,
            initialSelectedStartDateMillis = addTripInfoViewModel.serverStartDate.value ?: System.currentTimeMillis(),
            initialSelectedEndDateMillis = addTripInfoViewModel.serverEndDate.value,
            locale = Locale.KOREA
        )
    }

    // ✅ 사용자가 날짜를 변경할 때 UI 상태 업데이트
    LaunchedEffect(dateRangePickerState.selectedStartDateMillis, dateRangePickerState.selectedEndDateMillis) {
        addTripInfoViewModel.startDate.value = dateRangePickerState.selectedStartDateMillis
        addTripInfoViewModel.endDate.value = dateRangePickerState.selectedEndDateMillis
        addTripInfoViewModel.updateFormattedDates()
    }

    Scaffold(
        topBar = {
            LikeLionTopAppBar(
                title = "여행 일정 선택",
                navigationIconImage = ImageVector.vectorResource(R.drawable.arrow_back_24px),
                navigationIconOnClick = {
                    addTripInfoViewModel.tripDateNavigationOnClick(tripDocumentId ?: "")
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

            DateRangePicker(
                title = { Text("") },
                headline = {
                    Text(
                        text = if (tripDocumentId.isNullOrEmpty()) {
                            if (addTripInfoViewModel.endDate.value == null ||
                                addTripInfoViewModel.formattedStartDate == addTripInfoViewModel.formattedEndDate) {
                                addTripInfoViewModel.formattedStartDate.value
                            } else {
                                "${addTripInfoViewModel.formattedStartDate.value} ~ ${addTripInfoViewModel.formattedEndDate.value}"
                            }
                        } else {
                            val startChanged = addTripInfoViewModel.formattedStartDate.value != addTripInfoViewModel.formattedServerStartDate.value
                            val endChanged = addTripInfoViewModel.formattedEndDate.value != addTripInfoViewModel.formattedServerEndDate.value

                            if (startChanged || endChanged) {
                                if (addTripInfoViewModel.endDate.value == null ||
                                    addTripInfoViewModel.formattedStartDate == addTripInfoViewModel.formattedEndDate) {
                                    addTripInfoViewModel.formattedStartDate.value
                                } else {
                                    "${addTripInfoViewModel.formattedStartDate.value} ~ ${addTripInfoViewModel.formattedEndDate.value}"
                                }
                            } else {
                                if (addTripInfoViewModel.serverEndDate.value == null ||
                                    addTripInfoViewModel.formattedServerStartDate == addTripInfoViewModel.formattedServerEndDate) {
                                    addTripInfoViewModel.formattedServerStartDate.value
                                } else {
                                    "${addTripInfoViewModel.formattedServerStartDate.value} ~ ${addTripInfoViewModel.formattedServerEndDate.value}"
                                }
                            }
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

            LikeLionFilledButton(
                text = if (addTripInfoViewModel.formattedEndDate.value.isNullOrEmpty()) {
                    "${addTripInfoViewModel.formattedStartDate.value} ${
                        if (tripDocumentId.isNullOrEmpty()) " 등록완료" else " 수정완료"
                    }"
                } else {
                    "${addTripInfoViewModel.formattedStartDate.value} ~ ${addTripInfoViewModel.formattedEndDate.value} ${
                        if (tripDocumentId.isNullOrEmpty()) " 등록완료" else " 수정완료"
                    }"
                },
                onClick = {
                    if (tripDocumentId.isNullOrEmpty()) {
                        addTripInfoViewModel.completeDateOnClick()
                    } else {
                        addTripInfoViewModel.updateDateOnClick(tripDocumentId)
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(bottom = 5.dp),
                cornerRadius = 5
            )
        }
    }
}
