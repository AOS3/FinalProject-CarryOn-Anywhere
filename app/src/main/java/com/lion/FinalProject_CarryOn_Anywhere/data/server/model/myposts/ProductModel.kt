package com.lion.FinalProject_CarryOn_Anywhere.data.server.model.myposts

// 임시 파일입니다
// 내용 및 파일 이름 추후 수정

data class ProductModel(

    val productTitleName: String, // 여행 후기 제목
    val productPeriod : String, // 여행 기간 -> string은 임시
    val productImages: List<String>,
    val productReviewCount: Int, // 리뷰 수
    val productLikeCount: Int,// 좋아요 수

)