package com.lion.FinalProject_CarryOn_Anywhere.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lion.FinalProject_CarryOn_Anywhere.R
import com.lion.FinalProject_CarryOn_Anywhere.data.server.model.ProductModel

// 캐리톡 -> 여행 후기에 적용할 리스트
// 연결되어있는 ProductModel는 임시 파일입니다 -> 추후 수정

@Composable
fun LikeLionProductList(
    productList: List<ProductModel>,
    onCreatorNameClick: (ProductModel) -> Unit,
    onLikeClick: (ProductModel) -> Unit,
    onItemClick: (ProductModel) -> Unit,
    columns: Int = 2,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        modifier = Modifier
            .fillMaxSize()
            .padding(),
        contentPadding = PaddingValues(horizontal = 10.dp)
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
            .padding(10.dp)
            .clickable { onItemClick(product) },
        colors = CardDefaults.cardColors(Color.White),
        //elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        // 이미지와 상태 레이블을 겹쳐서 배치
        Box(modifier = Modifier
            .fillMaxWidth()
        ) {
            // 이미지
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                shape = RoundedCornerShape(10.dp),
            ) {
                Box (
                    modifier = if(product.productSellerName == "LZPP" || product.productName == "[PRE-ORDER] 아이돌 하루토 멀티 아크릴 스탠드 (남)" ||product.productName == "[PRE-ORDER] 아이돌 하루히 멀티 아크릴 스탠드 (여)" ){
                        Modifier.fillMaxSize()
                            .background(Color(0xFFFFFFFF))
                    }else{
                        Modifier.fillMaxSize()
                            .background(Color(0xFFF6F6F6))
                    },
                    contentAlignment = Alignment.Center
                ){
                    LikeLionProductImage(
                        modifier = Modifier
                            .graphicsLayer {
                                if(product.productManagementAllQuantity == 0L){
                                    alpha = 0.3f
                                }
                            },
                        imgUrl = product.productImages.firstOrNull() ?: "",
                        size = 200.dp,

                        // 어플 로고 -> 추후 수정
                        fixedImage = R.drawable.ic_launcher_background,
                    )
                }
            }


        }

        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 크리에이터 이름 -> 제목으로 사용
            Text(
                text = product.productSellerName,
                //style = Typography.bodySmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(start = 5.dp)
                    .weight(1f)
                    .clickable { onCreatorNameClick(product) },
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

        }

        // 상품 이름 -> 날짜로 사용
        Text(
            text = product.productName,
            //style = Typography.bodyMedium,
            modifier = Modifier
                .padding(start = 5.dp, end = 5.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )



        // 리뷰 정보
        if (product.productReviewCount > 0) {
            Row(
                modifier = Modifier
                    .padding(start = 5.dp, top = 2.dp, bottom = 10.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 좋아요
                Text(
                    text = "${product.productReviewCount}",
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 5.dp),
                    color = Color.Gray
                )

                // 리뷰
                Text(
                    text = "${product.productReviewCount}",
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 20.dp),
                    color = Color.Gray
                )
            }
        }
    }
}
