package com.lion.FinalProject_CarryOn_Anywhere.data.server.model

import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.ReportState
import com.lion.FinalProject_CarryOn_Anywhere.data.server.vo.ReportReplyVO


class ReportReplyModel {
    // 신고 요청 문서 아이디
    var reportReplyDocumentId:String = ""
    // 글 작성자
    var userDocumentId:String = ""
    // 신고자
    var reportUserDocumentId:String = ""
    // 요청 상태
    var reportReplyState = ReportState.REPORT_STATE_BEFORE
    // 데이터가 생성된 시간
    var reportReplyTimeStamp = 0L

    fun toReportReplyVO() : ReportReplyVO {
        val reportReplyVO = ReportReplyVO()

        reportReplyVO.reportReplyDocumentId = reportReplyDocumentId
        reportReplyVO.userDocumentId = userDocumentId
        reportReplyVO.reportUserDocumentId = reportUserDocumentId
        reportReplyVO.reportReplyState = reportReplyState.number
        reportReplyVO.reportReplyTimeStamp = reportReplyTimeStamp

        return reportReplyVO
    }
}