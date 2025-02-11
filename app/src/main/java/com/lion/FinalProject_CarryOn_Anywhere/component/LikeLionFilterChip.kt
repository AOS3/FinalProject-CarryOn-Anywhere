package com.lion.FinalProject_CarryOn_Anywhere.component

import android.R.attr.text
import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.google.common.math.LinearTransformation.horizontal

@Composable
fun LikeLionFilterChip(
    text: String,
    selected: Boolean,
    selectedColor: Color,
    unselectedColor: Color,
    chipTextStyle: TextStyle,
    selectedTextColor: Color,
    unselectedTextColor: Color,
    chipModifier: Modifier,
    modifier: Modifier = Modifier,
    onChipClicked: (String, Boolean) -> Unit,
    onDeleteButtonClicked: ((String) -> Unit)? = null // 삭제 버튼 콜백 추가
) {

    Surface(
        color = when {
            selected -> selectedColor
            else -> unselectedColor
        },
        shape = RoundedCornerShape(100.dp),
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { onChipClicked(text, selected) }
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ){
            Text(
                text = text,
                color = when {
                    selected -> selectedTextColor
                    else -> unselectedTextColor
                },
                style = chipTextStyle,
                modifier = chipModifier
            )
            if (onDeleteButtonClicked!=null) {
                IconButton(
                    onClick = { onDeleteButtonClicked(text) },
                    modifier = Modifier.size(20.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear, // 삭제 아이콘 설정
                        contentDescription = "Delete Chip",
                        tint = Color.Gray
                    )
                }
            }
        }
    }
}