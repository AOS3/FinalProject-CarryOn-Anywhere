package com.lion.FinalProject_CarryOn_Anywhere.data.server.vo

import com.lion.FinalProject_CarryOn_Anywhere.data.server.model.CarryTalkModel
import com.lion.FinalProject_CarryOn_Anywhere.data.server.model.ReplyModel
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.CarryTalkState
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.ReplyState
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.TalkTag

class ReplyVO {

    // 작성자 (사용자 문서 ID)
    var userId:String = ""
    // 게시글 문서 아이디
    var boardDocumentId:String = ""
    // 댓글 내용
    var replyContent:String = ""
    // 댓글 상태
    var replyState:Int = 1
    // 댓글 생성된 시간
    var replyTimeStamp:Long = 0L

    fun toReplyModel(replyDocumentId:String) : ReplyModel {
        val replyModel = ReplyModel()

        replyModel.replyDocumentId = replyDocumentId
        replyModel.boardDocumentId = boardDocumentId
        replyModel.replyContent = replyContent
        replyModel.replyTimeStamp = replyTimeStamp

        when(replyState){
            ReplyState.REPLY_STATE_NORMAL.number -> replyModel.replyState = ReplyState.REPLY_STATE_NORMAL
            ReplyState.REPLY_STATE_DELETE.number -> replyModel.replyState = ReplyState.REPLY_STATE_DELETE
            ReplyState.REPLY_STATE_COMPLAINT.number -> replyModel.replyState = ReplyState.REPLY_STATE_COMPLAINT
            ReplyState.REPLY_STATE_MODIFY.number -> replyModel.replyState = ReplyState.REPLY_STATE_MODIFY
        }

        return replyModel
    }
}