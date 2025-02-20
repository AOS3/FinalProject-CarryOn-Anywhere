package com.lion.FinalProject_CarryOn_Anywhere.data.api.TourAPI

object TourApiHelper {

    // 지역 코드
    val areaCodeMap = mapOf(
        "1" to "서울",
        "2" to "인천",
        "3" to "대전",
        "4" to "대구",
        "5" to "광주",
        "6" to "부산",
        "7" to "울산",
        "8" to "세종",
        "31" to "경기도",
        "32" to "강원도",
        "33" to "충청북도",
        "34" to "충청남도",
        "35" to "경상북도",
        "36" to "경상남도",
        "37" to "전라북도",
        "38" to "전라남도",
        "39" to "제주도"
    )

    // 콘텐츠 유형 코드
    val contentTypeMap = mapOf(
        "12" to "관광지",
        "14" to "문화시설",
        "15" to "축제공연행사",
        "25" to "여행코스",
        "28" to "레포츠",
        "32" to "숙박",
        "38" to "쇼핑",
        "39" to "음식점"
    )

    // 지역 코드 변환 함수
    fun getAreaName(areaCode: String?): String {
        return areaCodeMap[areaCode] ?: "지역 정보 없음"
    }

    // 콘텐츠 유형 변환 함수
    fun getContentType(contentTypeId: String?): String {
        return contentTypeMap[contentTypeId] ?: "기타"
    }
}