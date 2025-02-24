package com.lion.FinalProject_CarryOn_Anywhere.data.server.model

import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.RequestState
import com.lion.FinalProject_CarryOn_Anywhere.data.server.vo.RequestVO

class RequestModel {
    // 장소 요청 문서 아이디
    var requestDocumentId:String = ""
    // 작성자
    var userDocumentId:String = ""
    // 장소 이름
    var requestPlaceName:String = ""
    // 장소 도로명 주소
    var requestPlaceAddress:String = ""
    // 요청 상태
    var requestState = RequestState.REQUEST_STATE_ENABLE
    // 데이터가 생성된 시간
    var requestTimeStamp = 0L

    fun toRequestVO() : RequestVO {
        val requestVO = RequestVO()

        requestVO.userDocumentId = userDocumentId
        requestVO.requestPlaceName = requestPlaceName
        requestVO.requestPlaceAddress = requestPlaceAddress
        requestVO.requestTimeStamp = requestTimeStamp
        requestVO.requestState = requestState.number

        return requestVO
    }
}