package com.lion.FinalProject_CarryOn_Anywhere.data.server.service

import com.lion.FinalProject_CarryOn_Anywhere.data.server.model.TripModel
import com.lion.FinalProject_CarryOn_Anywhere.data.server.repository.TripRepository
import com.lion.FinalProject_CarryOn_Anywhere.data.server.vo.TripVO

class TripService(val tripRepository: TripRepository) {
    // 여행 정보를 추가하고 `documentId`를 반환하는 메서드
    suspend fun addTripData(tripModel: TripModel): String? {
        // VO 변환
        val tripVO = tripModel.toTripVO()
        // 저장 후 `documentId` 반환
        return tripRepository.addTripData(tripVO)
    }

    // 여행 날짜 데이터를 수정한다.
    suspend fun updateTripDate(tripModel: TripModel){
        val tripVO = tripModel.toTripVO()
        tripRepository.updateTripDate(tripVO, tripModel.tripDocumentId)
    }

    // 사용자 데이터를 수정한다.
    suspend fun updateTripShare(tripModel: TripModel){
        val tripVO = tripModel.toTripVO()
        tripRepository.updateTripShare(tripVO, tripModel.tripDocumentId)
    }

    // 사용자 데이터를 수정한다.
    suspend fun updateTripTitle(tripModel: TripModel){
        val tripVO = tripModel.toTripVO()
        tripRepository.updateTripTitle(tripVO, tripModel.tripDocumentId)
    }

    suspend fun gettingTripList(userDocumentId: String) : MutableList<TripModel>{
        // 글정보를 가져온다.
        val tripList = mutableListOf<TripModel>()
        val resultList = tripRepository.gettingTripList(userDocumentId)

        resultList.forEach {
            val tripVO = it["tripVO"] as TripVO
            val documentId = it["documentId"] as String
            val tripModel = tripVO.toTripModel(documentId)
            tripList.add(tripModel)
        }

        return tripList
    }

    // 서버에서 글을 삭제한다.
    suspend fun deleteTripData(tripDocumentId:String){
        tripRepository.deleteTripData(tripDocumentId)
    }

    // 사용자 데이터를 수정한다.
    suspend fun updateTripPlanList(tripModel: TripModel, tripDocumentId: String){
        val tripVO = tripModel.toTripVO()
        tripRepository.updateTripPlanList(tripVO, tripDocumentId)
    }

    // 여행 문서 id를 통해 여행 데이터를 가져온다.
    suspend fun selectTripDataOneById(documentId:String) : TripModel{
        // 여행 데이터를 가져온다.
        val tripVO = tripRepository.selectTripDataOneById(documentId)
        // TripModel객체를 생성한다.
        val tripModel = tripVO.toTripModel(documentId)

        return tripModel
    }

    // 여행 문서 id를 통해 여행 데이터를 가져온다.
    suspend fun selectTripDataOneBySharedCode(tripSharedCode: String) : TripModel{
        val document = tripRepository.selectTripDataSnapshotBySharedCode(tripSharedCode)
        val tripVO = document.toObject(TripVO::class.java)!!
        val tripModel = tripVO.toTripModel(document.id)

        return tripModel
    }

    // 여행 날짜 데이터를 수정한다.
    suspend fun updateTripShareUser(tripModel: TripModel){
        val tripVO = tripModel.toTripVO()
        tripRepository.updateTripShareUser(tripVO, tripModel.tripDocumentId)
    }
}