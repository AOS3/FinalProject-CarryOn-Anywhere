package com.lion.FinalProject_CarryOn_Anywhere.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.lion.FinalProject_CarryOn_Anywhere.data.api.TourAPI.TourApiModel
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.MainColor
import org.burnoutcrew.reorderable.ReorderableLazyListState
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState

@Composable
fun LikeLionAddPlaceItem(
    index: Int,
    place: Map<String, Any?>,  // ✅ `Place` → `TouristSpotItem` 변경
    isEdit: Boolean = false,
    state: ReorderableLazyListState = rememberReorderableLazyListState(onMove = { from, to -> } ),
    distanceToNext: Float? = null, // ✅ 다음 장소와의 거리 추가
    deleteOnClick: () -> Unit = {},
    lastIndex: Int = -1,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = if (index == lastIndex || isEdit) 20.dp else 0.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isEdit) {
                // 삭제 버튼 (빨간색 동그라미)
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .background(Color.Red, shape = CircleShape)
                        .clickable {
                            deleteOnClick()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "-",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            } else {
                // 동그라미 인덱스
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .background(MainColor, shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${index + 1}",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp)) // 간격 추가

            // 장소 정보
            Row(
                modifier = Modifier
                    .weight(1f)
                    .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
            ) {
                Column(
                    modifier = Modifier.padding(10.dp)
                ) {
                    Text(
                        text = (place["title"] as? String) ?: "알 수 없는 장소",  // 관광지명 안전 변환
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Black
                    )
                    Text(
                        text = if (place["addr2"] == "") {
                            (place["addr1"] as? String) ?: "주소 없음"
                        } else {
                            "${(place["addr1"] as? String)} / ${(place["addr2"] as? String)}"
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }

            if (isEdit) {
                LikeLionIconButton(
                    icon = Icons.Default.Menu,
                    modifier = Modifier.detectReorderAfterLongPress(state) // 드래그 가능하게 설정
                )
            }
        }

        // 다음 장소가 있다면 거리 정보 추가
        if (distanceToNext != null) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(15.dp))
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(30.dp)
                        .background(Color.Gray) // 타임라인 선
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = if (distanceToNext >= 1000) "%.2f km".format(distanceToNext / 1000) else "%.0f m".format(distanceToNext),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Black
                )
            }
        }
    }
}
