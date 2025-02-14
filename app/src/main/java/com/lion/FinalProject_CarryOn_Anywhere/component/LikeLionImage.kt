package com.lion.FinalProject_CarryOn_Anywhere.component

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun LikeLionImage(
    bitmap: Bitmap? = null,
    painter : Painter,
    contentScale: ContentScale = ContentScale.Fit,
    modifier: Modifier,
    tintColor : Color? = null,
    // 이미지가 동그라미 모형인지
    isCircular: Boolean = false,
    // 모서리 둥글기
    cornerRadius: Dp = 0.dp,
    // 테두리 두께
    borderWidth: Dp = 0.dp,
    // 테두리 색상
    borderColor: Color = Color.Transparent,
    // 클릭 이벤트
    onClick: (() -> Unit)? = null
) {
    Image(
        modifier = modifier
            .then(
                if (isCircular) {
                    Modifier
                        .clip(CircleShape) // 동그라미 모양
                        .border(borderWidth, borderColor, CircleShape) // 동그라미 테두리
                } else {
                    Modifier
                        .clip(RoundedCornerShape(cornerRadius)) // 모서리 둥글기
                        .border(borderWidth, borderColor, RoundedCornerShape(cornerRadius)) // 둥근 사각형 테두리
                }
            )
            .then(
                if (onClick != null) Modifier.clickable { onClick() } else Modifier
            ),
        painter = bitmap?.asImageBitmap()?.let { BitmapPainter(it) } ?: painter,
        contentDescription = null,
        contentScale = contentScale,
        colorFilter = if(tintColor != null){
            ColorFilter.tint(tintColor)
        } else {
            null
        },
    )
}