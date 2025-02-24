package com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.social

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.storage.FirebaseStorage
import com.lion.FinalProject_CarryOn_Anywhere.CarryOnApplication
import com.lion.FinalProject_CarryOn_Anywhere.data.server.model.ReplyModel
import com.lion.FinalProject_CarryOn_Anywhere.data.server.repository.ReplyRepository
import com.lion.FinalProject_CarryOn_Anywhere.data.server.service.CarryTalkService
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.ReplyState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// 게시글 데이터 클래스
//data class Comment(
//    val author: String,
//    var content: String,
//    val commentDate: String
//)

@HiltViewModel
class CommnetViewModel @Inject constructor(
    @ApplicationContext context: Context
) : ViewModel() {

    val carryOnApplication = context as CarryOnApplication

//    private val _comments = MutableStateFlow(
//        listOf(
//            Comment("김철수", "경복궁 근처 맛집 추천", "2025-02-12"),
//            Comment("이영희", "뷰가 정말 예뻐요!", "2025-02-10"),
//            Comment("박민수", "이런 일정 어때요?", "2025-01-25"),
//            Comment("김철수", "경복궁 근처 맛집 추천", "2025-02-12"),
//            Comment("이영희", "뷰가 정말 예뻐요!", "2025-02-10"),
//            Comment("박민수", "이런 일정 어때요?", "2025-01-25"),
//            Comment("김철수", "경복궁 근처 맛집 추천", "2025-02-12"),
//            Comment("이영희", "뷰가 정말 예뻐요!", "2025-02-10"),
//            Comment("박민수", "이런 일정 어때요?", "2025-01-25")
//        )
//    )
//
//    val comments: StateFlow<List<Comment>> = _comments
//
//    fun updateCommentContent(index: Int, newContent: String) {
//        _comments.update { currentList ->
//            currentList.mapIndexed { i, comment ->
//                if (i == index) comment.copy(content = newContent) else comment
//            }
//        }
//    }
//
//    fun addComment(newComment: Comment) {
//        _comments.update { currentList ->
//            currentList + newComment
//        }
//    }


    /// 추가 내용 khs

    private val _isRemove = MutableLiveData<Boolean?>()
    val isRemove: LiveData<Boolean?> = _isRemove

    // 입력값
    // 댓글 내용
    val textFieldReplyContent =  mutableStateOf("")


    // 특정 게시글에 대한 댓글 불러오기 -> 댓글 상태에 따라 가져와야함..



    // 댓글 추가 -> 업데이트(불러오기)
    // ReplyData에 저장 후 CarryTalkData의 특정 게시글(talkDocumentId)의 talkReplyList에 추가


    // 댓글 수정 -> 업데이트(불러오기)




    // 댓글 삭제 -> 업데이트(불러오기)
    fun removeReply(replyDocumentId: String) {
        viewModelScope.launch {
            val isRemove = ReplyRepository.updateReplyState(replyDocumentId,ReplyState.REPLY_STATE_DELETE)
            _isRemove.postValue(isRemove)

            if (isRemove) {
                // 댓글 불러오기
                println("댓글 불러와..")

            }
        }
    }

    // 댓글 신고 -> 업데이트(불러오기)
    fun reportReply(replyDocumentId: String) {
        viewModelScope.launch {
            val isRemove = ReplyRepository.updateReplyState(replyDocumentId,ReplyState.REPLY_STATE_COMPLAINT)
            _isRemove.postValue(isRemove)

            if (isRemove) {
                // 댓글 불러오기
                println("댓글 불러와..")

            }
        }
    }






}

