package com.lion.FinalProject_CarryOn_Anywhere.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.MainColor

@Composable
fun LikeLionFilledButton(
    // 버튼 텍스트
    text:String = "FilledButton",
    border: BorderStroke = BorderStroke(0.dp, Color.Transparent),
    // 위에 패딩
    paddingTop: Dp = 0.dp,
    // 버튼 활성화 여부
    isEnabled: Boolean = true,
    // 아이콘
    icon: ImageVector? = null,
    modifier: Modifier = Modifier,
    // 배경색
    containerColor: Color = MainColor,
    // 글자색
    contentColor: Color = Color.White,
    // 클릭 이벤트
    onClick:() -> Unit = {}

) {
    Button(
        modifier = modifier.padding(top = paddingTop),
        onClick = {
            if (isEnabled) {
                onClick()
            }
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isEnabled) containerColor else Color.LightGray,
            contentColor = if (isEnabled) contentColor else Color.Gray,
        ),
        border = border,
        shape = RoundedCornerShape(5.dp),
        enabled = isEnabled
    ) {
        Row {
            // ImageVector 아이콘 표시
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp) // 아이콘 크기
                )
                Spacer(modifier = Modifier.width(8.dp)) // 텍스트와 아이콘 간격
            }
        }
        Text(text = text)
    }
}
