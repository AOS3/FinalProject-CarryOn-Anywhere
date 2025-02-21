package com.lion.FinalProject_CarryOn_Anywhere.data.server.vo

import com.lion.FinalProject_CarryOn_Anywhere.data.server.model.ReplyModel
import com.lion.FinalProject_CarryOn_Anywhere.data.server.model.RequestModel
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.ReplyState
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.RequestState

class RequestVO {
    // 작성자
    var userDocumentId:String = ""
    // 장소 이름
    var requestPlaceName:String = ""
    // 장소 도로명 주소
    var requestPlaceAddress:String = ""
    // 요청 상태
    var requestState:Int = 1
    // 데이터가 생성된 시간
    var requestTimeStamp:Long = 0L

    fun toRequestModel(requestDocumentId:String) : RequestModel {
        val requestModel = RequestModel()
        requestModel.requestDocumentId = requestDocumentId
        requestModel.userDocumentId = userDocumentId
        requestModel.requestPlaceName = requestPlaceName
        requestModel.requestPlaceAddress = requestPlaceAddress
        requestModel.requestTimeStamp = requestTimeStamp

        when(requestState){
            RequestState.REQUEST_STATE_ENABLE.number -> requestModel.requestState = RequestState.REQUEST_STATE_ENABLE
            RequestState.REQUEST_STATE_DISABLE.number -> requestModel.requestState = RequestState.REQUEST_STATE_DISABLE
        }

        return requestModel
    }
}