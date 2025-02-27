package com.lion.FinalProject_CarryOn_Anywhere.data.server.repository

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.lion.FinalProject_CarryOn_Anywhere.data.server.model.ReplyModel
import com.lion.FinalProject_CarryOn_Anywhere.data.server.model.TripReviewModel
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.CarryTalkState
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.ReplyState
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.TripReviewState
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

        // 여행 후기 데이터 수정
        suspend fun updateTripReview(documentId: String, newTitle: String, newContent: String, newImageUrls: List<String>,
                                     newShareTitle: String, newTripDate: String, newSharePlace: List<String>, newSharePlan: List<Map<String, String>>) {
            try {
                val updateData = mapOf(
                    "tripReviewTitle" to newTitle,
                    "tripReviewContent" to newContent,
                    "tripReviewImage" to newImageUrls,
                    "tripReviewShareTitle" to newShareTitle,
                    "tripReviewShareDate" to newTripDate,
                    "tripReviewSharePlace" to newSharePlace,
                    "tripReviewSharePlan" to newSharePlan
                )
                collection.document(documentId).update(updateData).await()
            } catch (e: Exception) {
                throw e
            }
        }


        // 나의 여행 후기 가져오기
        suspend fun getMyTripReviews(userDocumentId:String): List<TripReviewModel> {

            val firestore = FirebaseFirestore.getInstance()
            val talkCollectionReference = firestore.collection("TripReviewData")

            // 특정 게시글에 대한 댓글 가져오기 : TripReviewData
            val result = talkCollectionReference
                .whereEqualTo("userDocumentId", userDocumentId)
                .whereEqualTo("tripReviewState", TripReviewState.TRIP_REVIEW_STATE_NORMAL)
                .get()
                .await()

            if (result.documents.isNotEmpty()) {
                // Firestore에서 가져온 데이터를 `TripReviewModel` 리스트로 변환 후 반환
                return result.documents.mapNotNull { document ->
                    document.toObject(TripReviewModel::class.java)
                }
            }
            else {
                return emptyList() // 댓글이 없으면 빈 리스트 반환
            }
        }


        // 좋아요 기능 추가
        suspend fun toggleLike(documentId: String, userId: String): Boolean {
            return try {
                val docRef = collection.document(documentId)
                val snapshot = docRef.get().await()
                val likeUserList = snapshot.get("tripReviewLikeUserList") as? MutableList<String> ?: mutableListOf()
                var likeCount = snapshot.getLong("tripReviewLikeCount")?.toInt() ?: 0

                if (likeUserList.contains(userId)) {
                    // 이미 좋아요를 눌렀다면 취소
                    likeUserList.remove(userId)
                    likeCount -= 1
                } else {
                    // 좋아요 추가
                    likeUserList.add(userId)
                    likeCount += 1
                }

                val updateData = mapOf(
                    "tripReviewLikeCount" to likeCount,
                    "tripReviewLikeUserList" to likeUserList
                )

                docRef.update(updateData).await()
                likeUserList.contains(userId) // 현재 좋아요 상태 반환
            } catch (e: Exception) {
                throw e
            }
        }

        // 특정 여행 후기의 좋아요 상태 가져오기
        suspend fun getLikeStatus(documentId: String, userId: String): Boolean {
            return try {
                val snapshot = collection.document(documentId).get().await()
                val likeUserList = snapshot.get("tripReviewLikeUserList") as? List<String> ?: emptyList()
                likeUserList.contains(userId)
            } catch (e: Exception) {
                false
            }
        }
    }
}
