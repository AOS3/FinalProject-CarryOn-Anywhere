package com.lion.FinalProject_CarryOn_Anywhere.data.server.model

// 임시 파일입니다
// 내용 및 파일 이름 추후 수정

data class ProductModel(
    val productName: String,
    val productSellerName: String,
    val productImages: List<String>,
    val productPrice: Int,
    val productReviewCount: Int,
    val productRating: Float,
    val productManagementAllQuantity: Long,
    val productLimitedSalesPeriod: String
)