package com.lion.FinalProject_CarryOn_Anywhere.data.server.vo

import com.lion.FinalProject_CarryOn_Anywhere.data.server.model.BannerModel
import com.lion.FinalProject_CarryOn_Anywhere.data.server.model.RequestModel
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.BannerState
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.RequestState

class BannerVO {
    // 배너 제목
    var bannerTitle:String = ""
    // 배너 이미지
    var bannerImage:String = ""
    // 배너 이동 URL (Web)
    var bannerDeepLink:String = ""
    // 배너 상태
    var bannerState:Int = 1
    // 데이터 생성 시간
    var bannerTimeStamp:Long = 0L

    fun toBannerModel(bannerDocumentId:String) : BannerModel {
        val bannerModel = BannerModel()

        bannerModel.bannerDocumentId = bannerDocumentId
        bannerModel.bannerTitle = bannerTitle
        bannerModel.bannerImage = bannerImage
        bannerModel.bannerDeepLink = bannerDeepLink
        bannerModel.bannerTimeStamp = bannerTimeStamp

        when(bannerState){
            BannerState.BANNER_STATE_ENABLE.number -> bannerModel.bannerState = BannerState.BANNER_STATE_ENABLE
            BannerState.BANNER_STATE_DISABLE.number -> bannerModel.bannerState = BannerState.BANNER_STATE_DISABLE
        }

        return bannerModel
    }
}