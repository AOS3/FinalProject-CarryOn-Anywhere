package com.lion.FinalProject_CarryOn_Anywhere.component

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LikeLionRadioGroup(
    options: List<String>,
    selectedOption: String,
    textModifier: Modifier = Modifier,
    onOptionSelected: (String) -> Unit,
    fontSize: TextUnit = 0.sp,
    modifier: Modifier = Modifier,
    columnModifier: Modifier = Modifier,
    orientation: Orientation = Orientation.Vertical,
    itemSpacing: Int = 8
) {
    when (orientation) {
        Orientation.Vertical -> {
            Column(modifier = modifier) {
                options.forEach { option ->
                    LikeLionRadioButton(
                        textModifier = textModifier,
                        rowModifier = columnModifier,
                        text = option,
                        fontSize = fontSize,
                        selected = option == selectedOption,
                        onClick = { onOptionSelected(option) }
                    )
                }
            }
        }
        Orientation.Horizontal -> {
            Row(
                modifier = modifier,
                horizontalArrangement = Arrangement.spacedBy(itemSpacing.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                options.forEach { option ->
                    LikeLionRadioButton(
                        textModifier = textModifier,
                        text = option,
                        fontSize = fontSize,
                        selected = option == selectedOption,
                        onClick = { onOptionSelected(option) }
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun LikeLionRadioGroupPreview() {
    val sampleOptions = listOf("동의", "동의 안 함")
    var selectedOption = sampleOptions.first() // 기본 선택 값

    LikeLionRadioGroup(
        options = sampleOptions,
        selectedOption = selectedOption,
        onOptionSelected = { selectedOption = it },
        fontSize = 16.sp,
        modifier = Modifier.fillMaxWidth(),
        orientation = Orientation.Vertical, // 또는 Orientation.Horizontal로 변경 가능
        itemSpacing = 8
    )
}
