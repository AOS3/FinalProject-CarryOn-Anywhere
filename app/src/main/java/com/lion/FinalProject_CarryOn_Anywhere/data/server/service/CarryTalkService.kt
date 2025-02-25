package com.lion.FinalProject_CarryOn_Anywhere.data.server.service

import android.util.Log
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.lion.FinalProject_CarryOn_Anywhere.data.server.model.CarryTalkModel
import com.lion.FinalProject_CarryOn_Anywhere.data.server.model.ReplyModel
import com.lion.FinalProject_CarryOn_Anywhere.data.server.repository.CarryTalkRepository
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.ReplyState
import com.lion.FinalProject_CarryOn_Anywhere.data.server.repository.TripReviewRepository
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.TalkTag
import kotlinx.coroutines.tasks.await

class CarryTalkService {
    companion object {

        // 여행 이야기 데이터 추가
        suspend fun addCarryTalkReview(carryTalkModel: CarryTalkModel): DocumentReference {
            return CarryTalkRepository.addCarryTalkData(carryTalkModel)
        }

        // 모든 여행 이야기 가져오기
        suspend fun fetchAllCarryTalks(): List<CarryTalkModel> {
            val result = CarryTalkRepository.getAllCarryTalks()
            return result
        }

        // 여행 후기 삭제 (talkState 변경)
        suspend fun deleteCarryTalkReview(documentId: String) {
            CarryTalkRepository.updateCarryTalkState(documentId, "CARRYTALK_STATE_DELETE")
        }

        // 여행 후기 데이터 수정
        suspend fun updateCarryTalk(documentId: String, newTag: String, newTitle: String, newContent: String, newImageUrls: List<String>) {
            CarryTalkRepository.updateCarryTalk(documentId, newTag, newTitle, newContent, newImageUrls)
        }
    }
}
