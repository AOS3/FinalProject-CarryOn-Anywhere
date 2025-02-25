package com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.social

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lion.FinalProject_CarryOn_Anywhere.data.server.model.TripReviewModel
import com.lion.FinalProject_CarryOn_Anywhere.data.server.service.TripReviewService
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.TripReviewState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// UI에 보여줄 데이터 구조
data class Review(
    val documentId: String = "",
    val imageUrls: List<String>,
    val title: String,
    val author: String,
    val nickName : String,
    val content: String,
    val postDate: Long,
    val likes: Int,
    val comments: Int,
    val tripDate: String,
    val shareTitle: String ,
    val sharePlace: List<String>,
    val sharePlan: List<Map<String, String>>
)

@HiltViewModel
class ReviewViewModel @Inject constructor(
    @ApplicationContext context: Context
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _reviews = MutableStateFlow<List<Review>>(emptyList())
    val reviews: StateFlow<List<Review>> get() = _reviews

    init {
        fetchTripReviews()

    }

    // Firstore에서 TRIP_REVIEW_STATE_NORMAL 인 데이터 가져오기
    fun fetchTripReviews() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val tripReviews = TripReviewService.fetchAllTripReviews()

                val reviewList = tripReviews
                    .filter { it.tripReviewState == TripReviewState.TRIP_REVIEW_STATE_NORMAL}
                    .map { tripReview ->
                        Review(
                            documentId = tripReview.tripReviewDocumentId,
                            imageUrls = tripReview.tripReviewImage,
                            title = tripReview.tripReviewTitle,
                            likes = tripReview.tripReviewLikeCount,
                            comments = tripReview.tripReviewReplyList.size,
                            postDate = tripReview.tripReviewTimestamp,
                            content = tripReview.tripReviewContent,
                            author = tripReview.userDocumentId,
                            nickName = tripReview.userName,
                            tripDate = tripReview.tripReviewShareDate,
                            shareTitle = tripReview.tripReviewShareTitle,
                            sharePlace = tripReview.tripReviewSharePlace,
                            sharePlan = tripReview.tripReviewSharePlan
                        )
                    }.sortedByDescending { it.postDate }

                _reviews.value = reviewList
            } catch (e: Exception) {
                Log.e("ReviewViewModel", "데이터 불러오기 실패: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    // TripReviewState를 TRIP_REVIEW_STATE_DELETE 로 변경 (삭제 - 안 보이게 처리)
    fun deleteTripReview(documentId: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                TripReviewService.deleteTripReview(documentId)
                fetchTripReviews()
                onSuccess()
            } catch (e: Exception) {
                onError("삭제 실패: ${e.message}")
            }
        }
    }

    // 여행 후기 수정 후 UI 업데이트
    fun editTripReview(documentId: String, newTitle: String, newContent: String, newImageUrls: List<String>) {
        viewModelScope.launch {
            try {
                TripReviewService.updateTripReview(documentId, newTitle, newContent, newImageUrls)
                fetchTripReviews()
            } catch (e: Exception) {
                Log.e("ReviewViewModel", "여행 후기 수정 실패: ${e.message}")
            }
        }
    }
}