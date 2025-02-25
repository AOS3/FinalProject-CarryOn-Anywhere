package com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.social

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.lion.FinalProject_CarryOn_Anywhere.data.server.model.TripReviewModel
import com.lion.FinalProject_CarryOn_Anywhere.data.server.service.CarryTalkService
import com.lion.FinalProject_CarryOn_Anywhere.data.server.service.TripReviewService
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.CarryTalkState
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.TalkTag
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.TripReviewState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

// UI에 보여줄 게시글 데이터 구조
data class Post(
    val documentId: String = "",
    val tag: String,
    val title: String,
    val content: String,
    val author: String,
    val nickName : String,
    val postDate: Long,
    val likes: Int,
    val comments: Int,
    val imageUrls: List<String> = emptyList()
)

@HiltViewModel
class StoryViewModel @Inject constructor(
    @ApplicationContext context: Context
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts: StateFlow<List<Post>> get() = _posts

    init {
        fetchCarryTalkPosts()
    }

    // Firstore에서 CARRYTALK_STATE_NORMAL 인 데이터 가져오기
    fun fetchCarryTalkPosts() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val carryTalks = CarryTalkService.fetchAllCarryTalks()

                val postList = carryTalks
                    .filter { it.talkState == CarryTalkState.CARRYTALK_STATE_NORMAL }
                    .map { talk ->
                    Post(
                        documentId = talk.talkDocumentId,
                        tag = talk.talkTag.str,
                        title = talk.talkTitle,
                        content = talk.talkContent,
                        author = talk.userDocumentId,
                        nickName = talk.userName,
                        postDate = talk.talkTimeStamp,
                        likes = talk.talkLikeCount,
                        comments = talk.talkReplyList.size,
                        imageUrls = talk.talkImage
                    )
                }.sortedByDescending { it.postDate }

                _posts.value = postList
                _isLoading.value = false

            } catch (e: Exception) {
                Log.e("StoryViewModel", "데이터 불러오기 실패: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    // CarryTalkState를 CARRYTALK_STATE_DELETE 로 변경 (삭제 - 안 보이게 처리)
    fun deleteCarryTalk(documentId: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                CarryTalkService.deleteCarryTalkReview(documentId)
                fetchCarryTalkPosts()
                onSuccess()
            } catch (e: Exception) {
                onError("삭제 실패: ${e.message}")
            }
        }
    }

    // 여행 후기 수정 후 UI 업데이트
    fun editCarryTalk(documentId: String, newTag: String, newTitle: String, newContent: String, newImageUrls: List<String>) {
        viewModelScope.launch {
            try {
                CarryTalkService.updateCarryTalk(documentId, newTag, newTitle, newContent, newImageUrls)
                fetchCarryTalkPosts()
            } catch (e: Exception) {
                Log.e("StoryViewModel", "여행 후기 수정 실패: ${e.message}")
            }
        }
    }




}
