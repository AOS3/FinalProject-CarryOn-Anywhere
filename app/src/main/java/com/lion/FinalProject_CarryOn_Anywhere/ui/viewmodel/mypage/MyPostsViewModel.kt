package com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.mypage

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.lion.FinalProject_CarryOn_Anywhere.data.server.model.CarryTalkModel
import com.lion.FinalProject_CarryOn_Anywhere.data.server.model.ReplyModel
import com.lion.FinalProject_CarryOn_Anywhere.data.server.model.TripReviewModel
import com.lion.FinalProject_CarryOn_Anywhere.data.server.service.CarryTalkService
import com.lion.FinalProject_CarryOn_Anywhere.data.server.service.ReplyService
import com.lion.FinalProject_CarryOn_Anywhere.data.server.service.TripReviewService
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.ReplyState
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.TripReviewState
import com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.social.Review
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

// UI에 보여줄 데이터 구조
data class MyReview(
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
class MyPostsViewModel @Inject constructor(
    @ApplicationContext context: Context
) : ViewModel() {


    // 특정 게시글에 해당하는 댓글 목록 LiveData
    private val _myTripReviews = MutableStateFlow<List<Review>>(emptyList())
    val myTripReviews: StateFlow<List<Review>> get() = _myTripReviews

    // 여행 후기 가져오기
    fun getMyTripReviews(userDocumentId:String){
        viewModelScope.launch {
            val myTripReviews = TripReviewService.getMyTripReviews(userDocumentId)

            val reviewList = myTripReviews
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

            _myTripReviews.value = reviewList

        }
    }



    // 나의 여행 이야기(CarryTalk) 가져오기
    // 특정 게시글에 해당하는 댓글 목록 LiveData
    private val _myCarryTalk = MutableStateFlow<List<CarryTalkModel>>(emptyList())
    val myCarryTalk: StateFlow<List<CarryTalkModel>> get() = _myCarryTalk

    // 여행 이야기 가져오기
    fun getMyCarryTalk(userDocumentId:String){
        viewModelScope.launch {
            val myCarryTalk = CarryTalkService.getMyCarryTalk(userDocumentId)
            _myCarryTalk.value = myCarryTalk
        }
    }



    //0225 댓글 가져오기 -> 구현 완료
    // 댓글 삭제 및 신고 성공 여부
    private val _isRemove1 = MutableLiveData<Boolean?>()
    private val _isRemove2 = MutableLiveData<Boolean?>()

    // 특정 게시글에 해당하는 댓글 목록 LiveData
    private val _myAllReplys = MutableStateFlow<List<ReplyModel>>(emptyList())
    val myAllReplys: StateFlow<List<ReplyModel>> get() = _myAllReplys

    // 사용자 ID로 작성한 댓글 모두 불러오기
    fun getAllReplysByUserId(userId: String){
        viewModelScope.launch {
            val myAllReplys = ReplyService.getAllReplysByUserId(userId)
            _myAllReplys.value = myAllReplys
        }
    }

    // 댓글 삭제
    // replyId 만으로 댓글 데이터 삭제하기
    // 댓글 삭제 -> 업데이트(불러오기)
    fun deleteReplyByReplyDocId(replyDocumentId: String, userId: String) {
        viewModelScope.launch {
            // ReplyData에서 댓글 삭제로 상태 변경
            val isRemove1 = ReplyService.updateReplyState(replyDocumentId,ReplyState.REPLY_STATE_DELETE)
            _isRemove1.postValue(isRemove1)

            // talkDocumentId의 댓글 리스트에서 replyDocumentId 삭제
            val isRemove2 = ReplyService.deleteReplyByReplyDocId(replyDocumentId)
            _isRemove2.postValue(isRemove2)


            if (isRemove1 && isRemove2) {
                // 나의 댓글 불러오기
                getAllReplysByUserId(userId)
            }
        }
    }



}