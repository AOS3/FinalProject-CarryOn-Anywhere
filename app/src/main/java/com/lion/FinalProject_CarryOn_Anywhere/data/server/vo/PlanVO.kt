package com.lion.FinalProject_CarryOn_Anywhere.data.server.vo

import com.google.firebase.Timestamp
import com.lion.FinalProject_CarryOn_Anywhere.data.server.model.PlanModel
import com.lion.FinalProject_CarryOn_Anywhere.data.server.model.TripModel
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.TripState

class PlanVO {

    // 여행 문서 아이디
    var tripDocumentId:String = ""
    // 장소 목록 (contentId)
    var placeList = mutableListOf<Map<String, Any?>>()
    // 여행 날짜
    var planDay:String = ""
    // 일정 데이터 생성 시간
    var planTimeStamp:Long = 0L

    fun toPlanModel(planDocumentId:String) : PlanModel {
        val planModel = PlanModel()

        planModel.planDocumentId = planDocumentId
        planModel.tripDocumentId = tripDocumentId
        planModel.placeList = placeList.toMutableList()
        planModel.planDay = planDay
        planModel.planTimeStamp = planTimeStamp

        return planModel
    }
}