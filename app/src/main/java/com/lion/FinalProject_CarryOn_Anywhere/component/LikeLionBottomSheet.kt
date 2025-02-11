package com.lion.FinalProject_CarryOn_Anywhere.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lion.finalprojectshoppingmallservice3team.customer.ui.viewmodel.shop.ProductInfoViewModel
import com.lion.finalprojectshoppingmallservice3team.ui.theme.MainColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LikeLionBottomSheet(
    onDismissRequest: () -> Unit,
    productPrice: Long, // 상품 가격
    selectedSize: String?,
    selectedColor: String?,
    viewModel : ProductInfoViewModel = hiltViewModel()
) {
    var quantity by remember { mutableStateOf(1) }


    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        containerColor  = Color.White,
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .background(Color.White)
            ) {
//                // 닫기 버튼
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.End
//                ) {
//                    IconButton(onClick = onDismissRequest) {
//                        Icon(
//                            imageVector = Icons.Default.Close,
//                            contentDescription = "Close",
//                            tint = Color.Gray
//                        )
//                    }
//                }

                // 사이즈 및 컬러가 존재하는 경우에만 표시
                Spacer(modifier = Modifier.height(8.dp))
                if (selectedSize != null) {
                    Text(text = "사이즈: $selectedSize")
                }
                if (selectedColor != null) {
                    Spacer(modifier = Modifier.height(4.dp)) // 사이즈와 컬러 사이에 간격 추가
                    Text(text = "컬러: $selectedColor")
                }

                // 수량 변경
                Spacer(modifier = Modifier.height(10.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween,  verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "수량",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .background(Color.LightGray, shape = RoundedCornerShape(5.dp))
                    ){
                        IconButton(onClick = { if (quantity > 1) quantity-- }) {
                            Icon(
                                imageVector = Icons.Default.Remove,
                                contentDescription = "Decrease Quantity"
                            )
                        }

                        Text(text = "$quantity",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(8.dp),
                            )

                        IconButton(onClick = { quantity++ }) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Increase Quantity"
                            )
                        }
                    }
                }

                // Divider
                Spacer(modifier = Modifier.height(10.dp))
                LikeLionDivider()

                // 총 상품 금액
                Spacer(modifier = Modifier.height(10.dp))
                val totalPrice = productPrice * quantity
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Text(
                        text = "총 상품 금액",
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${String.format("%,d", totalPrice)}원",
                        fontWeight = FontWeight.Bold
                    )
                }

                // 버튼들
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    LikeLionFilledButton(
                        onClick = { viewModel.shoppingCartButtonOnClick()},
                        modifier = Modifier.weight(1f),
                        contentColor = MainColor,
                        containerColor = Color.White,
                        border = BorderStroke(1.dp, MainColor),
                        text = "장바구니"
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    LikeLionFilledButton(
                        onClick = { viewModel.shopOrderSheetWriteButtonOnClick() },
                        modifier = Modifier.weight(1f),
                        text = "바로 구매하기"
                    )
                }
            }
        }
    )
}