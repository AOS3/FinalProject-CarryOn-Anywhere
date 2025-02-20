package com.lion.FinalProject_CarryOn_Anywhere.ui.screen.trip

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.MainColor
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.SubColor
import com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.trip.AddTripInfoViewModel
import com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.trip.TripInfoViewModel

@Composable
fun SelectTripRegionScreen(
    addTripInfoViewModel: AddTripInfoViewModel,
) {
    val regions by addTripInfoViewModel.regions
    val subRegionsMap by addTripInfoViewModel.subRegionsMap

    Scaffold(
        topBar = {
            LikeLionTopAppBar(
                title = "지역 선택",
                navigationIconImage = ImageVector.vectorResource(R.drawable.arrow_back_24px),
                navigationIconOnClick = {
                    addTripInfoViewModel.selectRegionNavigationOnClick()
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
                modifier = Modifier.padding(top = 25.dp, bottom = 20.dp)
            )

            if (addTripInfoViewModel.selectedRegions.size == 0) {
                Box(
                    modifier = Modifier.padding(bottom = 37.dp)
                )
            }

            // 선택된 지역 Chip Group
            LikeLionChipGroup(
                modifier = Modifier.padding(bottom = 20.dp),
                elements = addTripInfoViewModel.selectedRegions,
                chipStyle = ChipStyle(
                    selectedColor = Color.White,
                    unselectedColor = MainColor,
                    chipTextStyle = MaterialTheme.typography.bodyMedium,
                    selectedTextColor = Color.Black,
                    unselectedTextColor = Color.Black,
                    chipModifier = Modifier.padding(start = 10.dp, top = 4.dp, bottom = 4.dp, end = 10.dp)
                ),
                onChipClicked = { _, _, _ -> },
                onDeleteButtonClicked = { content, _ ->
                    addTripInfoViewModel.selectedRegions.removeIf { it.text == content }
                    addTripInfoViewModel.updateRegionButtonState()
                }
            )

            if (regions.isNotEmpty()) {
                LikeLionRegionSelect(
                    modifier = Modifier
                        .weight(1f),
                    regions = regions,
                    subRegionsMap = subRegionsMap,
                    regionOnClick = { selectedRegion ->
                        addTripInfoViewModel.fetchSubRegions(selectedRegion)
                    },
                    onClick = { selectedRegion ->
                        Log.d("SelectTripRegionScreen", "✅ 클릭한 시/도: $selectedRegion") // <== 로그 추가

                        if (addTripInfoViewModel.selectedRegions.none { it.text == selectedRegion }) {
                            addTripInfoViewModel.selectedRegions.add(
                                ChipState(text = selectedRegion, isSelected = mutableStateOf(false))
                            )
                            addTripInfoViewModel.updateRegionButtonState()
                        }
                    }
                )
            } else {
                Text(text = "", color = Color.Gray, modifier = Modifier
                    .weight(1f),)
            }

            LikeLionFilledButton(
                text = "${addTripInfoViewModel.selectedRegions.size}개 지역 선택 완료",
                modifier = Modifier.fillMaxWidth().padding(bottom = 25.dp),
                isEnabled = addTripInfoViewModel.isButtonRegionEnabled.value,
                cornerRadius = 5,
                onClick = {
                    addTripInfoViewModel.completeRegionOnClick()
                }
            )
        }
    }
}