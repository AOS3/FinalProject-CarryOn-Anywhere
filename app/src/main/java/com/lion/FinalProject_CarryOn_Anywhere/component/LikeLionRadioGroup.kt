package com.lion.finalprojectshoppingmallservice3team.Component

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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