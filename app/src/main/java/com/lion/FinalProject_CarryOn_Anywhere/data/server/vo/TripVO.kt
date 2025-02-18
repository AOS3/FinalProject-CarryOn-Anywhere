package com.lion.FinalProject_CarryOn_Anywhere.data.server.vo

import com.google.firebase.Timestamp
import com.lion.FinalProject_CarryOn_Anywhere.data.server.model.TripModel
import com.lion.FinalProject_CarryOn_Anywhere.data.server.model.UserModel
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.TripState
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.UserState

class TripVO {

    // 사용자 문서 ID
    var userDocumentId: String = ""
    // 공유받은 사용자 문서 아이디 목록 (Trip Edit 가능, 삭제 불가)
    var shareUserDocumentId= mutableListOf<String>()
    // 일정 목록
    var planList= mutableListOf<String>()
    // 여행 제목
    var tripTitle: String = ""
    // 여행 출발 날짜
    var tripStartDate: Long = 0L
    // 여행 도착 날짜
    var tripEndDate: Long = 0L
    // 여행 지역 목록
    var tripCityList = mutableListOf<Map<String, Any?>>()
    // 여행 공유 코드
    var tripShareCode: String = ""
    // 여행 생성 시간 (데이터가 들어온 시간)
    var tripTimeStamp:Long = 0L // 데이터 입력 시간 (Firebase Timestamp)
    // 여행 문서 상태 (1: 정상, 2: 삭제)
    var tripState: Int = 1

    fun toTripModel(tripDocumentId:String) : TripModel {
        val tripModel = TripModel()

        tripModel.tripDocumentId = tripDocumentId
        tripModel.userDocumentId = userDocumentId
        tripModel.shareUserDocumentId = shareUserDocumentId.toMutableList()
        tripModel.planList = planList.toMutableList()
        tripModel.tripTitle = tripTitle
        tripModel.tripStartDate = tripStartDate
        tripModel.tripEndDate = tripEndDate
        tripModel.tripCityList = tripCityList.toMutableList()
        tripModel.tripShareCode = tripShareCode
        tripModel.tripTimeStamp = tripTimeStamp

        when(tripState){
            TripState.TRIP_STATE_NORMAL.number -> tripModel.tripState = TripState.TRIP_STATE_NORMAL
            TripState.TRIP_STATE_DELETE.number -> tripModel.tripState = TripState.TRIP_STATE_DELETE
        }

        return tripModel
    }
}