package com.lion.FinalProject_CarryOn_Anywhere.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.MainColor

@Composable
fun LikeLionPlaceSearchList(
    dataList: MutableList<Map<String, Any>>,
    rowComposable: @Composable (Map<String, *>) -> Unit,
    // 로딩 상태
    isLoading: Boolean,
    listState: LazyListState,
    onRowClick: (Any?) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        state = listState,
    ) {
        items(dataList) { dataMap ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onRowClick(dataMap)
                    }
            ) {
                rowComposable(dataMap)
            }
        }
        // 로딩 중일 때 프로그래스 표시
        if (isLoading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = MainColor
                    )
                }
            }
        }
    }
}