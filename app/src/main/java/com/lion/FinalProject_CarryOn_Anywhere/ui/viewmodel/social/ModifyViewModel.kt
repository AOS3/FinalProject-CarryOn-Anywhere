package com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.social.Post
import com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.social.Review
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ModifyViewModel @Inject constructor(
    @ApplicationContext context: Context
) : ViewModel() {

    val postItems = listOf("여행 후기", "여행 이야기")
    val chipItems = listOf("맛집", "숙소", "여행 일정", "모임")

    private val _title = MutableStateFlow("")
    val title: StateFlow<String> = _title

    private val _content = MutableStateFlow("")
    val content: StateFlow<String> = _content

    private val _imageUris = MutableStateFlow<List<Uri>>(emptyList())
    val imageUris: StateFlow<List<Uri>> = _imageUris

    private val _selectedPostChip = MutableStateFlow("여행 후기")
    val selectedPostChip: StateFlow<String> = _selectedPostChip

    private val _selectedChip = MutableStateFlow("전체")
    val selectedChip: StateFlow<String> = _selectedChip


    fun addImages(clipData: android.content.ClipData, context: Context) {
        viewModelScope.launch {
            val selectedImages = mutableListOf<Uri>()
            val totalSelected = clipData.itemCount

            for (i in 0 until clipData.itemCount) {
                if (selectedImages.size + _imageUris.value.size < 10) {
                    selectedImages.add(clipData.getItemAt(i).uri)
                }
            }
            _imageUris.value = _imageUris.value + selectedImages

            if (totalSelected > 10) {
                Toast.makeText(context, "한번에 최대 10개의 사진 업로드가 가능합니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 데이터 초기화
    fun loadData(review: Review? = null, post: Post? = null) {
        review?.let {
            _title.value = it.title
            _content.value = it.content
            _selectedPostChip.value = "여행 후기"
            _imageUris.value = it.imageUrls as List<Uri>
        }

        post?.let {
            _title.value = it.title
            _content.value = it.content
            _selectedPostChip.value = "여행 이야기"
            _selectedChip.value = it.tag
            _imageUris.value = (it.imageUrls ?: emptyList()) as List<Uri>
        }
    }


    fun clearData() {
        _title.value = ""
        _content.value = ""
        _imageUris.value = emptyList()
    }

    fun addSingleImage(uri: Uri) {
        viewModelScope.launch {
            if (_imageUris.value.size < 10) {
                _imageUris.value = _imageUris.value + uri
            }
        }
    }

    fun removeImage(index: Int) {
        viewModelScope.launch {
            val currentList = _imageUris.value.toMutableList()
            if (index in currentList.indices) {
                currentList.removeAt(index)
                _imageUris.value = currentList
            }
        }
    }

    fun updateSelectedPostChip(text: String) {
        _selectedPostChip.value = text
    }

    fun updateSelectedChip(text: String) {
        _selectedChip.value = text
    }

    fun updateTitle(newTitle: String) {
        _title.value = newTitle
    }

    fun updateContent(newContent: String) {
        _content.value = newContent
    }
}
