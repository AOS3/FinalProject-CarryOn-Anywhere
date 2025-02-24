package com.lion.FinalProject_CarryOn_Anywhere.data.server.model

import com.google.firebase.Timestamp
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.TripReviewState
import com.lion.FinalProject_CarryOn_Anywhere.data.server.vo.TripReviewVO

// 여행 후기 글
class TripReviewModel {
    // 여행 후기 문서 아이디
    var tripReviewDocumentId:String = ""
    // 작성자
    var userDocumentId: String = ""
    // 작성자 닉네임
    var userName:String = ""
    // 여행 후기 제목
    var tripReviewTitle:String = ""
    // 일정 문서 아이디
    var tripDocumentId : String = ""
    // 여행 후기 내용
    var tripReviewContent:String = ""
    // 여행 후기 사진
    var tripReviewImage= mutableListOf<String>()
    // 여행 후기 댓글 (Reply)
    var tripReviewReplyList= mutableListOf<String>()
    // 좋아요
    var tripReviewLikeCount:Int = 0
    // 여행 후기 상태 (기본 - 1, 삭제 - 2)
    var tripReviewState = TripReviewState.TRIP_REVIEW_STATE_NORMAL
    // 데이터가 생성된 시간
    var tripReviewTimestamp = 0L

    fun toTripReviewVO(): TripReviewVO {
        val tripReviewVO = TripReviewVO()

        tripReviewVO.userDocumentId = userDocumentId
        tripReviewVO.tripReviewTitle = tripReviewTitle
        tripReviewVO.tripDocumentId = tripDocumentId
        tripReviewVO.tripReviewContent = tripReviewContent
        tripReviewVO.tripReviewImage = tripReviewImage.toMutableList()
        tripReviewVO.tripReviewReplyList = tripReviewReplyList.toMutableList()
        tripReviewVO.tripReviewLikeCount = tripReviewLikeCount
        tripReviewVO.tripReviewTimestamp = tripReviewTimestamp
        tripReviewVO.tripReviewState = tripReviewState.number

        return tripReviewVO
    }
}