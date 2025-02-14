package com.lion.FinalProject_CarryOn_Anywhere.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.lion.FinalProject_CarryOn_Anywhere.R
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.MainColor

@Composable
fun LikeLionLikeButton(
    initialLiked: Boolean = false,
    size: Int = 20
) {
    var isLiked by remember { mutableStateOf(initialLiked) }

    val iconColor = if (isLiked) MainColor else Color.Black

    Image(
        painter = painterResource(R.drawable.thumb_up_24px),
        contentDescription = "Like",
        modifier = Modifier
            .size(size.dp)
            .clickable(
                indication = null, // 클릭 리플 효과 제거
                interactionSource = remember { MutableInteractionSource() } // 터치 이벤트 추적 X
            ) { isLiked = !isLiked },
        colorFilter = ColorFilter.tint(iconColor)
    )
}
