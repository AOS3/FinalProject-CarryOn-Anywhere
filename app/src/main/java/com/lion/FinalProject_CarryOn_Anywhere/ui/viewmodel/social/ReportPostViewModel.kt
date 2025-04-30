package com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.social

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lion.FinalProject_CarryOn_Anywhere.data.server.model.ReportPostModel
import com.lion.FinalProject_CarryOn_Anywhere.data.server.service.ReportPostService
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.ReportState
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.ReportType
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReportPostViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val reportPostService: ReportPostService
) : ViewModel() {

    fun reportReview(
        reportPostDocumentId: String,
        reportedUserId: String,
        reporterUserId: String,
        reportStateNumber: Int, // 기본값으로 BEFORE 설정
        reportTypeNumber: Int, // 기본값으로 REVIEW 설정
        onReported: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val reportState = when (reportStateNumber) {
                    1 -> ReportState.REPORT_STATE_BEFORE
                    2 -> ReportState.REPORT_STATE_AFTER
                    else -> ReportState.REPORT_STATE_BEFORE // 예외 처리
                }

                val reportType = when (reportTypeNumber) {
                    1 -> ReportType.REPORT_STATE_REVIEW
                    2 -> ReportType.REPORT_STATE_STORY
                    else -> ReportType.REPORT_STATE_REVIEW // 예외 처리
                }

                val reportModel = ReportPostModel().apply {
                    this.reportPostDocumentId = reportPostDocumentId
                    this.userDocumentId = reportedUserId
                    this.reportUserDocumentId = reporterUserId
                    this.reportPostState = reportState
                    this.reportType = reportType
                    this.reportPostTimeStamp = System.currentTimeMillis()
                }

                reportPostService.addReportPostData(reportModel)
                onReported()
            } catch (e: Exception) {
                onError(e.message ?: "신고 중 오류가 발생했습니다.")
            }
        }
    }
}
