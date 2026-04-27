package com.lion.FinalProject_CarryOn_Anywhere.data.server.vo

import com.google.firebase.Timestamp
import com.lion.FinalProject_CarryOn_Anywhere.data.server.model.TripReviewModel
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.TripReviewState

class TripReviewVO {
    // 작성자
    var userDocumentId: String = ""
    // 여행 후기 제목
    var tripReviewTitle:String = ""
    // 일정 문서 아이디
    var tripDocumentId : String = ""
    // 여행 후기 내용
    var tripReviewContent:String = ""
    // 여행 후기 사진
    var tripReviewImage = mutableListOf<String>()
    // 여행 후기 댓글 (Reply)
    var tripReviewReplyList = mutableListOf<String>()
    // 좋아요
    var tripReviewLikeCount:Int = 0
    // 여행 후기 상태 (기본 - 1, 삭제 - 2)
    var tripReviewState:Int = 1
    // 데이터가 생성된 시간
    var tripReviewTimestamp:Long = 0L

    fun toTripReviewModel(tripReviewDocumentId:String) : TripReviewModel {
        val tripReviewModel = TripReviewModel()

        tripReviewModel.tripReviewDocumentId = tripReviewDocumentId
        tripReviewModel.userDocumentId = userDocumentId
        tripReviewModel.tripReviewTitle = tripReviewTitle
        tripReviewModel.tripDocumentId = tripDocumentId
        tripReviewModel.tripReviewContent = tripReviewContent
        tripReviewModel.tripReviewImage = tripReviewImage.toMutableList()
        tripReviewModel.tripReviewReplyList = tripReviewReplyList.toMutableList()
        tripReviewModel.tripReviewLikeCount = tripReviewLikeCount
        tripReviewModel.tripReviewTimestamp = tripReviewTimestamp

        when(tripReviewState){
            TripReviewState.TRIP_REVIEW_STATE_NORMAL.number -> tripReviewModel.tripReviewState = TripReviewState.TRIP_REVIEW_STATE_NORMAL
            TripReviewState.TRIP_REVIEW_STATE_DELETE.number -> tripReviewModel.tripReviewState = TripReviewState.TRIP_REVIEW_STATE_DELETE
        }

        return tripReviewModel
    }
}