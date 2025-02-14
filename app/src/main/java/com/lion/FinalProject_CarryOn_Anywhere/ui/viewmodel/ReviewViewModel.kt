package com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.lion.FinalProject_CarryOn_Anywhere.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

data class Review(
    val imageRes: List<Int>,
    val title: String,
    val author: String,
    val content: String,
    val postDate: String,
    val date: String,
    val likes: Int,
    val comments: Int
)

@HiltViewModel
class ReviewViewModel @Inject constructor() : ViewModel() {
    private val _reviews = MutableStateFlow(
        listOf(
            Review(
                imageRes = listOf(R.drawable.sample1, R.drawable.sample2),
                title = "서울 경복궁 여행 후기!!",
                author = "홍길동",
                content = "안녕하세요",
                postDate = "2025-02-12",
                date = "25-01-07 ~ 25-01-10",
                likes = 3,
                comments = 18
            ),
            Review(
                imageRes = listOf(R.drawable.sample1),
                title = "대전 빵투어 여행 후기",
                author = "김길동",
                content = "뷰가 정말 예뻐요!",
                postDate = "2025-02-12",
                date = "25-01-13 ~ 25-01-15",
                likes = 1,
                comments = 200
            ),
            Review(
                imageRes = listOf(R.drawable.sample2),
                title = "제주도 한달 살기",
                author = "박길동",
                content = "이런 일정 어때요?",
                postDate = "2025-02-13",
                date = "25-01-01 ~ 25-01-31",
                likes = 12,
                comments = 54
            ),
            Review(
                imageRes = listOf(R.drawable.sample2),
                title = "부산 야경 투어 후기",
                author = "이길동",
                content = "경복궁 근처 맛집 추천",
                postDate = "2025-02-14",
                date = "25-02-10 ~ 25-02-12",
                likes = 5,
                comments = 36
            )
        )
    )

//    private val _reviews = MutableStateFlow<List<Review>>(emptyList())

    val reviews: StateFlow<List<Review>> = _reviews
}