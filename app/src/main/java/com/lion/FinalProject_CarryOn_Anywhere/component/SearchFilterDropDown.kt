package com.lion.FinalProject_CarryOn_Anywhere.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SearchFilterDropdown(
    filterOptions: Map<String, String>,
    selectedFilter: String,
    onFilterSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    // "전체" 옵션
    val extendedFilterOptions = filterOptions.toMutableMap().apply {
        putIfAbsent("전체", "전체")
    }

    val selectedFilterName = extendedFilterOptions[selectedFilter] ?: "전체"

    Box(
        modifier = Modifier
            .wrapContentSize(Alignment.TopStart)
    ) {
        Button(
            onClick = { expanded = true },
            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
        ) {
            Text(text = selectedFilterName)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .heightIn(max = 300.dp)
                .background(Color.White)
        ) {
            filterOptions.forEach { (code, name) ->
                DropdownMenuItem(
                    text = { Text(name) }, // str 값 노출됨
                    onClick = {
                        onFilterSelected(code) // 전달은 코드 값
                        expanded = false
                    }
                )
            }
        }
    }
}
