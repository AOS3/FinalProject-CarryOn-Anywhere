package com.lion.FinalProject_CarryOn_Anywhere.data.server.service

import com.lion.FinalProject_CarryOn_Anywhere.data.server.model.ReportReplyModel
import com.lion.FinalProject_CarryOn_Anywhere.data.server.repository.ReportReplyRepository
import javax.inject.Inject

class ReportReplyService @Inject constructor(
    val reportReplyRepository: ReportReplyRepository
){
    // 신고 요청 정보를 추가하는 메서드
    fun addReportReplyData(reportReplyModel: ReportReplyModel) {
        val reportReplyVO = reportReplyModel.toReportReplyVO()
        reportReplyRepository.addReportReplyData(reportReplyVO)
    }
}