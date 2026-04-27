package com.lion.FinalProject_CarryOn_Anywhere.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.SubColor

@Composable
fun LikeLionFilterChip(
    // 칩 버튼 텍스트
    text: String,
    // 버튼 선택 여부
    selected: Boolean,
    // 선택된 버튼의 배경색
    selectedColor: Color,
    // 선택되지 않은 버튼의 배경색
    unselectedColor: Color,
    // 선택되지 않았을 때의 테두리 색상
    borderColor: Color,
    // 텍스트 스타일
    chipTextStyle: TextStyle,
    // 선택된 버튼 텍스트 색상
    selectedTextColor: Color,
    // 선택되지 않은 버튼의 텍스트 색상
    unselectedTextColor: Color,
    // 텍스트(칩 내부 요소)의 모디파이어
    chipModifier: Modifier,
    // 칩 버튼 모서리 각도
    cornerRadius: Int,
    // 칩 버튼의 모디파이어
    modifier: Modifier = Modifier,
    // 칩 버튼 클릭 이벤트
    onChipClicked: (String, Boolean) -> Unit,
    // 칩 삭제 버튼이 클릭되었을 때 호출되는 콜백 함수 (텍스트)
    onDeleteButtonClicked: ((String) -> Unit)? = null
) {
    Surface(
        // 선택 여부에 따라 배경색 변경
        color = if (selected) selectedColor else Color.Transparent,
        // 모서리 각도 설정
        shape = RoundedCornerShape(cornerRadius.dp),
        // 선택되지 않은 경우 테두리 추가
        border = if (!selected) BorderStroke(1.dp, borderColor) else null,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                // 클릭 이벤트
                .clickable { onChipClicked(text, selected) }
                // 내부 패딩
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = text,
                // 선택 여부에 따라 글자색 변경
                color = if (selected) selectedTextColor else unselectedTextColor,
                style = chipTextStyle,
                modifier = chipModifier
            )

            // 삭제 버튼이 존재하는 경우, 아이콘 버튼 추가
            if (onDeleteButtonClicked != null) {
                IconButton(
                    // 클릭시 콜백 함수
                    onClick = { onDeleteButtonClicked(text) },
                    // 아이콘 크기 설정
                    modifier = Modifier.size(20.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Delete Chip",
                        tint = Color.Black
                    )
                }
            }
        }
    }
}