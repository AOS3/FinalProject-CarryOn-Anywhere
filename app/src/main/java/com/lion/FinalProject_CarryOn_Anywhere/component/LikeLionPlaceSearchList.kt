package com.lion.FinalProject_CarryOn_Anywhere.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun LikeLionPlaceSearchList(
    dataList: SnapshotStateList<Map<String, Any>>,
    rowComposable: @Composable (Map<String, *>) -> Unit,
    onRowClick:() -> Unit,
) {
    LazyColumn {
        items(dataList) { dataMap ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onRowClick()
                    }
            ) {
                rowComposable(dataMap)
            }
        }
    }
}