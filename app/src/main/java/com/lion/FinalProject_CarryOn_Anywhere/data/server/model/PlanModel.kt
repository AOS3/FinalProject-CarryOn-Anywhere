package com.lion.FinalProject_CarryOn_Anywhere.data.server.model

import com.google.firebase.Timestamp
import com.lion.FinalProject_CarryOn_Anywhere.data.server.vo.PlanVO
import com.lion.FinalProject_CarryOn_Anywhere.data.server.vo.TripVO

class PlanModel {
    // 일정 문서 아이디
    var planDocumentId:String = ""
    // 여행 문서 아이디
    var tripDocumentId:String = ""
    // 장소 목록 (contentId)
    var placeList = mutableListOf<String>()
    // 일정 데이터 생성 시간
    var planTimeStamp = 0L

    fun toPlanVO(): PlanVO {
        val planVO = PlanVO()

        planVO.tripDocumentId = tripDocumentId
        planVO.placeList = placeList.toMutableList()
        planVO.planTimeStamp = planTimeStamp

        return planVO
    }
}