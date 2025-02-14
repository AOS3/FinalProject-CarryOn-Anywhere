package com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SocialViewModel @Inject constructor() : ViewModel() {

    // MutableStateFlow로 변경하여 상태 유지
    private val _selectedTabIndex = MutableStateFlow(0)
    val selectedTabIndex: StateFlow<Int> = _selectedTabIndex

    // 탭 변경 시 즉시 상태 업데이트
    fun updateTabIndex(index: Int) {
        _selectedTabIndex.value = index
    }
}
