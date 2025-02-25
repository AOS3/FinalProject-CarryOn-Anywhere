package com.lion.FinalProject_CarryOn_Anywhere.component

import android.util.Log
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.lion.FinalProject_CarryOn_Anywhere.R
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.GrayColor

@Composable
fun LikeLionPlaceListItem(
    imageUrl: String,
    title: String,
    subtitle: String,
    location: String,
    onSelectClick: () -> Unit

) {
    val fixedImageUrl = imageUrl.replace("http://", "https://")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 이미지 (비율 유지)
        AsyncImage(
            model = fixedImageUrl.also { Log.d("ImageTest", "이미지 URL: $it") }, // 🔹 로그 찍기
            contentDescription = "장소 이미지",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(70.dp)
                .clip(RoundedCornerShape(10.dp)),
            placeholder = painterResource(R.drawable.noplaceimg),
            error = painterResource(R.drawable.noplaceimg)
        )

        Spacer(modifier = Modifier.width(12.dp))

        // 텍스트 정보
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "$subtitle • $location",
                style = MaterialTheme.typography.bodyMedium,
                color = GrayColor
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // 선택 버튼
        Button(
            onClick = { onSelectClick() },
            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
            shape = RoundedCornerShape(20.dp)
        ) {
            Text("선택", color = Color.Black)
        }
    }
}
