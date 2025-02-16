package com.lion.FinalProject_CarryOn_Anywhere.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lion.FinalProject_CarryOn_Anywhere.R
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.GrayColor
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.SubColor
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.Typography
import java.util.Vector

@Composable
fun PlaceSearchListItem(
    // 장소
    place: Map<String, Any>,
    // 아이콘
    icon:ImageVector,
    // 아이콘 색상
    iconColor: Color = Color.Red,
    // 아이콘 사이즈
    size: Dp = 42.dp,
    // 아이콘 배경 색상
    iconBackColor: Color = Color.Transparent,
    // 버튼 클릭했을 때
    iconButtonOnClick : () -> Unit = {},
    modifier: Modifier = Modifier
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .background(Color.Transparent),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = place["imageRes"] as Int),
            contentDescription = null,
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(10.dp)),
            contentScale = ContentScale.Crop,
        )

        Spacer(modifier = Modifier.width(20.dp))

        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            Text(
                text = place["title"].toString(),
                style = Typography.headlineSmall,
                color = SubColor,
                modifier = Modifier
                    .padding(top = 10.dp)
            )

            Text(
                text = place["region"].toString(),
                style = Typography.labelLarge,
                color = SubColor,
                modifier = Modifier
                    .padding(top = 10.dp)
            )

            Text(
                text = place["category"].toString(),
                style = Typography.labelLarge,
                fontSize = 14.sp,
                color = GrayColor,
                modifier = Modifier
                    .padding(top = 10.dp)
            )
        }

        LikeLionIconButton(
            icon = icon,
            iconColor = iconColor,
            size = size,
            iconButtonOnClick = iconButtonOnClick,
            iconBackColor = iconBackColor,
        )
    }
}