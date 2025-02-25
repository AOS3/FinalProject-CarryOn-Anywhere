package com.lion.FinalProject_CarryOn_Anywhere.data.server.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.lion.FinalProject_CarryOn_Anywhere.data.server.model.CarryTalkModel
import com.lion.FinalProject_CarryOn_Anywhere.data.server.model.ReplyModel
import com.lion.FinalProject_CarryOn_Anywhere.data.server.model.TripReviewModel
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.ReplyState
import kotlinx.coroutines.tasks.await

class ReplyRepository {

    companion object {

        // 0221 khs 댓글 관련 추가 부분

        // 댓글 불러오기
        // 특정 게시글에 대한 댓글 불러오기
        suspend fun getAllReplysByTalkDocId(boardDocumentId: String): List<ReplyModel> {
            val firestore = FirebaseFirestore.getInstance()
            val talkCollectionReference = firestore.collection("ReplyData")

            // 특정 게시글에 대한 댓글 가져오기 : CarryTalkData
            val result1 = talkCollectionReference
                .whereEqualTo("boardDocumentId", boardDocumentId)
                .whereEqualTo("replyState", ReplyState.REPLY_STATE_NORMAL)
                .get()
                .await()

            if (result1.documents.isNotEmpty()) {
                // Firestore에서 가져온 데이터를 `ReplyModel` 리스트로 변환 후 반환
                return result1.documents.mapNotNull { document ->
                    document.toObject(ReplyModel::class.java)
                }
            }
            else {
                return emptyList() // 댓글이 없으면 빈 리스트 반환
            }
        }


        // 댓글 추가
        // ReplyData에 저장 후 CarryTalkData의 특정 게시글(talkDocumentId)의 talkReplyList에 추가
        suspend fun addReply(talkDocumentId: String, replyModel: ReplyModel): String {
            val fireStore = FirebaseFirestore.getInstance()
            val replyCollection = fireStore.collection("ReplyData") // 댓글 DB
            val talkCollection = fireStore.collection("CarryTalkData") // 여행이야기 DB
            val reviewCollection = fireStore.collection("TripReviewData") // 여행 후기

            // `ReplyData`에 댓글 추가
            val replyDocumentReference = replyCollection.document()
            replyModel.replyDocumentId = replyDocumentReference.id
            replyModel.boardDocumentId = talkDocumentId
            replyDocumentReference.set(replyModel).await()
            Log.d("test100","1 `ReplyData`에 댓글 추가")

            // 게시글이 여행 이야기인 경우 `CarryTalkData`에 댓글 추가
            // Firestore에서 해당 게시글 가져오기
            val talkDocumentReference = talkCollection.document(talkDocumentId)
            val talkSnapshot = talkDocumentReference.get().await()

            if (talkSnapshot.exists()) {
                val talkModel = talkSnapshot.toObject(CarryTalkModel::class.java)
                talkModel?.let {
                    val updatedReplyList = it.talkReplyList.toMutableList().apply {
                        add(replyModel.replyDocumentId) // 새 댓글 ID 추가
                    }

                    // `talkReplyList` 업데이트
                    talkDocumentReference.update("talkReplyList", updatedReplyList).await()
                }
            }

            // 게시글이 여행 후기인 경우 `TripReviewData`에 댓글 추가
            // Firestore에서 해당 게시글 가져오기
            val reviewDocumentReference = reviewCollection.document(talkDocumentId)
            val reviewSnapshot = reviewDocumentReference.get().await()

            if (reviewSnapshot.exists()) {
                val reviewModel = reviewSnapshot.toObject(TripReviewModel::class.java)
                reviewModel?.let {
                    val updatedReplyList = it.tripReviewReplyList.toMutableList().apply {
                        add(replyModel.replyDocumentId) // 새 댓글 ID 추가
                    }

                    // `talkReplyList` 업데이트
                    reviewDocumentReference.update("tripReviewReplyList", updatedReplyList).await()
                }
            }
            return replyModel.replyDocumentId // 성공적으로 추가된 댓글 ID 반환
        }


        // 댓글 글 데이터(CarryTalkData, TripReviewData)의 댓글 리스트(talkReplyList, tripReviewReplyList)에서 삭제하기
        suspend fun deleteReplyFromList(replyDocumentId: String, talkDocumentId: String): Boolean {

            val fireStore = FirebaseFirestore.getInstance()
            val talkCollection = fireStore.collection("CarryTalkData") // 여행이야기 DB
            val reviewCollection = fireStore.collection("TripReviewData") // 여행 후기

            return try {
                // 게시글이 여행 이야기인 경우 `CarryTalkData`에서 댓글 삭제
                val talkDocumentReference = talkCollection.document(talkDocumentId)
                val talkSnapshot = talkDocumentReference.get().await()

                if (talkSnapshot.exists()) {
                    val talkModel = talkSnapshot.toObject(CarryTalkModel::class.java)
                    talkModel?.let {
                        val updatedReplyList = it.talkReplyList.toMutableList().apply {
                            remove(replyDocumentId) // 새 댓글 ID 추가
                        }

                        // `talkReplyList` 업데이트
                        talkDocumentReference.update("talkReplyList", updatedReplyList).await()
                    }
                }

                // 게시글이 여행 후기인 경우 `TripReviewData`에 댓글 추가
                // Firestore에서 해당 게시글 가져오기
                val reviewDocumentReference = reviewCollection.document(talkDocumentId)
                val reviewSnapshot = reviewDocumentReference.get().await()

                if (reviewSnapshot.exists()) {
                    val reviewModel = reviewSnapshot.toObject(TripReviewModel::class.java)
                    reviewModel?.let {
                        val updatedReplyList = it.tripReviewReplyList.toMutableList().apply {
                            remove(replyDocumentId)
                        }

                        // `talkReplyList` 업데이트
                        reviewDocumentReference.update("tripReviewReplyList", updatedReplyList).await()
                    }
                }
                true

            } catch (e: Exception) {
                // 에러 처리
                false
            }
        }


        // 댓글 수정
        // ReplyData에서 특정 댓글(replyDocumentId)을 찾아 수정
        suspend fun updateReplyData(replyDocumentId: String, replyModel: ReplyModel): String {
            val fireStore = FirebaseFirestore.getInstance()
            val replyCollection = fireStore.collection("ReplyData")

            // Firestore에서 해당 댓글 가져오기
            val replyDocumentReference = replyCollection.document(replyDocumentId)
            val replySnapshot = replyDocumentReference.get().await()

            if (replySnapshot.exists()) {
                // 업데이트할 댓글 내용
                val updateData = mapOf(
                    "replyContent" to replyModel.replyContent, // 새로운 댓글 내용
                    "replyTimeStamp" to System.currentTimeMillis() // 수정된 시간
                )
                replyDocumentReference.update(updateData).await()

                return replyModel.replyDocumentId // 성공적으로 수정된 댓글 ID 반환
            }
            return "" // 해당 댓글이 없으면 빈 문자열 반환
        }


        // 댓글 수정, 삭제 및 신고 -> 댓글 상태 변환
        suspend fun updateReplyState(replyDocumentId: String, newState: ReplyState): Boolean {

            return try {
                val firestore = FirebaseFirestore.getInstance()
                val collectionReference = firestore.collection("ReplyData")
                val documentReference = collectionReference.document(replyDocumentId)
                val updateMap = mapOf("replyState" to newState)
                documentReference.update(updateMap).await()
                true

            } catch (e: Exception) {
                // 에러 처리
                false
            }
        }
    }
}