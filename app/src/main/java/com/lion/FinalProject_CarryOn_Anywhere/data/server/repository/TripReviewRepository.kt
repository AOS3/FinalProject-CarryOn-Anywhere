package com.lion.FinalProject_CarryOn_Anywhere.data.server.repository

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.lion.FinalProject_CarryOn_Anywhere.data.server.model.TripReviewModel
import kotlinx.coroutines.tasks.await

class TripReviewRepository {
    companion object {

        private val collection by lazy { FirebaseFirestore.getInstance().collection("TripReviewData") }


        // Firestoere에 데이터 추가
        suspend fun addTripReviewData(tripReviewModel: TripReviewModel): DocumentReference {
            val documentRef = collection.document()
            tripReviewModel.tripDocumentId = documentRef.id
            documentRef.set(tripReviewModel).await()
            return documentRef
        }

        // 모든 여행 후기 데이터 가져오기
        suspend fun getAllTripReviews(): List<TripReviewModel> {
            return try {
                collection.get().await().documents.mapNotNull { doc ->
                    doc.toObject(TripReviewModel::class.java)?.apply {
                        tripReviewDocumentId = doc.id
                    }
                }
            } catch (e: Exception) {
                emptyList()
            }
        }

        // tripReviewState 상태 변경(삭제 - 안 보이게 처리)
        suspend fun updateTripReviewState(documentId: String, state: String) {
            try {
                collection.document(documentId).update("tripReviewState", state).await()
            } catch (e: Exception) {
                throw e
            }
        }
    }
}
