package com.lion.FinalProject_CarryOn_Anywhere.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.SubColor

@Composable
fun LikeLionRegionSelect(
    modifier: Modifier = Modifier,
    regions: List<String>,
    subRegionsMap: Map<String, List<String>>,
    onClick: (String) -> Unit = {}
) {
    var selectedRegion by remember { mutableStateOf(regions[0]) } // 선택된 시/도
    val selectedSubRegions by remember(selectedRegion) {
        derivedStateOf { subRegionsMap[selectedRegion] ?: emptyList() }
    }

    Row(
        modifier = modifier.fillMaxHeight()
    ) {
        // 왼쪽: 시/도 목록
        LazyColumn(
            modifier = Modifier.weight(0.3f)
        ) {
            itemsIndexed(regions) { index, region ->
                val isFirst = index == 0
                val isLast = index == regions.lastIndex
                val isSelected = region == selectedRegion

                // 첫 번째와 마지막 항목에만 RoundedCorner 적용
                val cornerRadius = RoundedCornerShape(
                    topStart = if (isFirst) 12.dp else 0.dp,
                    topEnd = if (isFirst) 12.dp else 0.dp,
                    bottomStart = if (isLast) 12.dp else 0.dp,
                    bottomEnd = if (isLast) 12.dp else 0.dp
                )

                Box(
                    modifier = Modifier
                        .width(100.dp)
                        .clickable { selectedRegion = region }
                        .background(
                            if (isSelected) SubColor else Color(0xFFEFEFEF),
                            shape = cornerRadius
                        )
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = region,
                        fontSize = 16.sp,
                        color = if (isSelected) Color.White else Color.Black
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        // 오른쪽: 선택된 시/도의 구/군 목록
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            itemsIndexed(selectedSubRegions) { index, subRegion ->
                val isFirst = index == 0
                val isLast = index == selectedSubRegions.lastIndex

                // 첫 번째와 마지막 항목에만 RoundedCorner 적용
                val cornerRadius = RoundedCornerShape(
                    topStart = if (isFirst) 12.dp else 0.dp,
                    topEnd = if (isFirst) 12.dp else 0.dp,
                    bottomStart = if (isLast) 12.dp else 0.dp,
                    bottomEnd = if (isLast) 12.dp else 0.dp
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (selectedRegion == "서울" ||
                                selectedRegion == "부산" ||
                                selectedRegion == "인천" ||
                                selectedRegion == "대구" ||
                                selectedRegion == "대전" ||
                                selectedRegion == "울산"
                                ) {
                                val fullRegion = "${selectedRegion}시 $subRegion"
                                onClick(fullRegion)
                            } else if (
                                selectedRegion == "경기" ||
                                selectedRegion == "강원" ||
                                selectedRegion == "충청" ||
                                selectedRegion == "경상" ||
                                selectedRegion == "전라"
                            ) {
                                val fullRegion = "${selectedRegion}도 $subRegion"
                                onClick(fullRegion)
                            } else {
                                val fullRegion = "${selectedRegion}특별자치도 $subRegion"
                                onClick(fullRegion)
                            }
                        }
                        .background(color = Color.Transparent, shape = cornerRadius)
                        .border(width = 1.dp, color = Color.LightGray, shape = cornerRadius)
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = subRegion, fontSize = 16.sp, color = Color.Black)
                }
            }
        }
    }
}
