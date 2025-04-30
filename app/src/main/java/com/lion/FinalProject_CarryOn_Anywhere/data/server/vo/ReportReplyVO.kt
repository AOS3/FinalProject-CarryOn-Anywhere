package com.lion.FinalProject_CarryOn_Anywhere.data.server.vo

import com.lion.FinalProject_CarryOn_Anywhere.data.server.model.ReportReplyModel
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.ReportState

class ReportReplyVO {

    // 신고 요청 문서 아이디
    var reportReplyDocumentId: String = ""
    // 글 작성자
    var userDocumentId: String = ""
    // 신고자
    var reportUserDocumentId: String = ""
    // 요청 상태 (숫자로 저장)
    var reportReplyState: Int = ReportState.REPORT_STATE_BEFORE.number
    // 데이터가 생성된 시간
    var reportReplyTimeStamp: Long = 0L

    fun toReportReplyModel(reportReplyDocumentId: String): ReportReplyModel {
        val reportReplyModel = ReportReplyModel()
        reportReplyModel.reportReplyDocumentId = reportReplyDocumentId
        reportReplyModel.userDocumentId = userDocumentId
        reportReplyModel.reportUserDocumentId = reportUserDocumentId
        reportReplyModel.reportReplyTimeStamp = reportReplyTimeStamp

        reportReplyModel.reportReplyState = when (reportReplyState) {
            ReportState.REPORT_STATE_BEFORE.number -> ReportState.REPORT_STATE_BEFORE
            ReportState.REPORT_STATE_AFTER.number -> ReportState.REPORT_STATE_AFTER
            else -> ReportState.REPORT_STATE_BEFORE // 기본값 처리
        }

        return reportReplyModel
    }
}