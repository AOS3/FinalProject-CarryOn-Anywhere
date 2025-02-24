package com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel

import android.content.ClipData
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.storage.FirebaseStorage
import com.lion.FinalProject_CarryOn_Anywhere.data.server.model.CarryTalkModel
import com.lion.FinalProject_CarryOn_Anywhere.data.server.model.TripReviewModel
import com.lion.FinalProject_CarryOn_Anywhere.data.server.service.CarryTalkService
import com.lion.FinalProject_CarryOn_Anywhere.data.server.service.TripReviewService
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.TalkTag
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    @ApplicationContext context: Context
) : ViewModel() {
    // UI에서 사용할 데이터 리스트
    val postItems = listOf("여행 후기", "여행 이야기")
    val chipItems = listOf("맛집", "숙소", "여행 일정", "모임")

    // 선택된 Chip 상태
    private val _selectedPostChip = MutableStateFlow(postItems[0])
    val selectedPostChip: StateFlow<String> get() = _selectedPostChip

    private val _selectedChip = MutableStateFlow(chipItems[0])
    val selectedChip: StateFlow<String> get() = _selectedChip

    // 이미지 URI 리스트
    private val _imageUris = MutableStateFlow<List<Uri>>(emptyList())
    val imageUris: StateFlow<List<Uri>> get() = _imageUris

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    fun setLoading(isLoading: Boolean) {
        _isLoading.value = isLoading
    }


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

    // 카테고리에 따라 데이터 저장
    fun savePost(
        title: String,
        content: String,
        userDocumentId: String,
        userName: String,
        imageUrls: List<String>
    ) {
        val job = Job()
        val coroutineScope = CoroutineScope(Dispatchers.IO + job)

        coroutineScope.launch {
            _isLoading.value = true

            when (_selectedPostChip.value) {
                "여행 후기" -> {
                    val tripReview = TripReviewModel().apply {
                        this.userDocumentId = userDocumentId
                        this.userName = userName
                        this.tripReviewTitle = title
                        this.tripReviewContent = content
                        this.tripReviewImage = imageUrls.toMutableList()
                        this.tripReviewLikeCount = 0
                        this.tripReviewReplyList = mutableListOf()
                        this.tripReviewTimestamp = System.currentTimeMillis()
                    }

                    try {
                        val documentRef = TripReviewService.addTripReview(tripReview)
                        tripReview.tripDocumentId = documentRef.id
                        documentRef.set(tripReview).await()

                        Log.d("PostViewModel", "여행 후기 업로드 성공: ${tripReview.tripDocumentId}")
                    } catch (e: Exception) {
                        Log.e("PostViewModel", "여행 후기 업로드 실패: ${e.message}")
                    }
                }

                "여행 이야기" -> {
                    val carryTalk = CarryTalkModel().apply {
                        this.userDocumentId = userDocumentId
                        this.userName = userName
                        this.talkTitle = title
                        this.talkContent = content
                        this.talkImage = imageUrls.toMutableList()
                        this.talkLikeCount = 0
                        this.talkReplyList = mutableListOf()
                        this.talkTimeStamp = System.currentTimeMillis()
                        this.talkTag = when (_selectedChip.value) {
                            "맛집" -> TalkTag.TALK_TAG_RESTAURANT
                            "숙소" -> TalkTag.TALK_TAG_ACCOMMODATION
                            "여행 일정" -> TalkTag.TALK_TAG_TRIP_PLAN
                            "모임" -> TalkTag.TALK_TAG_MEET
                            else -> TalkTag.TALK_TAG_ALL
                        }
                    }

                    try {
                        val documentRef = CarryTalkService.addCarryTalkReview(carryTalk)
                        carryTalk.talkDocumentId = documentRef.id
                        documentRef.set(carryTalk).await()

                        Log.d("PostViewModel", "여행 이야기 업로드 성공: ${carryTalk.talkDocumentId}")
                    } catch (e: Exception) {
                        Log.e("PostViewModel", "여행 이야기 업로드 실패: ${e.message}")
                    }
                }
            }
            _isLoading.value = false
        }
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

                    // 🛠업로드 상태 확인
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