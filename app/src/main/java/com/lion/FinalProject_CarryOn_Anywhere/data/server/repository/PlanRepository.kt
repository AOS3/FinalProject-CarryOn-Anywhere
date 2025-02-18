package com.lion.FinalProject_CarryOn_Anywhere.data.server.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.lion.FinalProject_CarryOn_Anywhere.data.server.vo.PlanVO
import kotlinx.coroutines.tasks.await

class PlanRepository {

    // 사용자 정보를 추가하는 메서드
    suspend fun addPlanData(planVO: PlanVO){
        val firestore = FirebaseFirestore.getInstance()
        val collectionReference = firestore.collection("PlanData")
        collectionReference.add(planVO)
    }

    suspend fun getPlanByDocumentIdAndDay(tripDocumentId: String, day: String): PlanVO? {
        val firestore = FirebaseFirestore.getInstance()
        val collectionReference = firestore.collection("PlanData")

        // `tripDocumentId`와 `planDay`가 같은 문서 조회
        val querySnapshot = collectionReference
            .whereEqualTo("tripDocumentId", tripDocumentId)
            .whereEqualTo("planDay", day)
            .get()
            .await()

        return if (!querySnapshot.isEmpty) {
            val document = querySnapshot.documents.first()
            val planVO = document.toObject(PlanVO::class.java)

            // `placeList`를 `Map<String, Any?>`으로 유지
            planVO?.placeList = document.get("placeList") as? MutableList<Map<String, Any?>> ?: mutableListOf()

            planVO
        } else {
            null
        }
    }

    suspend fun updatePlanData(planVO: PlanVO) {
        val firestore = FirebaseFirestore.getInstance()
        val collectionReference = firestore.collection("PlanData")

        val querySnapshot = collectionReference
            .whereEqualTo("tripDocumentId", planVO.tripDocumentId)
            .whereEqualTo("planDay", planVO.planDay)
            .get()
            .await()

        if (!querySnapshot.isEmpty) {
            val documentId = querySnapshot.documents.first().id

            // Firestore에서 기존 리스트 가져오기
            val existingPlaceList = querySnapshot.documents.first().get("placeList") as? MutableList<Map<String, Any?>> ?: mutableListOf()

            // `planVO.placeList`가 비어있는지 확인
            if (planVO.placeList.isEmpty()) {
                return
            }

            // `planVO.placeList`의 마지막 장소와 기존 리스트의 마지막 장소를 비교
            val lastExistingPlace = existingPlaceList.lastOrNull()
            val lastNewPlace = planVO.placeList.lastOrNull()

            // 마지막 장소와 새로운 장소가 동일하면 추가하지 않음
            if (lastExistingPlace != null && lastNewPlace != null &&
                lastExistingPlace["contentid"] == lastNewPlace["contentid"]) {
                Log.d("updatePlanData", "🚫 마지막 장소와 동일하여 추가하지 않음!")
                return
            }

            // 새로운 장소 추가
            existingPlaceList.add(lastNewPlace!!)

            // Firestore에 업데이트된 리스트 저장
            collectionReference.document(documentId)
                .update("placeList", existingPlaceList)
                .await()
        }
    }
}