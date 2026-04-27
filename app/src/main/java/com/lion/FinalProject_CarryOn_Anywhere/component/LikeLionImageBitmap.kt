package com.lion.FinalProject_CarryOn_Anywhere.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale


@Composable
fun LikeLionImageBitmap(
    imageBitmap: ImageBitmap,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit
) {
    Image(
        modifier = modifier.fillMaxWidth(),
        bitmap = imageBitmap,
        contentDescription = null,
        contentScale = contentScale,
    )
}