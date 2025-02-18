package com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel

import android.content.ClipData
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject


@HiltViewModel
class PostViewModel @Inject constructor(
    @ApplicationContext context: Context
) : ViewModel() {

    // UI에서 사용할 데이터 리스트
    val postItems = listOf("여행 후기", "여행 이야기")
    val chipItems = listOf("전체", "맛집", "숙소", "여행 일정", "모임")

    // 선택된 Chip 상태
    private val _selectedPostChip = MutableStateFlow(postItems[0])
    val selectedPostChip: StateFlow<String> get() = _selectedPostChip

    private val _selectedChip = MutableStateFlow(chipItems[0])
    val selectedChip: StateFlow<String> get() = _selectedChip

    // 이미지 URI 리스트
    private val _imageUris = MutableStateFlow<List<Uri>>(emptyList())
    val imageUris: StateFlow<List<Uri>> get() = _imageUris

    // 선택된 Post Chip 업데이트
    fun updateSelectedPostChip(chip: String) {
        _selectedPostChip.value = chip
    }

    // 선택된 Chip 업데이트
    fun updateSelectedChip(chip: String) {
        _selectedChip.value = chip
    }

    // 이미지 여러 개 추가
    fun addImages(clipData: ClipData, context: Context) {
        val selectedImages = mutableListOf<Uri>()
        val totalSelected = clipData.itemCount

        for (i in 0 until clipData.itemCount) {
            if (selectedImages.size + _imageUris.value.size < 10) {
                selectedImages.add(clipData.getItemAt(i).uri)
            }
        }

        _imageUris.value = _imageUris.value + selectedImages

        // 10개 이상 선택 시 토스트 알림
        if (totalSelected > 10) {
            Toast.makeText(context, "한번에 최대 10개의 사진 업로드가 가능합니다.", Toast.LENGTH_SHORT).show()
        }
    }

    // 단일 이미지 추가
    fun addSingleImage(uri: Uri) {
        if (_imageUris.value.size < 10) {
            _imageUris.value = _imageUris.value + uri
        }
    }

    // 이미지 삭제
    fun removeImage(index: Int) {
        _imageUris.value = _imageUris.value.toMutableList().apply { removeAt(index) }
    }
}
