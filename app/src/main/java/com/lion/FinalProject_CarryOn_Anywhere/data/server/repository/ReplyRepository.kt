package com.lion.FinalProject_CarryOn_Anywhere.data.server.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.lion.FinalProject_CarryOn_Anywhere.data.server.model.CarryTalkModel
import com.lion.FinalProject_CarryOn_Anywhere.data.server.model.ReplyModel
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.ReplyState
import kotlinx.coroutines.tasks.await

class ReplyRepository {

    companion object {

        // 0221 khs 댓글 관련 추가 부분

        // 댓글 불러오기
        // 특정 게시글에 대한 댓글 불러오기
        suspend fun getAllReplysByTalkDocId(boardDocumentId: String): List<ReplyModel> {
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("CarryTalkData")

            // 특정 게시글에 대한 댓글 가져오기
            val result = collectionReference
                .whereEqualTo("boardDocumentId", boardDocumentId) // 필드명 수정
                .get()
                .await()

            if (result.documents.isNotEmpty()) {
                // Firestore에서 가져온 데이터를 `ReplyModel` 리스트로 변환 후 반환
                return result.documents.mapNotNull { document ->
                    document.toObject(ReplyModel::class.java)
                }
            } else {
                return emptyList() // 댓글이 없으면 빈 리스트 반환
            }
        }


        // 댓글 추가
        // ReplyData에 저장 후 CarryTalkData의 특정 게시글(talkDocumentId)의 talkReplyList에 추가
        suspend fun addReply(talkDocumentId: String, replyModel: ReplyModel): String {
            val fireStore = FirebaseFirestore.getInstance()
            val replyCollection = fireStore.collection("ReplyData")
            val talkCollection = fireStore.collection("CarryTalkData")

            // `ReplyData`에 댓글 추가
            val replyDocumentReference = replyCollection.document()
            replyModel.replyDocumentId = replyDocumentReference.id
            replyDocumentReference.set(replyModel).await()

            // `CarryTalkData`에 댓글 추가
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
            return replyModel.replyDocumentId // ✅ 성공적으로 추가된 댓글 ID 반환
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
        suspend fun updateReplyState(replyDocumentId: String, newState: ReplyState) {
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("ReplyData")
            val documentReference = collectionReference.document(replyDocumentId)

            val updateMap = mapOf(
                "replyState" to newState.number
            )
            documentReference.update(updateMap).await()
        }
    }
}