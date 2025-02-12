import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.MainColor

@Composable
fun LikeLionFixedTabs(
    tabTitleWithCounts: List<String>,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    TabModifier: Modifier = Modifier,
    divider: @Composable () -> Unit = {},
) {
    TabRow(
        selectedTabIndex = selectedTabIndex,
        modifier = modifier.fillMaxWidth(),
        divider = divider,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                modifier = Modifier
                    // 선택된 탭에 맞게 위치 설정
                    .tabIndicatorOffset(tabPositions[selectedTabIndex])
                    .fillMaxWidth(),
                color = MainColor,
                height = 3.dp
            )
        }
    ) {
        tabTitleWithCounts.forEachIndexed { index, title ->
            Tab(
                selected = selectedTabIndex == index,
                onClick = { onTabSelected(index) },
                text = {
                    Text(
                        text = title,
                        color = if (selectedTabIndex == index) MainColor else Color(0xFF49454F),
                    )
                },
                modifier = TabModifier
            )
        }
    }
}