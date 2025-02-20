package com.lion.FinalProject_CarryOn_Anywhere.data.server.model

import com.google.firebase.Timestamp
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.TripState
import com.lion.FinalProject_CarryOn_Anywhere.data.server.vo.TripVO
import com.lion.FinalProject_CarryOn_Anywhere.data.server.vo.UserVO
import java.time.LocalDate

class TripModel {
    // 여행 문서 ID
    var tripDocumentId: String = ""
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
    var tripTimeStamp = 0L // 데이터 입력 시간 (Firebase Timestamp)
    // 여행 문서 상태 (1: 정상, 2: 삭제)
    var tripState = TripState.TRIP_STATE_NORMAL

    fun toTripVO(): TripVO {
        val tripVO = TripVO()
        tripVO.userDocumentId = userDocumentId
        tripVO.shareUserDocumentId = shareUserDocumentId.toMutableList()
        tripVO.planList = planList.toMutableList()
        tripVO.tripTitle = tripTitle
        tripVO.tripStartDate = tripStartDate
        tripVO.tripEndDate = tripEndDate
        tripVO.tripCityList = tripCityList.toMutableList() // ✅ Map 그대로 저장
        tripVO.tripShareCode = tripShareCode
        tripVO.tripTimeStamp = tripTimeStamp
        tripVO.tripState = tripState.number

        return tripVO
    }
}