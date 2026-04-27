package com.lion.FinalProject_CarryOn_Anywhere.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lion.FinalProject_CarryOn_Anywhere.R
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.GrayColor

@Composable
fun LikeLionEmptyView(message: String) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // X 아이콘
        Image(
            painter = painterResource(id = R.drawable.close_24px), // X 아이콘 리소스
            colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(Color.Gray),
            contentDescription = "No Content",
            modifier = Modifier
                .size(100.dp)
                .padding(bottom = 10.dp)
        )

        // 동적 텍스트
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = GrayColor
        )
    }
}

// 미리보기
@Preview(showBackground = true)
@Composable
fun EmptyScreenPreview() {
    LikeLionEmptyView(message = "작성된 후기가 없습니다.")
}
