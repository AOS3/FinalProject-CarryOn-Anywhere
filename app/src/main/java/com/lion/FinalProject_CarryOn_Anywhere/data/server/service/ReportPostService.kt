package com.lion.FinalProject_CarryOn_Anywhere.data.server.service

import com.lion.FinalProject_CarryOn_Anywhere.data.server.model.ReportPostModel
import com.lion.FinalProject_CarryOn_Anywhere.data.server.repository.ReportPostRepository
import javax.inject.Inject

class ReportPostService @Inject constructor(
    val reportPostRepository: ReportPostRepository
) {
    // 신고 요청 정보를 추가하는 메서드
    fun addReportPostData(reportPostModel: ReportPostModel) {
        val reportPostVO = reportPostModel.toReportPostVO()
        reportPostRepository.addReportPostData(reportPostVO)
    }
}
