package com.lion.FinalProject_CarryOn_Anywhere.data.server.service

import android.util.Log
import com.lion.FinalProject_CarryOn_Anywhere.data.server.model.PlanModel
import com.lion.FinalProject_CarryOn_Anywhere.data.server.model.TripModel
import com.lion.FinalProject_CarryOn_Anywhere.data.server.repository.PlanRepository
import com.lion.FinalProject_CarryOn_Anywhere.data.server.vo.PlanVO


class PlanService(val planRepository: PlanRepository) {

    // 일정 정보를 추가하고 `documentId`를 반환하는 메서드
    suspend fun addPlanData(planModel: PlanModel): String? {
        // VO 변환
        val planVO = planModel.toPlanVO()
        // 저장 후 `documentId` 반환
        return planRepository.addPlanData(planVO)
    }

    // Firebase에서 tripDocumentId와 day가 같은 Plan 데이터를 가져옴
    suspend fun getPlanByDocumentIdAndDay(tripDocumentId: String, day: String): PlanVO? {
        return planRepository.getPlanByDocumentIdAndDay(tripDocumentId, day)
    }

    suspend fun updatePlanData(planModel: PlanModel) {
        planRepository.updatePlanData(planModel.toPlanVO())
    }

    // 장소 리스트 업데이트
    suspend fun updatePlanByDocumentIdAndDay(tripDocumentId: String, day: String, newPlaceList: List<Map<String, Any?>>) {
        planRepository.updatePlanByDocumentIdAndDay(tripDocumentId, day, newPlaceList)
    }

    // 서버에서 글을 삭제한다.
    suspend fun deleteAllPlansByTripId(tripDocumentId:String){
        planRepository.deleteAllPlansByTripId(tripDocumentId)
    }

    suspend fun getPlansByTripDocumentId(planDocumentId: String): PlanVO? {
        return planRepository.getPlansByPlanDocumentId(planDocumentId)
    }

}