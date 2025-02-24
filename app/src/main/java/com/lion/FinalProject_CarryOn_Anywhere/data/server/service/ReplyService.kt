package com.lion.FinalProject_CarryOn_Anywhere.data.server.service

import com.lion.FinalProject_CarryOn_Anywhere.data.server.model.ReplyModel
import com.lion.FinalProject_CarryOn_Anywhere.data.server.repository.CarryTalkRepository
import com.lion.FinalProject_CarryOn_Anywhere.data.server.repository.ReplyRepository
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.ReplyState

class ReplyService {

    companion object {
        // 0221 khs 댓글 관련 추가 부분

        // 특정 게시글에 대한 댓글 불러오기
        suspend fun getAllReplysByTalkDocId(boardDocumentId: String): List<ReplyModel>? {
            return ReplyRepository.getAllReplysByTalkDocId(boardDocumentId)
        }


        // 댓글 추가
        // ReplyData에 저장 후 CarryTalkData의 특정 게시글(talkDocumentId)의 talkReplyList에 추가
        suspend fun addReply(talkDocumentId: String, replyModel: ReplyModel) {
            // 저장 메서드 호출
            ReplyRepository.addReply(talkDocumentId, replyModel)
        }


        // 댓글 수정
        suspend fun updateReplyData(replyDocumentId: String, replyModel: ReplyModel) {
            // 저장 메서드 호출
            ReplyRepository.updateReplyData(replyDocumentId, replyModel)
        }


        // 댓글 수정, 삭제 및 신고 -> 댓글 상태 변환에 사용
        suspend fun updateReplyState(replyDocumentId: String, newState: ReplyState) {
            ReplyRepository.updateReplyState(replyDocumentId, newState)
        }
    }
}