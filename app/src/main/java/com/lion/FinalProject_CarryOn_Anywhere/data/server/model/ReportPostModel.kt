package com.lion.FinalProject_CarryOn_Anywhere.data.server.model

import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.ReportState
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.ReportType
import com.lion.FinalProject_CarryOn_Anywhere.data.server.vo.ReportPostVO


class ReportPostModel {
    // 신고 요청 문서 아이디
    var reportPostDocumentId:String = ""
    // 글 작성자
    var userDocumentId:String = ""
    // 신고자
    var reportUserDocumentId:String = ""
    // 요청 상태
    var reportPostState = ReportState.REPORT_STATE_BEFORE
    // 데이터가 생성된 시간
    var reportPostTimeStamp = 0L
    // 여행 후기 OR 여행 이야기
    var reportType = ReportType.REPORT_STATE_REVIEW

    fun toReportPostVO() : ReportPostVO {
        val reportPostVO = ReportPostVO()

        reportPostVO.reportPostDocumentId = reportPostDocumentId
        reportPostVO.userDocumentId = userDocumentId
        reportPostVO.reportUserDocumentId = reportUserDocumentId
        reportPostVO.reportPostState = reportPostState.number
        reportPostVO.reportType = reportType.number
        reportPostVO.reportPostTimeStamp = reportPostTimeStamp

        return reportPostVO
    }
}