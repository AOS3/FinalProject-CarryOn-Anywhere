package com.lion.FinalProject_CarryOn_Anywhere.data.server.model

import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.CarryTalkState
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.TalkTag
import com.lion.FinalProject_CarryOn_Anywhere.data.server.vo.CarryTalkVO

class CarryTalkModel {
    // 여행 이야기 문서 아이디
    var talkDocumentId:String = ""
    // 작성자
    var userDocumentId: String = ""
    // 작성자 닉네임
    var userName:String = ""
    // 여행 이야기 태그
    var talkTag = TalkTag.TALK_TAG_RESTAURANT
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
    var talkState = CarryTalkState.CARRYTALK_STATE_NORMAL
    // 데이터가 생성된 시간
    var talkTimeStamp = 0L

    fun toCarryTalkVO(): CarryTalkVO {
        val carryTalkVO = CarryTalkVO()

        carryTalkVO.userDocumentId = userDocumentId
        carryTalkVO.talkTitle = talkTitle
        carryTalkVO.talkContent = talkContent
        carryTalkVO.talkImage = talkImage.toMutableList()
        carryTalkVO.talkReplyList = talkReplyList.toMutableList()
        carryTalkVO.talkLikeCount = talkLikeCount
        carryTalkVO.talkTimeStamp = talkTimeStamp

        return carryTalkVO
    }
}