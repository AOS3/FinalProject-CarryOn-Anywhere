package com.lion.FinalProject_CarryOn_Anywhere.component

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

fun Modifier.shimmerEffect(radius: Dp = 16.dp): Modifier = composed {
    var size by remember { mutableStateOf(IntSize.Zero) }

    val transition = rememberInfiniteTransition(label = "")
    val startOffsetX by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1200, // 🔹 속도를 조금 더 부드럽게 조정
                easing = FastOutSlowInEasing // 🔹 최신 앱에서 많이 쓰이는 가속 & 감속 애니메이션 적용
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = ""
    )

    background(
        brush = Brush.linearGradient(
            colors = listOf(
                Color(0xFFEFF0F1), // 연한 회색
                Color(0xFFE0E0E0), // 중간 회색
                Color(0xFFEFF0F1)  // 연한 회색
            ),
            start = Offset(startOffsetX, 0f),
            end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat())
        ),
        shape = RoundedCornerShape(radius)
    ).onGloballyPositioned {
        size = it.size // 🔹 크기 감지하여 반응형 적용
    }
}