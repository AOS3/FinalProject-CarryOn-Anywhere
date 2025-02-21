package com.lion.FinalProject_CarryOn_Anywhere.data.server.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.lion.FinalProject_CarryOn_Anywhere.data.server.vo.RequestVO

class RequestRepository {

    // 요청 정보를 추가하는 메서드
    fun addRequestData(requestVO: RequestVO){
        val firestore = FirebaseFirestore.getInstance()
        val collectionReference = firestore.collection("RequestData")
        collectionReference.add(requestVO)
    }
}