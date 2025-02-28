package com.lion.FinalProject_CarryOn_Anywhere.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.rememberAsyncImagePainter
import com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.social.Review
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


// 나의 글 -> 여행 후기에 들어가는 컴포넌트

//@Composable
//fun LikeLionProductList(
//    productList: List<Review>,
//    onCreatorNameClick: (Review) -> Unit = {},
//    onLikeClick: (Review) -> Unit = {},
//    onItemClick: (Review) -> Unit = {},
//    columns: Int = 2
//) {
//    LazyVerticalGrid(
//        columns = GridCells.Fixed(columns),
//        modifier = Modifier.fillMaxSize(),
//        contentPadding = PaddingValues(horizontal = 5.dp)
//    ) {
//        items(productList) { product ->
//            LikeLionProductItem(
//                product = product,
//                onCreatorNameClick = onCreatorNameClick,
//                onLikeClick = onLikeClick,
//                onItemClick = onItemClick
//            )
//        }
//    }
//}

@Composable
fun LikeLionProductItem(
    product: Review,
    onCreatorNameClick: (Review) -> Unit,
    onLikeClick: (Review) -> Unit,
    onItemClick: (Review) -> Unit,

) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .clickable { onItemClick(product) },

        colors = CardDefaults.cardColors(Color.White),
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .size(150.dp),
                shape = RoundedCornerShape(10.dp),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFF6F6F6)),
                    contentAlignment = Alignment.Center
                ) {
                    // 맨 처음 사진만 나오게 한다
                    product.imageUrls.firstOrNull()?.let { imageUrl ->
                        Image(
                            painter = rememberAsyncImagePainter(imageUrl),
                            contentDescription = "여행 후기 Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = product.title,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(start = 5.dp)
                    .padding(bottom = 5.dp)
                    .weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Text(
            text = product.tripDate,
            fontSize = 13.sp, // ✅ 원하는 크기로 설정
            modifier = Modifier.padding(start = 5.dp, end = 5.dp),
            style = MaterialTheme.typography.bodySmall,
        )


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(
                text = "리뷰  ${product.comments} ",
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 5.dp),
                color = Color.Gray
            )

            Text(
                text = "좋아요 ${product.likes} ",
                fontSize = 12.sp,
                modifier = Modifier.padding(end = 5.dp),
                color = Color.Gray
            )
        }


    }
}


// 날짜 변환
private fun formattedDate(timestamp: Long): String {
    val date = Date(timestamp)
    val format = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    return format.format(date)
}

//// ✅ 테스트용 ProductModel 리스트
//@Composable
//fun PreviewProductList() {
//
//}
//
//// ✅ Jetpack Compose Preview 추가
//@Preview(showBackground = true)
//@Composable
//fun PreviewProductGrid() {
//    PreviewProductList()
//}