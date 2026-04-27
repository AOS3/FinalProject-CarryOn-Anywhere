package com.lion.FinalProject_CarryOn_Anywhere.data.server.repository

import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.lion.FinalProject_CarryOn_Anywhere.data.server.vo.BannerVO
import kotlinx.coroutines.tasks.await

class BannerRepository {

    // 배너 목록을 가져오는 메서드
    suspend fun gettingBannerList() : MutableList<Map<String, *>>{
        val firestore = FirebaseFirestore.getInstance()
        val collectionReference = firestore.collection("BannerData")

        try {
            val result = collectionReference
                .orderBy("bannerTimeStamp", Query.Direction.DESCENDING)
                .get()
                .await()
            Log.d("BannerDebug", "Firestore에서 가져온 문서 개수: ${result.size()}")

            // 반환할 리스트
            val resultList = mutableListOf<Map<String, *>>()
            // 데이터의 수 만큼 반환한다.
            result.forEach {
                val map = mapOf(
                    // 문서의 id
                    "documentId" to it.id,
                    // 데이터를 가지고 있는 객체
                    "bannerVO" to it.toObject(BannerVO::class.java)
                )
                Log.d("BannerDebug", "Firestore에서 가져온 배너 데이터: $map")

                resultList.add(map)
            }
            return resultList
        } catch (e:Exception) {
            return mutableListOf()
        }
    }

    // 이미지 데이터를 가져온다.
    suspend fun gettingImage(imageFileName:String) : Uri {
        val storageReference = FirebaseStorage.getInstance().reference
        // 파일명을 지정하여 이미지 데이터를 가져온다.
        val childStorageReference = storageReference.child("images/$imageFileName")
        val imageUri = childStorageReference.downloadUrl.await()
        return imageUri
    }
}