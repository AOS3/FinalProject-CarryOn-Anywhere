package com.lion.FinalProject_CarryOn_Anywhere.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier

@Composable
fun LikeLionPlaceSearchList(
    dataList: SnapshotStateList<Map<String, Any>>,
    rowComposable: @Composable (Map<String, *>) -> Unit,
    onRowClick: (Any?) -> Unit,
) {
    LazyColumn {
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
    }
}