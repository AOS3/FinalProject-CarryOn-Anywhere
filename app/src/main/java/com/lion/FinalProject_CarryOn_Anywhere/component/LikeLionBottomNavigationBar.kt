package com.lion.FinalProject_CarryOn_Anywhere.component


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Mood
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.lion.FinalProject_CarryOn_Anywhere.R
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.GrayColor
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.MainColor
import kotlinx.coroutines.flow.MutableSharedFlow

@Composable
fun LikeLionBottomNavigation(
    navController: NavController,
    items: List<BottomNavigationItemData>,
) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .selectableGroup()
            .windowInsetsPadding(WindowInsets.navigationBars)
            .height(70.dp),
        containerColor = Color.White,
        contentColor = Color.Transparent,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            items.forEach { item ->
                val selected = currentRoute == item.route

                val noRippleInteractionSource = remember {
                    object : MutableInteractionSource {
                        override val interactions = MutableSharedFlow<Interaction>()
                        override suspend fun emit(interaction: Interaction) {}
                        override fun tryEmit(interaction: Interaction) = true
                    }
                }

                NavigationBarItem(
                    modifier = Modifier.background(Color.Transparent),
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            modifier = Modifier
                                .size(26.dp),
                            tint = if (selected) MainColor else GrayColor,
                            contentDescription = item.label
                        )
                    },
                    label = {
                        Text(
                            text = item.label,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            color = if (selected) MainColor else GrayColor,
                        )
                    },
                    selected = selected,
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Color.Transparent,
                    ),
                    interactionSource = noRippleInteractionSource,
                    onClick = {
                        if (currentRoute != item.route) {
                            navController.navigate(item.route) {
                                launchSingleTop = true
                                restoreState = false
                                popUpTo(currentRoute!!) {
                                    inclusive = true
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}


data class BottomNavigationItemData(
    val icon: ImageVector,
    val label: String,
    val route: String
)