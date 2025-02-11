package com.lion.FinalProject_CarryOn_Anywhere.component

import android.R.attr.contentDescription
import android.R.attr.fontWeight
import android.R.attr.onClick
import androidx.compose.foundation.Canvas
import com.lion.finalprojectshoppingmallservice3team.R
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.defaultMinSize
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
import androidx.compose.material.minimumInteractiveComponentSize
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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
import com.lion.finalprojectshoppingmallservice3team.ui.theme.MainColor
import kotlinx.coroutines.flow.MutableSharedFlow

@Composable
fun LikeLionBottomNavigation(
    navController: NavController,
    items: List<BottomNavigationItemData>,
) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    NavigationBar (
        modifier = Modifier.fillMaxWidth()
            .selectableGroup()
            .windowInsetsPadding(WindowInsets.navigationBars)
            .height(70.dp),
        containerColor = Color.LightGray.copy(alpha = 0.7f),
        contentColor = Color.Transparent,
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ){
            items.forEach { item ->
                // 현재 항목이 선택된 상태인지 확인
                val selected = currentRoute == item.route


                // 드디어 리플 제거
                val noRippleInteractionSource = remember {
                    object : MutableInteractionSource {
                        override val interactions = MutableSharedFlow<Interaction>()
                        override suspend fun emit(interaction: Interaction) {}
                        override fun tryEmit(interaction: Interaction) = true
                    }
                }
                NavigationBarItem(
                    modifier = Modifier
                        .background(Color.Transparent),
                    icon = {
                        Box {
                            val shadow = if (item.icon == Icons.Default.Mood)
                                ImageVector.vectorResource(R.drawable.radio_button_checked_24px)
                            else item.icon
                            val mainShadowColor = if(item.icon == Icons.Default.Home) Color.Black
                                else Color(0xFF8369A8)
                            Icon(imageVector = shadow,
                                modifier = Modifier
                                    .size(width = 27.dp, height = 26.dp)
                                    .offset(x = (1).dp, y = (3.8).dp),
                                tint = if (selected) mainShadowColor.copy(0.35f) else Color.Black.copy(0.25f),
                                contentDescription = item.label)
                            Icon(imageVector = item.icon,
                                modifier = Modifier
                                    .size(26.dp)
                                    .offset(y = (3).dp),
                                tint = if (selected) MainColor else Color.Black,
                                contentDescription = item.label)

                        }

                    },
                    label = {
                        Canvas(
                            modifier = Modifier
                                .size(55.dp,21.dp)
                                .padding(bottom = 13.dp)
                                .offset(x = (7).dp, y = (4.3).dp)
                                .clip(RoundedCornerShape(200.dp)),
                            onDraw = {
                                drawOval(
                                    color = if (selected) Color.Transparent.copy(0.65f) else Color.Transparent.copy(0.3f),
                                    topLeft = Offset((-5).dp.toPx(), 0.dp.toPx()),
                                    size = Size(65.dp.toPx(), 10.dp.toPx()),
                                )
                            }
                        )
                            Box(
                                modifier = Modifier
                                    .background(Color.Transparent)
                                    .clip(RoundedCornerShape(10.dp))
                                    .padding(bottom = 13.dp)
                                    .offset(y = (-3).dp)
                                    .wrapContentWidth()
                            ) {

                                Text(text = item.label,
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center,
                                    color = if (selected) MainColor else Color.Black
                                )


                            }

                    },
                    selected = selected,
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Color.Transparent,
                    ),

                    // 리플제거
                    interactionSource = noRippleInteractionSource,
                    onClick = {
                        // 현재 route가 선택된 route와 다를 경우
                        if (currentRoute != item.route) {
                            navController.navigate(item.route) {
                                launchSingleTop = true  // 현재 화면을 재사용하여 새로운 화면을 추가하지 않음
                                restoreState = false  // 이전 상태를 복원하지 않음
                                popUpTo(currentRoute!!) {  // 스택을 초기화하여 해당 화면을 최상위로 만듬
                                    inclusive = true  // 해당 route 포함하여 이전 화면 제거
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