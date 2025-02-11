package com.lion.FinalProject_CarryOn_Anywhere.component

import android.R.attr.navigationIcon
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LikeLionTopAppBar(
    title:String = "",
    backColor:Color,
    navigationIconImage:ImageVector? = null,
    scrollValue: Int = 0,
    navigationIconOnClick:() -> Unit = {},
    menuItems : @Composable RowScope.() -> Unit = {},
){

    Column {
        TopAppBar(
            // 타이틀
            title = {
                Text(text = title)
            },
            colors = TopAppBarDefaults.topAppBarColors(backColor),
            // 네비게이션 아이콘
            navigationIcon = if(navigationIconImage == null){
                {}
            } else {
                {
                    IconButton(
                        onClick = navigationIconOnClick
                    ) {
                        Icon(
                            imageVector = navigationIconImage,
                            contentDescription = null
                        )
                    }
                }
            },
            actions = {
                menuItems()
            },
        )
        if (scrollValue>0)
            HorizontalDivider(
                thickness = 1.5.dp,
                color = Color(0x40383838)
            )
    }

}