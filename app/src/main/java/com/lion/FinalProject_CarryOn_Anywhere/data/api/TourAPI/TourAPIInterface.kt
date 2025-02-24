package com.lion.FinalProject_CarryOn_Anywhere.data.api.TourAPI

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TourAPIInterface {
    @GET("areaCode1")
    suspend fun getRegionCodes(
        @Query("serviceKey") serviceKey: String,
        @Query("numOfRows") numOfRows: Int = 50,
        @Query("pageNo") pageNo: Int = 1,
        @Query("MobileOS") mobileOS: String = "AND",
        @Query("MobileApp") mobileApp: String = "AppTest",
        @Query("_type") type: String = "json",
    ): Response<TourApiModel.AreaCodeResponse>

    @GET("areaCode1")
    suspend fun getSubRegions(
        @Query("serviceKey") serviceKey: String,
        @Query("MobileOS") mobileOS: String = "AND",
        @Query("MobileApp") mobileApp: String = "AppTest",
        @Query("_type") type: String = "json",
        @Query("numOfRows") numOfRows: Int = 50,
        @Query("pageNo") pageNo: Int = 1,
        @Query("areaCode") areaCode: String
    ): Response<TourApiModel.AreaCodeResponse>

    @GET("areaBasedList1")
    suspend fun getPlaces(
        @Query("serviceKey") serviceKey: String,
        @Query("numOfRows") numOfRows: Int = 20,
        @Query("pageNo") pageNo: Int = 1,
        @Query("MobileOS") mobileOS: String = "AND",
        @Query("MobileApp") mobileApp: String = "AppTest",
        @Query("_type") type: String = "json",
        @Query("areaCode") areaCode: String, // String 타입으로 변경
        @Query("sigunguCode") sigunguCode: String? = null, // 선택적 파라미터 유지
        @Query("contentTypeId") contentTypeId: String? = null // 선택적 파라미터 유지
    ): Response<TourApiModel.TouristSpotResponse>

    // 키워드 검색
    @GET("searchKeyword1")
    suspend fun getSearchPlaces(
        @Query("serviceKey") serviceKey: String,
        @Query("numOfRows") numOfRows: Int = 10,
        @Query("pageNo") pageNo: Int = 1,
        @Query("MobileOS") mobileOS: String = "AND",
        @Query("MobileApp") mobileApp: String = "AppTest",
        @Query("keyword") keyword: String,
        @Query("_type") type: String = "json",
        @Query("listYN") listYN:String = "Y",
        @Query("arrange") arrange:String = "C",
    ): Response<TourApiModel.TouristSpotResponse>

    // 공통 정보 조회
    @GET("detailCommon1")
    suspend fun getDetailCommon1(
        @Query("serviceKey") serviceKey: String,
        @Query("contentId") contentId: String,
        @Query("contentTypeId") contentTypeId: String,
        @Query("MobileOS") mobileOS: String = "AND",
        @Query("MobileApp") mobileApp: String = "AppTest",
        @Query("_type") type: String = "json",
        @Query("defaultYN") defaultYN:String = "Y",
        @Query("firstImageYN") firstImageYN:String = "Y",
        @Query("areacodeYN") areacodeYN:String = "Y",
        @Query("catcodeYN") catcodeYN:String = "Y",
        @Query("addrinfoYN") addrinfoYN:String = "Y",
        @Query("overviewYN") overviewYN:String = "Y",
        ): Response<TourApiModel.TouristSpotDetailResponse>
}