package com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.social

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import com.lion.FinalProject_CarryOn_Anywhere.R

// 게시글 데이터 클래스
data class Post(
    val tag: String,
    val title: String,
    val content: String,
    val author: String,
    val postDate: String,
    val likes: Int,
    val comments: Int,
    val imageRes: List<Int>? = null // 이미지가 없는 경우도 고려
)

@HiltViewModel
class StoryViewModel @Inject constructor() : ViewModel() {
    private val _posts= MutableStateFlow(
        listOf(
            Post("맛집", "서울 맛집 투어", "경복궁 근처 맛집 추천", "김철수", "2025-02-12", 5, 12, listOf(R.drawable.sample1, R.drawable.sample2)),
            Post("숙소", "부산 오션뷰 호텔", "뷰가 정말 예뻐요!", "이영희", "2025-02-10", 7, 18, listOf(R.drawable.sample1, R.drawable.sample2)),
            Post("여행 일정", "강릉 여행 코스", "이런 일정 어때요?", "박민수", "2025-01-25", 10, 22),
            Post("맛집", "서울 맛집 투어", "경복궁 근처 맛집 추천", "김철수", "2025-02-12", 5, 12, listOf(R.drawable.sample1, R.drawable.sample2)),
            Post("숙소", "부산 오션뷰 호텔", "뷰가 정말 예뻐요!", "이영희", "2025-02-10", 7, 18, listOf(R.drawable.sample1, R.drawable.sample2)),
            Post("여행 일정", "강릉 여행 코스", "이런 일정 어때요?", "박민수", "2025-01-25", 10, 22)
        )
    )

    val posts: StateFlow<List<Post>> = _posts
}