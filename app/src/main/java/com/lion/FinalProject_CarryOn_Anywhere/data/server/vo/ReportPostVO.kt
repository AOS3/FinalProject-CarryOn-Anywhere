package com.lion.FinalProject_CarryOn_Anywhere.data.server.vo

import com.lion.FinalProject_CarryOn_Anywhere.data.server.model.ReportPostModel
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.ReportState
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.ReportType

class ReportPostVO {
    // 신고 요청 문서 아이디
    var reportPostDocumentId: String = ""
    // 글 작성자
    var userDocumentId: String = ""
    // 신고자
    var reportUserDocumentId: String = ""
    // 요청 상태 (숫자로 저장)
    var reportPostState: Int = ReportState.REPORT_STATE_BEFORE.number
    // 데이터가 생성된 시간
    var reportPostTimeStamp: Long = 0L
    // 여행 후기 OR 여행 이야기
    var reportType: Int = ReportType.REPORT_STATE_REVIEW.number

    fun toReportPostModel(reportPostDocumentId: String): ReportPostModel {
        val reportPostModel = ReportPostModel()
        reportPostModel.reportPostDocumentId = reportPostDocumentId
        reportPostModel.userDocumentId = userDocumentId
        reportPostModel.reportUserDocumentId = reportUserDocumentId
        reportPostModel.reportPostTimeStamp = reportPostTimeStamp

        reportPostModel.reportPostState = when (reportPostState) {
            ReportState.REPORT_STATE_BEFORE.number -> ReportState.REPORT_STATE_BEFORE
            ReportState.REPORT_STATE_AFTER.number -> ReportState.REPORT_STATE_AFTER
            else -> ReportState.REPORT_STATE_BEFORE // 기본값 처리
        }

        reportPostModel.reportType = when (reportType) {
            ReportType.REPORT_STATE_REVIEW.number -> ReportType.REPORT_STATE_REVIEW
            ReportType.REPORT_STATE_STORY.number -> ReportType.REPORT_STATE_STORY
            else -> ReportType.REPORT_STATE_REVIEW // 기본값 처리
        }

        return reportPostModel
    }
}
