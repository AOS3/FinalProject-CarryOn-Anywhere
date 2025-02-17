package com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.social

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

// 일정 공유 데이터 클래스
data class Share(
    val title: String,
    val date: String,
)
@HiltViewModel
class SharingViewModel @Inject constructor() : ViewModel() {
    private val _shares = MutableStateFlow(
        listOf(
            Share("서울 맛집 투어", "2025-02-12"),
            Share("부산 오션뷰 호텔", "2025-02-10"),
            Share("강릉 여행 코스", "2025-01-25"),
            Share("서울 맛집 투어", "2025-02-12"),
        )
    )

    val shares: StateFlow<List<Share>> = _shares
}