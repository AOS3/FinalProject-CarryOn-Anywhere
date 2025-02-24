package com.lion.FinalProject_CarryOn_Anywhere.component

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.MainColor
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.SubColor
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.Typography

@Composable
fun PlaceSearchListItem(
    // 장소
    place: Map<String, *>,
    // 아이콘
    //icon:ImageVector,
    // 아이콘 색상
    iconColor: Color = Color.Red,
    // 아이콘 사이즈
    size: Dp = 42.dp,
    // 아이콘 배경 색상
    iconBackColor: Color = Color.Transparent,
    modifier: Modifier = Modifier,
    // 찜 여부
    isLiked: Boolean,
    // 찜 버튼 클릭했을 때
    onLikeClick: (String, String) -> Unit,
) {

    // 로딩 여부
    val isLoading = place["isLoading"] as? Boolean ?: true

    val imageUrl = place["firstimage"].toString()
    // 이미지 url http ↔ https 변환
    val fixedImageUrl = when {
        imageUrl.startsWith("http://") -> imageUrl.replace("http://", "https://")
        imageUrl.startsWith("https://") -> imageUrl.replace("https://", "http://")
        else -> null
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .background(Color.Transparent),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isLoading) {
            // 로딩 중일 때 가운데 프로그래스바 표시
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.Gray)
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MainColor
                )
            }
        } else {
            // ✅ 로드 완료 시 기존 UI 표시
            AsyncImage(
                model = fixedImageUrl,
                contentDescription = "장소 이미지",
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.noplaceimg),
                error = painterResource(R.drawable.noplaceimg)
            )
        }

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

        // 찜 버튼
        LikeLionIconButton(
            icon =  if (isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
            iconColor = iconColor,
            size = size,
            iconButtonOnClick = {
                val contentId = place["contentid"].toString()
                val contentTypeId = place["contenttypeid"].toString()
                onLikeClick(contentId, contentTypeId)
            },
            iconBackColor = iconBackColor,
        )
    }
}