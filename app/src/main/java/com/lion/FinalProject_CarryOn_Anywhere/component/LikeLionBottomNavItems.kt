package com.lion.FinalProject_CarryOn_Anywhere.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.lion.FinalProject_CarryOn_Anywhere.R
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.ScreenName

@Composable
fun LikeLionBottomNavItems(
//isLoggedIn: Boolean
    ): List<BottomNavigationItemData> {

    return listOf(
        BottomNavigationItemData(
            icon = ImageVector.vectorResource(id = R.drawable.home_variant),
            label = "홈",
            route = ScreenName.MAIN_SCREEN.name
        ),
        BottomNavigationItemData(
            icon = ImageVector.vectorResource(id = R.drawable.assignment_24px),
            label = "캐리톡",
            route = ScreenName.SOCIAL_SCREEN.name
        ),
        BottomNavigationItemData(
            icon = ImageVector.vectorResource(id = R.drawable.favorite_24px),
            label = "내 장소",
            route = ScreenName.MY_LIKE.name
        ),
        BottomNavigationItemData(
            icon = ImageVector.vectorResource(id = R.drawable.account_circle_24px),
            label = "마이페이지",
            //route = if (isLoggedIn) "loginMyPage" else "logoutMyPage",
            route = ScreenName.MY_PAGE.name
        )
    )
}

