package com.lion.FinalProject_CarryOn_Anywhere.data.server.vo

import com.lion.FinalProject_CarryOn_Anywhere.data.server.model.CarryTalkModel
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.CarryTalkState
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.TalkTag

class CarryTalkVO {

    // 작성자
    var userDocumentId: String = ""
    // 여행 이야기 태그
    var talkTag:Int = 1
    // 여행 이야기 제목
    var talkTitle:String = ""
    // 여행 이야기 내용
    var talkContent:String = ""
    // 여행 후기 사진
    var talkImage= mutableListOf<String>()
    // 여행 이야기 댓글 (Reply)
    var talkReplyList= mutableListOf<String>()
    // 좋아요
    var talkLikeCount:Int = 0
    // 여행 이야기 상태 (기본 - 1, 삭제 - 2)
    var talkState:Int = 1
    // 데이터가 생성된 시간
    var talkTimeStamp: Long = 0L

    fun toCarryTalkModel(talkDocumentId:String) : CarryTalkModel {
        val carryTalkModel = CarryTalkModel()

        carryTalkModel.talkDocumentId = talkDocumentId
        carryTalkModel.userDocumentId = userDocumentId
        carryTalkModel.talkTitle = talkTitle
        carryTalkModel.talkContent = talkContent
        carryTalkModel.talkImage = talkImage.toMutableList()
        carryTalkModel.talkReplyList = talkReplyList.toMutableList()
        carryTalkModel.talkLikeCount = talkLikeCount
        carryTalkModel.talkTimeStamp = talkTimeStamp

        when(talkTag){
            TalkTag.TALK_TAG_ALL.number -> carryTalkModel.talkTag = TalkTag.TALK_TAG_ALL
            TalkTag.TALK_TAG_RESTAURANT.number -> carryTalkModel.talkTag = TalkTag.TALK_TAG_RESTAURANT
            TalkTag.TALK_TAG_ACCOMMODATION.number -> carryTalkModel.talkTag = TalkTag.TALK_TAG_ACCOMMODATION
            TalkTag.TALK_TAG_TRIP_PLAN.number -> carryTalkModel.talkTag = TalkTag.TALK_TAG_TRIP_PLAN
            TalkTag.TALK_TAG_MEET.number -> carryTalkModel.talkTag = TalkTag.TALK_TAG_MEET
        }

        when(talkState) {
            CarryTalkState.CARRYTALK_STATE_NORMAL.number -> carryTalkModel.talkState = CarryTalkState.CARRYTALK_STATE_NORMAL
            CarryTalkState.CARRYTALK_STATE_DELETE.number -> carryTalkModel.talkState = CarryTalkState.CARRYTALK_STATE_DELETE
        }

        return carryTalkModel
    }
}