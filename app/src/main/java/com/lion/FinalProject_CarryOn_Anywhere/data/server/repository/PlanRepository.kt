package com.lion.FinalProject_CarryOn_Anywhere.data.server.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.lion.FinalProject_CarryOn_Anywhere.data.server.vo.PlanVO
import com.lion.FinalProject_CarryOn_Anywhere.data.server.vo.TripVO
import kotlinx.coroutines.tasks.await

class PlanRepository {

    // 일정 정보를 Firestore에 추가하고 `documentId`를 반환하는 메서드
    suspend fun addPlanData(planVO: PlanVO): String? {
        return try {
            val firestore = FirebaseFirestore.getInstance()
            val documentRef = firestore.collection("PlanData").add(planVO).await() // ✅ `.await()` 사용하여 documentId 가져오기
            documentRef.id // ✅ Firestore에서 자동 생성된 `documentId` 반환
        } catch (e: Exception) {
            Log.e("TripRepository", "⚠ 여행 데이터 저장 실패", e)
            null
        }
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

    suspend fun updatePlanByDocumentIdAndDay(tripDocumentId: String, day: String, newPlaceList: List<Map<String, Any?>>) {
        try {
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("PlanData")

            // `tripDocumentId`와 `planDay`가 같은 문서 조회
            val querySnapshot = collectionReference
                .whereEqualTo("tripDocumentId", tripDocumentId)
                .whereEqualTo("planDay", day)
                .get()
                .await()

            if (querySnapshot.documents.isNotEmpty()) {
                val documentRef = querySnapshot.documents.first().reference

                // 🔹 기존 문서가 존재하면 업데이트 수행
                documentRef.update("placeList", newPlaceList).await()

                // 🔹 Firestore에서 업데이트 후 최신 데이터 가져와 출력
                val updatedDoc = documentRef.get().await()
                val updatedPlaceList = updatedDoc.get("placeList") as? List<Map<String, Any?>>
                Log.d("PlanService", "newPlaceList: $newPlaceList")
                Log.d("PlanService", "✅ Firestore PlanData 업데이트 완료: $tripDocumentId - $day")
                Log.d("PlanService", "🔹 업데이트된 placeList: $updatedPlaceList")
            } else {
                Log.e("PlanService", "🚨 Firestore에서 기존 문서를 찾을 수 없음: $tripDocumentId - $day")
            }
        } catch (e: Exception) {
            Log.e("PlanService", "❌ Firestore PlanData 업데이트 실패: ${e.message}")
        }
    }

}