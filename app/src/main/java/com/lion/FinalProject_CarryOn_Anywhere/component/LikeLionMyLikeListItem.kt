package com.lion.FinalProject_CarryOn_Anywhere.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lion.FinalProject_CarryOn_Anywhere.R

@Composable
fun LikeLionMyLikeItem(
    title: String,
    location: String,
    category: String,
    imageResId: Int = R.drawable.test1, // 샘플 이미지
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    onItemClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onItemClick() }, // ✅ 클릭 시 상세 페이지 이동
        verticalAlignment = Alignment.CenterVertically
    ) {
        // ✅ 대표 이미지
        Image(
            painter = painterResource(id = imageResId),
            contentDescription = null,
            modifier = Modifier
                .width(100.dp)
//                .height()
                .padding(5.dp)
                .clip(
                    RoundedCornerShape(10.dp))

        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 5.dp)
        ) {
            Text(text = title, fontSize = 16.sp, color = Color.Black)
            Text(text = location, fontSize = 12.sp, color = Color.Gray)
            Text(text = category, fontSize = 12.sp, color = Color.Gray)
        }

        // ✅ 찜 아이콘
        IconButton(onClick = onFavoriteClick) {
            Icon(
                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = "찜하기",
                tint = if (isFavorite) Color.Red else Color.Gray
            )
        }
    }
}

// ✅ 미리보기 추가
@Preview(showBackground = true)
@Composable
fun PreviewLikeLionMyLikeList() {
    var isFavorite by remember { mutableStateOf(true) } // ✅ 초기 찜 상태

    LikeLionMyLikeItem(
        title = "라마다프라자호텔 제주도",
        location = "제주도",
        category = "숙박",
        isFavorite = isFavorite,
        onFavoriteClick = { isFavorite = !isFavorite }, // ✅ 찜 상태 변경
        onItemClick = { /* 클릭 시 상세 페이지 이동 */ }
    )
}
