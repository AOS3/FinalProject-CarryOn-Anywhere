package com.lion.FinalProject_CarryOn_Anywhere.data.server.model

import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.ReplyState
import com.lion.FinalProject_CarryOn_Anywhere.data.server.vo.CarryTalkVO
import com.lion.FinalProject_CarryOn_Anywhere.data.server.vo.ReplyVO

class ReplyModel {
    // 댓글 문서 아이디
    var replyDocumentId:String = ""
    // 작성자 (사용자 문서 ID)
    var userDocumentId:String = ""
    // 게시글 문서 아이디
    var boardDocumentId:String = ""
    // 댓글 내용
    var replyContent:String = ""
    // 댓글 상태
    var replyState = ReplyState.REPLY_STATE_NORMAL
    // 댓글 생성된 시간
    var replyTimeStamp = 0L

    fun toReplyVO(): ReplyVO {
        val replyVO = ReplyVO()

        replyVO.boardDocumentId = boardDocumentId
        replyVO.replyContent = replyContent
        replyVO.replyTimeStamp = replyTimeStamp
        replyVO.replyState = replyState.number

        return replyVO
    }
}