package com.lion.FinalProject_CarryOn_Anywhere.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.MainColor
import org.checkerframework.common.subtyping.qual.Bottom

@Composable
fun LikeLionFilledButton(
    // 버튼 텍스트
    text: String = "FilledButton",
    // 테두리 설정
    border: BorderStroke = BorderStroke(0.dp, Color.Transparent),
    // 위쪽 패딩
    paddingTop: Dp = 0.dp,
    // 아래쪽 패딩
    paddingBottom: Dp = 0.dp,
    // 좌우 패딩
    horizontalPadding: Dp = 10.dp,
    // 버튼이 가로를 꽉 채울지 여부
    fillWidth: Boolean = true,
    // 버튼 너비 (기본값 null → 설정하지 않으면 fillWidth 적용)
    buttonWidth: Dp? = null,
    // 버튼 높이 (기본값 null → 설정하지 않으면 자동 높이)
    buttonHeight: Dp? = null,
    // 버튼 모서리 각도
    cornerRadius: Int,
    // 버튼 활성화 여부
    isEnabled: Boolean = true,
    // 아이콘
    icon: ImageVector? = null,
    // 모디파이어
    modifier: Modifier = Modifier,
    // 배경색
    containerColor: Color = MainColor,
    // 글자색
    contentColor: Color = Color.White,
    // 클릭 했을 때
    onClick: () -> Unit = {}
) {
    Button(
        modifier = modifier
            .then(
                // 너비가 지정 되었을 경우 적용
                if (buttonWidth != null) Modifier.width(buttonWidth)
                // fillWidth가 true면 가로 최대
                else if (fillWidth) Modifier.fillMaxWidth()
                // 아무 것도 적용하지 않으면 기본 크기
                else Modifier
            )
            .then(
                // 높이가 설정되었으면 적용
                if (buttonHeight != null) Modifier.height(buttonHeight)
                else Modifier
            )
            .padding(
                start = horizontalPadding,
                top = paddingTop,
                end = horizontalPadding,
                bottom = paddingBottom
            ),
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
        // 모서리 각도 설정
        shape = RoundedCornerShape(cornerRadius.dp),
        enabled = isEnabled
    ) {
        Row {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
        Text(text = text)
    }
}
