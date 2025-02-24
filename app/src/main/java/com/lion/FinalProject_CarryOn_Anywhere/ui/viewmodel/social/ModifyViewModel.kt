package com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.storage.FirebaseStorage
import com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.social.Post
import com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.social.Review
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID
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

    val _selectedChip = MutableStateFlow("전체")
    val selectedChip: StateFlow<String> = _selectedChip

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    fun setLoading(isLoading: Boolean) {
        _isLoading.value = isLoading
    }

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

    object ImageUploader {

        private val storage = FirebaseStorage.getInstance()
        private val storageRef = storage.reference.child("images")

        // 이미지 업로드 메서드
        suspend fun uploadImages(uriList: List<Uri>): List<String> {
            val downloadUrls = mutableListOf<String>()
            for (uri in uriList) {
                try {
                    // 고유 파일명 생성
                    val fileName = "IMG_${UUID.randomUUID()}.jpg"
                    val imageRef = storageRef.child(fileName)

                    // 파일 업로드
                    val uploadTask = imageRef.putFile(uri).await()

                    // 업로드 상태 확인
                    if (uploadTask.task.isSuccessful) {
                        val downloadUrl = imageRef.downloadUrl.await().toString()
                        downloadUrls.add(downloadUrl)
                        Log.d("ImageUploader", "이미지 업로드 성공: $downloadUrl")
                    } else {
                        Log.e("ImageUploader", "이미지 업로드 실패: ${uploadTask.error?.message}")
                    }
                } catch (e: Exception) {
                    Log.e("ImageUploader", "이미지 업로드 예외 발생: ${e.message}", e)
                }
            }
            return downloadUrls
        }
    }
}
