package com.lion.FinalProject_CarryOn_Anywhere.data.server.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.lion.FinalProject_CarryOn_Anywhere.data.server.vo.ReportPostVO
import javax.inject.Inject

class ReportPostRepository @Inject constructor() {
    // 신고 요청 정보를 추가하는 메서드
    fun addReportPostData(reportPostVO: ReportPostVO) {
        val firestore = FirebaseFirestore.getInstance()
        firestore.collection("ReportPostData")
            .document(reportPostVO.reportPostDocumentId) // 직접 ID 지정
            .set(reportPostVO)
    }
}