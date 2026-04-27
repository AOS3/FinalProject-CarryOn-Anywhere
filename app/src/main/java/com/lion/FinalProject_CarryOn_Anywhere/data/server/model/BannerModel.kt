package com.lion.FinalProject_CarryOn_Anywhere.data.server.model

import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.BannerState
import com.lion.FinalProject_CarryOn_Anywhere.data.server.vo.BannerVO

class BannerModel {
    // 배너 문서 아이디
    var bannerDocumentId:String = ""
    // 배너 제목
    var bannerTitle:String = ""
    // 배너 이미지
    var bannerImage:String = ""
    // 배너 이동 URL (Web)
    var bannerDeepLink:String = ""
    // 배너 상태
    var bannerState = BannerState.BANNER_STATE_ENABLE
    // 데이터 생성 시간
    var bannerTimeStamp = 0L

    fun toBannerVO() : BannerVO {
        val bannerVO = BannerVO()

        bannerVO.bannerTitle = bannerTitle
        bannerVO.bannerImage = bannerImage
        bannerVO.bannerDeepLink = bannerDeepLink
        bannerVO.bannerTimeStamp = bannerTimeStamp
        bannerVO.bannerState = bannerState.number


        return bannerVO
    }
}