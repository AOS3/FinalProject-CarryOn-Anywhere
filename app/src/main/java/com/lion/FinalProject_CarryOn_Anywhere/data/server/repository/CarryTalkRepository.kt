package com.lion.FinalProject_CarryOn_Anywhere.data.server.repository

import android.util.Log
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.lion.FinalProject_CarryOn_Anywhere.data.server.model.CarryTalkModel
import com.lion.FinalProject_CarryOn_Anywhere.data.server.model.ReplyModel
import com.lion.FinalProject_CarryOn_Anywhere.data.server.repository.TripReviewRepository.Companion
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.ReplyState
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.UserState
import com.lion.FinalProject_CarryOn_Anywhere.data.server.vo.ReplyVO
import kotlinx.coroutines.tasks.await

class CarryTalkRepository {
    companion object {

        private val collection by lazy {
            FirebaseFirestore.getInstance().collection("CarryTalkData")
        }

        // Firestoere에 데이터 추가
        suspend fun addCarryTalkData(carryTalkModel: CarryTalkModel): DocumentReference {
            val documentRef = collection.document()
            carryTalkModel.talkDocumentId = documentRef.id
            documentRef.set(carryTalkModel).await()
            return documentRef
        }

        // 모든 여행 이야기 데이터 가져오기
        suspend fun getAllCarryTalks(): List<CarryTalkModel> {
            return try {
                val documents = collection.get().await().documents

                documents.mapNotNull { doc ->
                    val carryTalk = doc.toObject(CarryTalkModel::class.java)
                    carryTalk?.apply { talkDocumentId = doc.id }
                }
            } catch (e: Exception) {
                emptyList()
            }
        }

        // talkState 상태 변경(삭제 - 안 보이게 처리)
        suspend fun updateCarryTalkState(documentId: String, state: String) {
            try {
                collection.document(documentId).update("talkState", state).await()
            } catch (e: Exception) {
                throw e
            }
        }
    }
}
