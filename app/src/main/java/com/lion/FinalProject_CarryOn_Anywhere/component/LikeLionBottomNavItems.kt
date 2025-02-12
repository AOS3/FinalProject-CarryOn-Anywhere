package com.lion.FinalProject_CarryOn_Anywhere.component

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Mood
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shop
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.lion.FinalProject_CarryOn_Anywhere.R

@Composable
fun LikeLionBottomNavItems(isLoggedIn: Boolean): List<BottomNavigationItemData> {

    return listOf(
        BottomNavigationItemData(
            icon = ImageVector.vectorResource(id = R.drawable.home_variant),
            label = "홈",
            route = "home"
        ),
        BottomNavigationItemData(
            icon = ImageVector.vectorResource(id = R.drawable.assignment_24px),
            label = "캐리톡",
            route = "social"
        ),
        BottomNavigationItemData(
            icon = ImageVector.vectorResource(id = R.drawable.favorite_24px),
            label = "내 장소",
            route = "myPlace"
        ),
        BottomNavigationItemData(
            icon = ImageVector.vectorResource(id = R.drawable.account_circle_24px),
            label = "마이페이지",
            route = if (isLoggedIn) "loginMyPage" else "logoutMyPage",
        )
    )
}

