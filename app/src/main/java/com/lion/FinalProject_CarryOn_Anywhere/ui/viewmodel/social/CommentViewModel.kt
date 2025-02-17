package com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.social

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

// 게시글 데이터 클래스
data class Comment(
    val author: String,
    val content: String,
    val commentDate: String,
)

@HiltViewModel
class CommnetViewModel @Inject constructor() : ViewModel() {
    private val _comments= MutableStateFlow(
        listOf(
            Comment("김철수", "경복궁 근처 맛집 추천", "2025-02-12"),
            Comment("이영희", "뷰가 정말 예뻐요!", "2025-02-10"),
            Comment("박민수", "이런 일정 어때요?", "2025-01-25")
        )
    )

    val comments: StateFlow<List<Comment>> = _comments
}