package com.lion.FinalProject_CarryOn_Anywhere.data.server.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.lion.FinalProject_CarryOn_Anywhere.data.server.vo.TripVO
import kotlinx.coroutines.tasks.await

class TripRepository {

    // 여행 정보를 Firestore에 추가하고 `documentId`를 반환하는 메서드
    suspend fun addTripData(tripVO: TripVO): String? {
        return try {
            val firestore = FirebaseFirestore.getInstance()
            val documentRef = firestore.collection("TripData").add(tripVO).await() // ✅ `.await()` 사용하여 documentId 가져오기
            documentRef.id // ✅ Firestore에서 자동 생성된 `documentId` 반환
        } catch (e: Exception) {
            Log.e("TripRepository", "⚠ 여행 데이터 저장 실패", e)
            null
        }
    }

    // 여행 데이터를 수정한다.
    suspend fun updateTripDate(tripVO: TripVO, tripDocumentId: String) {
        // 수정할 데이터를 담을 맵
        val customerMap = mapOf(
            "tripStartDate" to tripVO.tripStartDate,
            "tripEndDate" to tripVO.tripEndDate,
        )
        // 수정할 문서에 접근할 수 있는 객체를 가져온다.
        val firestore = FirebaseFirestore.getInstance()
        val collectionReference = firestore.collection("TripData")
        val documentReference = collectionReference.document(tripDocumentId)
        documentReference.update(customerMap).await()
    }

    // 여행 데이터를 수정한다.
    suspend fun updateTripPlanList(tripVO: TripVO, tripDocumentId: String) {
        val firestore = FirebaseFirestore.getInstance()
        val collectionReference = firestore.collection("TripData")
        val documentReference = collectionReference.document(tripDocumentId)

        try {
            // Firestore에서 기존 planList 가져오기
            val snapshot = documentReference.get().await()
            val existingPlanList = snapshot.get("planList") as? MutableList<String> ?: mutableListOf()

            // 새로운 planDocumentId 추가 후 중복 제거
            val updatedPlanList = (existingPlanList + tripVO.planList).distinct()

            // 업데이트할 데이터 맵 생성
            val updateMap = mapOf("planList" to updatedPlanList)

            // Firestore 업데이트 실행
            documentReference.update(updateMap).await()

            Log.d("TripRepository", "Firestore planList 업데이트 완료: $updatedPlanList")
        } catch (e: Exception) {
            Log.e("TripRepository", "Firestore planList 업데이트 실패", e)
        }
    }

    // 글의 문서 id를 통해 글 데이터를 가져온다.
    suspend fun selectTripDataOneById(documentId:String) : TripVO{
        val firestore = FirebaseFirestore.getInstance()
        val collectionReference = firestore.collection("TripData")
        val documentReference = collectionReference.document(documentId)
        val documentSnapShot = documentReference.get().await()
        val tripVO = documentSnapShot.toObject(TripVO::class.java)!!
        return tripVO
    }
}