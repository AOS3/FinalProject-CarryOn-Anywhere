package com.lion.FinalProject_CarryOn_Anywhere.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lion.FinalProject_CarryOn_Anywhere.R

@Composable
fun LikeLionEmptyPhoto(
    message: String, // 표시할 메시지
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 이미지 (아이콘)
        Image(
            painter = painterResource(R.drawable.hide_image_24px), // 기본 이미지 사용
            colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(Color.Gray),
            contentDescription = "No image",
            modifier = Modifier
                .size(60.dp)
                .padding(bottom = 5.dp)
        )

        // 메시지
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            textAlign = TextAlign.Center, // ✅ 텍스트 중앙 정렬 적용
            modifier = Modifier.fillMaxWidth() // ✅ 가로로 꽉 채워서 중앙 정렬 유지
        )
    }
}