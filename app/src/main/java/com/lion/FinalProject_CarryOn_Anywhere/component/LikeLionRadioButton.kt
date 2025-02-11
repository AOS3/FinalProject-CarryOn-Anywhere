package com.lion.finalprojectshoppingmallservice3team.Component

import androidx.annotation.Size
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lion.finalprojectshoppingmallservice3team.ui.theme.MainColor

@Composable
fun LikeLionRadioButton(
    text: String,
    fontSize: TextUnit = 0.sp,
    textModifier: Modifier = Modifier,
    selected: Boolean,
    onClick: () -> Unit,
    rowModifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = rowModifier.clickable {
            onClick()
        }
    ) {
        RadioButton(
            selected = selected,
            onClick = null, // Row의 클릭 이벤트에 의해 처리되므로 null로 설정
            colors = RadioButtonDefaults.colors(
                selectedColor = MainColor, // 선택된 상태의 색상
                unselectedColor = Color.Gray // 선택되지 않은 상태의 색상
            )
        )
        Text(
            text = text,
            fontSize = fontSize,
            modifier = textModifier,
            color = Color.Black
        )
    }
}