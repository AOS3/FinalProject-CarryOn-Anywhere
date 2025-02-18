package com.lion.FinalProject_CarryOn_Anywhere.data.api.TourAPI

class TourApiModel {
    data class AreaCodeResponse(
        val response: AreaCodeBody?
    )

    data class AreaCodeBody(
        val header: ResponseHeader?,
        val body: AreaCodeItems?
    )

    data class ResponseHeader(
        val resultCode: String?,
        val resultMsg: String?
    )

    data class AreaCodeItems(
        val items: AreaCodeList?,  // ✅ items를 객체로 감싸야 함
        val numOfRows: Int?,
        val pageNo: Int?,
        val totalCount: Int?
    )

    data class AreaCodeList(
        val item: List<AreaCodeItem>? // ✅ `item` 배열이므로 List로 정의
    )

    data class AreaCodeItem(
        val rnum: Int?, // ✅ JSON에 있는 `rnum` 추가
        val code: String?,  // ✅ API에서 "code": "1" (문자열)로 반환됨 → `String` 사용
        val name: String?
    )

    data class TouristSpotResponse(
        val response: TouristSpotBody?
    )

    data class TouristSpotBody(
        val body: TouristSpotItems?
    )

    data class TouristSpotItems(
        val items: TouristSpotList?
    )

    data class TouristSpotList(
        val item: List<TouristSpotItem>?
    )

    data class TouristSpotItem(
        val title: String?,       // 관광지명
        val addr1: String?,       // 기본 주소
        val addr2: String?,       // 상세 주소
        val areacode: String?,    // 지역 코드
        val sigungucode: String?, // 시군구 코드
        val contentid: String?,   // 관광지 콘텐츠 ID
        val contenttypeid: String?, // 콘텐츠 유형 ID
        val firstimage: String?,  // 대표 이미지
        val firstimage2: String?, // 보조 이미지
        val mapx: String?,        // 경도
        val mapy: String?,        // 위도
        val tel: String?,         // 전화번호
        val zipcode: String?      // 우편번호
    )
}
