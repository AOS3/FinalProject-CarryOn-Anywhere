package com.lion.FinalProject_CarryOn_Anywhere.data.server.service

import android.net.Uri
import android.util.Log
import com.lion.FinalProject_CarryOn_Anywhere.data.server.model.BannerModel
import com.lion.FinalProject_CarryOn_Anywhere.data.server.repository.BannerRepository
import com.lion.FinalProject_CarryOn_Anywhere.data.server.vo.BannerVO

class BannerService(val bannerRepository: BannerRepository) {

    suspend fun gettingBannerList(): MutableList<BannerModel> {
        val bannerList = mutableListOf<BannerModel>()

        try {
            val resultList = bannerRepository.gettingBannerList()
            Log.d("BannerDebug", "BannerService에서 가져온 배너 리스트 크기: ${resultList.size}")

            resultList.forEach {
                val bannerVO = it["bannerVO"] as? BannerVO
                val documentId = it["documentId"] as? String

                if (bannerVO != null && documentId != null) {
                    val bannerModel = bannerVO.toBannerModel(documentId)
                    bannerList.add(bannerModel)
                } else {

                }
            }

            return bannerList
        } catch (e: Exception) {
            Log.e("BannerError", "배너 데이터를 가져오는 중 오류 발생: ${e.message}")
            return mutableListOf()
        }
    }

    // 이미지 데이터를 가져온다.
    suspend fun gettingImage(imageFileName:String) : Uri? {
        val imageUri = bannerRepository.gettingImage(imageFileName)
        return imageUri
    }
}