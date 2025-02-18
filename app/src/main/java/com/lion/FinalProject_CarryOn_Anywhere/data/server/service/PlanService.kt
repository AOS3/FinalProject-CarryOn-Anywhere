package com.lion.FinalProject_CarryOn_Anywhere.data.server.service

import com.lion.FinalProject_CarryOn_Anywhere.data.server.model.PlanModel
import com.lion.FinalProject_CarryOn_Anywhere.data.server.repository.PlanRepository
import com.lion.FinalProject_CarryOn_Anywhere.data.server.vo.PlanVO


class PlanService(val planRepository: PlanRepository) {
    // 사용자 정보를 추가하는 메서드
    suspend fun addPlanData(planModel: PlanModel){
        // 데이터를 VO에 담아준다.
        val planVO = planModel.toPlanVO()
        // 저장하는 메서드를 호출한다.
        planRepository.addPlanData(planVO)
    }

    // Firebase에서 tripDocumentId와 day가 같은 Plan 데이터를 가져옴
    suspend fun getPlanByDocumentIdAndDay(tripDocumentId: String, day: String): PlanVO? {
        return planRepository.getPlanByDocumentIdAndDay(tripDocumentId, day)
    }

    suspend fun updatePlanData(planModel: PlanModel) {
        planRepository.updatePlanData(planModel.toPlanVO())
    }
}