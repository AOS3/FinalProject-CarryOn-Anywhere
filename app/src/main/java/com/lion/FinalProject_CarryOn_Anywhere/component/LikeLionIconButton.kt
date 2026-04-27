package com.lion.FinalProject_CarryOn_Anywhere.component

import android.R.attr.contentDescription
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun LikeLionIconButton(
    icon:ImageVector,
    iconColor: Color = LocalContentColor.current,
    color: Color = Color.White,
    iconBackColor: Color = Color.White,
    size: Dp = 42.dp,
    padding: Dp = 4.dp,
    iconButtonOnClick : () -> Unit = {},
    modifier: Modifier = Modifier,
    borderNull: Boolean = true
) {

    val iconSize = size - 10.dp

    Row {
        IconButton(
            onClick = {
                iconButtonOnClick()
            },
            modifier = modifier.size(size),
            colors = IconButtonDefaults.iconButtonColors(
                color
            )
        ){
            Icon(
                modifier = Modifier.background(color)
                    .size(iconSize),
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
            )
        }
        Spacer(Modifier.width(padding))
    }


}