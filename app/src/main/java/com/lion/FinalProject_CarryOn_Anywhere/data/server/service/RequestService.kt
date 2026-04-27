package com.lion.FinalProject_CarryOn_Anywhere.data.server.service

import com.lion.FinalProject_CarryOn_Anywhere.data.server.model.RequestModel
import com.lion.FinalProject_CarryOn_Anywhere.data.server.repository.RequestRepository

class RequestService(val requestRepository: RequestRepository) {

    // 요청 정보를 추가하는 메서드
    fun addRequestData(requestModel: RequestModel){
        // 데이터를 VO에 담아준다.
        val requestVO = requestModel.toRequestVO()
        // 저장하는 메서드를 호출한다.
        requestRepository.addRequestData(requestVO)
    }
}