package com.lion.FinalProject_CarryOn_Anywhere.component

import android.R.attr.text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@Composable
fun LikeLionChipGroup(
    modifier: Modifier = Modifier,
    elements: List<ChipState>,
    chipStyle: ChipStyle,
    onChipClicked: (String, Boolean, Int) -> Unit,
    onDeleteButtonClicked: ((String, Int) -> Unit)? = null, // 삭제 버튼 콜백 추가
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(start = 10.dp, end = 10.dp),
    ) {
        items(elements.size) { idx ->
            LikeLionFilterChip(
                text = elements[idx].text,
                selected = elements[idx].isSelected.value,
                selectedColor = chipStyle.selectedColor,
                unselectedColor = chipStyle.unselectedColor,
                chipTextStyle = chipStyle.chipTextStyle,
                selectedTextColor = chipStyle.selectedTextColor,
                unselectedTextColor = chipStyle.unselectedTextColor,
                chipModifier = chipStyle.chipModifier,
                modifier = Modifier.shadow(4.dp, shape = RoundedCornerShape(50)),
                onChipClicked = { content, isSelected ->
                    onChipClicked(content, isSelected, idx)
                },

                onDeleteButtonClicked = if (onDeleteButtonClicked!=null)
                { content -> onDeleteButtonClicked(content, idx) }
                else null
            )
            Spacer(modifier = Modifier.padding(8.dp))
        }
    }
}

data class ChipState(
    val text: String,
    var isSelected: MutableState<Boolean>
)

data class ChipStyle(
    val selectedColor: Color,
    val unselectedColor: Color,
    val chipTextStyle: TextStyle,
    val selectedTextColor: Color,
    val unselectedTextColor: Color,
    val chipModifier: Modifier = Modifier,
)
