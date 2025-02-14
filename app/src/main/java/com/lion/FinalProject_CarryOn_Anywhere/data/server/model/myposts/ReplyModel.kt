package com.lion.FinalProject_CarryOn_Anywhere.data.server.model.myposts

import androidx.test.services.events.TimeStamp

data class ReplyModel(

    val userDocumentId : String, //작성자
    //val boardDocumentId : String,	//여행 후기 or 이야기 문서 아이디
    val replyContent : String,	//댓글 내용
    // val replyState:	Int, // 댓글 상태 -> 기본 - 1 ,삭제 - 2, 신고 - 3, 수정 - 4
    val replyTimeStamp: String,  // : TimeStamp 데이터가 생성된 날짜, 시간	저장 nano / 가져오기 Long
)
