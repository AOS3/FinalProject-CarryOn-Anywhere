package com.lion.FinalProject_CarryOn_Anywhere.data.server.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.lion.FinalProject_CarryOn_Anywhere.data.server.vo.ReportReplyVO
import javax.inject.Inject

class ReportReplyRepository @Inject constructor() {
    // 신고 요청 정보를 추가하는 메서드
    fun addReportReplyData(reportReplyVO: ReportReplyVO) {
        val firestore = FirebaseFirestore.getInstance()
        firestore.collection("ReportReplyData")
            .document(reportReplyVO.reportReplyDocumentId) // 직접 ID 지정
            .set(reportReplyVO)
    }
}