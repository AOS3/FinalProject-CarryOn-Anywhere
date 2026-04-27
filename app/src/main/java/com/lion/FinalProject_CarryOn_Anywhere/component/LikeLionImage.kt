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
import androidx.compose.ui.graphics.ColorMatrix


@Composable
fun LikeLionImage(
    bitmap: Bitmap? = null,
    painter: Painter,
    contentScale: ContentScale = ContentScale.Fit,
    modifier: Modifier,
    tintColor: Color? = null,
    isCircular: Boolean = false,
    cornerRadius: Dp = 0.dp,
    borderWidth: Dp = 0.dp,
    borderColor: Color = Color.Transparent,
    onClick: (() -> Unit)? = null,
    isGrayscale: Boolean = false //흑백 여부
) {
    val grayscaleColorFilter = ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(0f) })

    Image(
        modifier = modifier
            .then(
                if (isCircular) {
                    Modifier
                        .clip(CircleShape)
                        .border(borderWidth, borderColor, CircleShape)
                } else {
                    Modifier
                        .clip(RoundedCornerShape(cornerRadius))
                        .border(borderWidth, borderColor, RoundedCornerShape(cornerRadius))
                }
            )
            .then(
                if (onClick != null) Modifier.clickable { onClick() } else Modifier
            ),
        painter = bitmap?.asImageBitmap()?.let { BitmapPainter(it) } ?: painter,
        contentDescription = null,
        contentScale = contentScale,
        colorFilter = if (isGrayscale) grayscaleColorFilter else tintColor?.let { ColorFilter.tint(it) }
    )
}

