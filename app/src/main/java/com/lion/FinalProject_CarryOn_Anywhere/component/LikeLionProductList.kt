package com.lion.FinalProject_CarryOn_Anywhere.component

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lion.FinalProject_CarryOn_Anywhere.R
import com.lion.FinalProject_CarryOn_Anywhere.data.server.model.myposts.ProductModel

// 나의 글 -> 여행 후기에 들어가는 컴포넌트

@Composable
fun LikeLionProductList(
    productList: List<ProductModel>,
    onCreatorNameClick: (ProductModel) -> Unit = {},
    onLikeClick: (ProductModel) -> Unit = {},
    onItemClick: (ProductModel) -> Unit = {},
    columns: Int = 2
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 5.dp)
    ) {
        items(productList) { product ->
            LikeLionProductItem(
                product = product,
                onCreatorNameClick = onCreatorNameClick,
                onLikeClick = onLikeClick,
                onItemClick = onItemClick
            )
        }
    }
}

@Composable
fun LikeLionProductItem(
    product: ProductModel,
    onCreatorNameClick: (ProductModel) -> Unit,
    onLikeClick: (ProductModel) -> Unit,
    onItemClick: (ProductModel) -> Unit
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
                    LikeLionProductImage(

                        imgUrl = R.drawable.test1.toString(), // ✅ 모든 이미지 동일하게 설정 -> 임시
                        size = 150.dp
                    )
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
                text = product.productTitleName,
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
            text = product.productPeriod,
            fontSize = 13.sp, // ✅ 원하는 크기로 설정
            modifier = Modifier.padding(start = 5.dp, end = 5.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,

        )


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(
                text = "리뷰  ${product.productReviewCount} ",
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 5.dp),
                color = Color.Gray
            )

            Text(
                text = "좋아요 ${product.productLikeCount} ",
                fontSize = 12.sp,
                modifier = Modifier.padding(end = 5.dp),
                color = Color.Gray
            )
        }


    }
}

// ✅ 테스트용 ProductModel 리스트
@Composable
fun PreviewProductList() {

}

// ✅ Jetpack Compose Preview 추가
@Preview(showBackground = true)
@Composable
fun PreviewProductGrid() {
    PreviewProductList()
}