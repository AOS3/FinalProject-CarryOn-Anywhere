package com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.social

import android.content.Context
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

// 게시글 데이터 클래스
data class Comment(
    val author: String,
    var content: String,
    val commentDate: String
)

@HiltViewModel
class CommnetViewModel @Inject constructor(
    @ApplicationContext context: Context
) : ViewModel() {
    private val _comments = MutableStateFlow(
        listOf(
            Comment("김철수", "경복궁 근처 맛집 추천", "2025-02-12"),
            Comment("이영희", "뷰가 정말 예뻐요!", "2025-02-10"),
            Comment("박민수", "이런 일정 어때요?", "2025-01-25"),
            Comment("김철수", "경복궁 근처 맛집 추천", "2025-02-12"),
            Comment("이영희", "뷰가 정말 예뻐요!", "2025-02-10"),
            Comment("박민수", "이런 일정 어때요?", "2025-01-25"),
            Comment("김철수", "경복궁 근처 맛집 추천", "2025-02-12"),
            Comment("이영희", "뷰가 정말 예뻐요!", "2025-02-10"),
            Comment("박민수", "이런 일정 어때요?", "2025-01-25")
        )
    )

    val comments: StateFlow<List<Comment>> = _comments

    fun updateCommentContent(index: Int, newContent: String) {
        _comments.update { currentList ->
            currentList.mapIndexed { i, comment ->
                if (i == index) comment.copy(content = newContent) else comment
            }
        }
    }

    fun addComment(newComment: Comment) {
        _comments.update { currentList ->
            currentList + newComment
        }
    }
}