package com.lion.FinalProject_CarryOn_Anywhere.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.contentcapture.ContentCaptureManager.Companion.isEnabled
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.GrayColor
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.SubColor
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.Typography

// 메인 - 인기 여행 후기
@Composable
fun BestTripReviewCard(
    // 이미지
    image: Painter,
    // 제목
    title: String,
    // 작성자
    writer: String,
    // 내용
    content: String,
    // 클릭 했을 때
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card (
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.elevatedCardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        onClick = {
            onClick()
        }
    ){
        Column(
            modifier = Modifier
                .padding(20.dp)
        ) {
            Image(
                painter = image,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop,
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = title,
                style = Typography.titleMedium,
                color = SubColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 16.sp,
                )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = writer,
                style = Typography.bodyLarge,
                color = SubColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 14.sp,
                )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = content,
                style = Typography.bodyLarge,
                fontSize = 14.sp,
                color = GrayColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }

    }
}