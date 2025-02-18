package com.lion.FinalProject_CarryOn_Anywhere.ui.screen.trip

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lion.FinalProject_CarryOn_Anywhere.R
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionAddPlaceItem
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionAlertDialog
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionIconButton
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionTopAppBar
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.GrayColor
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.MainColor
import com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.trip.TripInfoViewModel
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable

@Composable
fun EditPlanPlaceScreen(
    tripInfoViewModel: TripInfoViewModel = hiltViewModel(),
    selectedDay: String,
    selectedIndex: Int,
    tripDocumentId: String
) {
    val reorderState = rememberReorderableLazyListState(
        onMove = { from, to ->
            tripInfoViewModel.reorderPlaces(selectedDay, from.index, to.index) // ViewModel에서 순서 변경
        }
    )

    LaunchedEffect(selectedIndex) {
        Log.d("EditPlanPlaceScreen", "selectedIndex: $selectedIndex")
    }

    Scaffold(
        topBar = {
            LikeLionTopAppBar(
                title = "장소 편집",
                navigationIconImage = ImageVector.vectorResource(R.drawable.arrow_back_24px),
                navigationIconOnClick = {
                    tripInfoViewModel.editPlaceNavigationOnClick(tripDocumentId)
                },
                menuItems = {
                    LikeLionIconButton(
                        icon = ImageVector.vectorResource(R.drawable.done_24px),
                        iconButtonOnClick = {
                            tripInfoViewModel.editPlaceDoneOnClick(tripDocumentId)
                        }
                    )
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, bottom = 20.dp)
            ) {
                Text(
                    text = "day${selectedIndex + 1}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black,
                    modifier = Modifier.padding(end = 10.dp)
                )

                Text(
                    text = selectedDay,
                    style = MaterialTheme.typography.bodyLarge,
                    color = GrayColor,
                    modifier = Modifier.padding(end = 10.dp)
                )
            }

            tripInfoViewModel.placesByDay[selectedDay]?.let { places ->
                LazyColumn(
                    state = reorderState.listState,
                    modifier = Modifier.reorderable(reorderState) // 드래그 가능하게 설정
                ) {
                    itemsIndexed(places, key = { _, item -> item.contentid ?: item.title ?: "" }) { index, place ->
                        LikeLionAddPlaceItem(
                            index = index,
                            place = place,
                            isEdit = true,
                            deleteOnClick = {
                                tripInfoViewModel.deleteTargetPlace.value = place
                                tripInfoViewModel.deletePlaceDialogState.value = true
                            },
                            modifier = Modifier
                                .detectReorderAfterLongPress(reorderState) // 드래그 감지 추가
                        )
                    }
                }
            }

            // 일정 삭제 시 띄우는 다이얼로그
            LikeLionAlertDialog(
                showDialogState = tripInfoViewModel.deletePlaceDialogState,
                confirmButtonTitle = "삭제",
                confirmButtonModifier = Modifier
                    .weight(1f)
                    .padding(start = 10.dp),
                confirmButtonOnClick = {
                    tripInfoViewModel.deleteTargetPlace.value?.let { place ->
                        tripInfoViewModel.removePlaceFromDay(selectedDay, place)
                    }
                    tripInfoViewModel.deleteTargetPlace.value = null
                    tripInfoViewModel.deletePlaceDialogState.value = false
                },
                dismissButtonTitle = "취소",
                dismissContainerColor = Color.Transparent,
                dismissButtonModifier = Modifier
                    .weight(1f)
                    .padding(end = 10.dp),
                dismissBorder = BorderStroke(1.dp, MainColor),
                dismissButtonOnClick = {
                    tripInfoViewModel.deletePlaceDialogState.value = false
                },
                title = "일정 삭제",
                titleModifier = Modifier.fillMaxWidth().padding(bottom = 5.dp),
                titleAlign = TextAlign.Center,
                text = "장소를 삭제하면 다시 추가해야 합니다.",
                textModifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
            )
        }
    }
}