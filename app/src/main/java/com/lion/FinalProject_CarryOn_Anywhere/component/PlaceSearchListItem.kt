package com.lion.FinalProject_CarryOn_Anywhere.component

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import com.lion.FinalProject_CarryOn_Anywhere.R
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.GrayColor
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.SubColor
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.Typography

@Composable
fun PlaceSearchListItem(
    // 장소
    place: Map<String, *>,
    // 아이콘
    icon:ImageVector,
    // 아이콘 색상
    iconColor: Color = Color.Red,
    // 아이콘 사이즈
    size: Dp = 42.dp,
    // 아이콘 배경 색상
    iconBackColor: Color = Color.Transparent,
    // 버튼 클릭했을 때
    iconButtonOnClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .background(Color.Transparent),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val imageUrl = place["firstimage"]?.toString()
            ?.takeIf { !it.isNullOrEmpty() }
            ?: "https://example.com/default_image.jpg"
        //Log.d("IMAGE_DEBUG", "로드할 이미지 : $imageUrl")

        val painter = rememberAsyncImagePainter(model = imageUrl)
        val state = painter.state.collectAsState()

        //Log.d("IMAGE_DEBUG", "Coil 상태: ${state.value}")
        AsyncImage(
            model = imageUrl,
            contentDescription = "장소 이미지",
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(10.dp))
                .fillMaxSize(),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(R.drawable.carryon_logo_final) // 로드 발생중 보여줄 사진
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
                fontSize = 13.sp,
                modifier = Modifier
                    .padding(top = 10.dp)
            )

            Text(
                text = place["category"].toString(),
                style = Typography.labelLarge,
                fontSize = 13.sp,
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