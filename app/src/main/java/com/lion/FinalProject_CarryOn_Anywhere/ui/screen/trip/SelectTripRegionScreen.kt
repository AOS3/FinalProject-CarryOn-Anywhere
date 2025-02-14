package com.lion.FinalProject_CarryOn_Anywhere.ui.screen.trip

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lion.FinalProject_CarryOn_Anywhere.R
import com.lion.FinalProject_CarryOn_Anywhere.component.ChipState
import com.lion.FinalProject_CarryOn_Anywhere.component.ChipStyle
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionChipGroup
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionFilledButton
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionRegionSelect
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionTopAppBar
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.GrayColor
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.SubColor
import com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.trip.TripInfoViewModel

@Composable
fun SelectTripRegionScreen(
    tripInfoViewModel: TripInfoViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            LikeLionTopAppBar(
                title = "지역 선택",
                navigationIconImage = ImageVector.vectorResource(R.drawable.arrow_back_24px),
                navigationIconOnClick = {

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
                text = "여행하실 지역을 선택해주세요.",
                style = MaterialTheme.typography.bodyLarge,
                color = GrayColor,
                modifier = Modifier.padding(top = 15.dp, bottom = 15.dp)
            )

            LikeLionChipGroup(
                modifier = Modifier.padding(bottom = 15.dp),
                elements = tripInfoViewModel.selectedRegions,
                chipStyle = ChipStyle(
                    selectedColor = SubColor,
                    unselectedColor = Color.LightGray,
                    chipTextStyle = MaterialTheme.typography.bodyMedium,
                    selectedTextColor = Color.White,
                    unselectedTextColor = Color.Black,
                    chipModifier = Modifier.padding(start = 10.dp, top = 4.dp, bottom = 4.dp, end = 10.dp)
                ),
                onChipClicked = { _, _, _ -> },
                onDeleteButtonClicked = { content, _ ->
                    tripInfoViewModel.selectedRegions.removeIf { it.text == content }
                }
            )

            LikeLionRegionSelect(
                modifier = Modifier.weight(1f),
                regions = tripInfoViewModel.regions,
                subRegionsMap = tripInfoViewModel.subRegionsMap,
                onClick = { selectedRegion ->
                    if (tripInfoViewModel.selectedRegions.none { it.text == selectedRegion }) {
                        tripInfoViewModel.selectedRegions.add(
                            ChipState(
                                text = selectedRegion,
                                isSelected = mutableStateOf(false)
                            )
                        ) // 리스트에 직접 추가
                    }
                }
            )

            LikeLionFilledButton(
                text = "${tripInfoViewModel.selectedRegions.size}개 지역 선택 완료",
                modifier = Modifier.fillMaxWidth().padding(bottom = 5.dp),
                cornerRadius = 5,
                onClick = {
                    tripInfoViewModel.completeRegionOnClick()
                }
            )
        }
    }
}