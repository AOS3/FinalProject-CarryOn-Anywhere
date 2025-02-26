package com.lion.FinalProject_CarryOn_Anywhere.data.server.service

import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.lion.FinalProject_CarryOn_Anywhere.data.server.model.TripReviewModel
import com.lion.FinalProject_CarryOn_Anywhere.data.server.repository.TripReviewRepository
import kotlinx.coroutines.tasks.await

class TripReviewService {
    companion object {

        // 여행 후기 데이터 추가
        suspend fun addTripReview(tripReviewModel: TripReviewModel): DocumentReference {
            return TripReviewRepository.addTripReviewData(tripReviewModel)
        }

        // 모든 여행 후기 가져오기
        suspend fun fetchAllTripReviews(): List<TripReviewModel> {
            return TripReviewRepository.getAllTripReviews()
        }

        // 여행 후기 삭제 (tripReviewState 변경)
        suspend fun deleteTripReview(documentId: String) {
            TripReviewRepository.updateTripReviewState(documentId, "TRIP_REVIEW_STATE_DELETE") // ✅ 올바른 호출 방식
        }

        // 여행 후기 데이터 수정
        suspend fun updateTripReview(documentId: String, newTitle: String, newContent: String, newImageUrls: List<String>) {
            TripReviewRepository.updateTripReview(documentId, newTitle, newContent, newImageUrls)
        }


        // 나의 후기 가져오기
        suspend fun getMyTripReviews(userDocumentId:String): List<TripReviewModel> {
            return TripReviewRepository.getMyTripReviews(userDocumentId)
        }

        // 좋아요 토글 (추가 및 취소)
        suspend fun toggleLike(documentId: String, userId: String): Boolean {
            return TripReviewRepository.toggleLike(documentId, userId)
        }

        // 특정 후기의 좋아요 상태 가져오기
        suspend fun getLikeStatus(documentId: String, userId: String): Boolean {
            return TripReviewRepository.getLikeStatus(documentId, userId)
        }
    }
}