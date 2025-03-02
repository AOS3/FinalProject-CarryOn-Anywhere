package com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.storage.FirebaseStorage
import com.lion.FinalProject_CarryOn_Anywhere.data.server.service.PlanService
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
    @ApplicationContext context: Context,
    private val planService: PlanService
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

    // 선택된 제목
    private val _selectedTitle = MutableStateFlow("")
    val selectedTitle: StateFlow<String> get() = _selectedTitle

    // 선택된 시작 날짜
    private val _selectedStartDate = MutableStateFlow("")
    val selectedStartDate: StateFlow<String> get() = _selectedStartDate

    // 선택된 마지막 날짜
    private val _selectedEndDate = MutableStateFlow("")
    val selectedEndDate: StateFlow<String> get() = _selectedEndDate

    // 선택된 여행의 지역 목록
    private val _tripCityList = MutableStateFlow<List<Map<String, Any>>>(emptyList())
    val tripCityList: StateFlow<List<Map<String, Any>>> get() = _tripCityList

    // 선택된 여행의 일정 목록
    private val _planList = MutableStateFlow<List<Map<String, Any>>>(emptyList())
    val planList: StateFlow<List<Map<String, Any>>> get() = _planList

    // PlanData
    private val _dailyPlanData = MutableStateFlow<Map<String, List<Map<String, Any?>>>>(emptyMap())
    val dailyPlanData: StateFlow<Map<String, List<Map<String, Any?>>>> get() = _dailyPlanData

    val _selectedChip = MutableStateFlow("전체")
    val selectedChip: StateFlow<String> = _selectedChip

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _isComplete = MutableStateFlow(false)
    val isComplete: StateFlow<Boolean> get() = _isComplete

    // 선택된 제목 업데이트
    fun updateSelectedTitle(title: String) {
        _selectedTitle.value = title
    }

    // 선택된 시작 날짜 업데이트
    fun updateSelectedStartDate(date: String) {
        _selectedStartDate.value = date
    }

    // 선택된 마지막 날짜 업데이트
    fun updateSelectedEndDate(date: String) {
        _selectedEndDate.value = date
    }

    // 선택된 여행 지역 업데이트
    fun updateTripCityList(cityList: List<Map<String, Any>>) {
        _tripCityList.value = cityList
    }

    // 선택된 일정 목록 업데이트 후 일정 데이터 업데이트
    fun updatePlanList(planList: List<Map<String, Any>>) {
        _planList.value = planList
        fetchDailyPlanData() // Firestore에서 일정 데이터 가져오기
    }

    fun setLoading(isLoading: Boolean) {
        _isLoading.value = isLoading
    }

    fun setComplete(value: Boolean) {
        _isComplete.value = value
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

    fun loadInitialTripData(review: Review) {
        val date = review.tripDate
        val partsDate = date.split("~").map { it.trim() }
        val city = review.sharePlace
        val partsCity = city.flatMap { it.split("/").map { part -> part.trim() } }

        _selectedTitle.value = review.shareTitle
        _selectedStartDate.value = partsDate[0]
        _selectedEndDate.value = partsDate[1]
        _tripCityList.value = partsCity.chunked(2).map { chunk ->
            mapOf(
                "regionName" to (chunk.getOrNull(0) ?: ""),
                "subRegionName" to (chunk.getOrNull(1) ?: "")
            )
        }
        _planList.value = review.sharePlan

        //  _dailyPlanData 초기값
        if (_dailyPlanData.value.isEmpty()) {
            val initialPlanDataMap = mutableMapOf<String, MutableList<Map<String, Any?>>>()

            review.sharePlan.forEach { plan ->
                val planDate = plan["date"] ?: "미정"

                val modifiedPlan = plan.toMutableMap().apply {
                    this["addr1"] = this.remove("addr") ?: ""
                    this["addr2"] = this.remove("addrDetail") ?: ""
                    this["title"] = this.remove("place") ?: ""

                }

                initialPlanDataMap.getOrPut(planDate) { mutableListOf() }.add(modifiedPlan)
            }

            _dailyPlanData.value = initialPlanDataMap
        }
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
                        Log.d("ModifyViewModel", "이미지 업로드 성공: $downloadUrl")
                    } else {
                        Log.e("ModifyViewModel", "이미지 업로드 실패: ${uploadTask.error?.message}")
                    }
                } catch (e: Exception) {
                    Log.e("ModifyViewModel", "이미지 업로드 예외 발생: ${e.message}", e)
                }
            }
            return downloadUrls
        }
    }

    private fun fetchDailyPlanData() {
        viewModelScope.launch {
            val planDataMap = mutableMapOf<String, MutableList<Map<String, Any?>>>()

            for (plan in _planList.value) {

                val planId = plan["id"] as? String

                if (!planId.isNullOrEmpty()) {
                    val planData = planService.getPlansByTripDocumentId(planId)

                    planData?.placeList?.let { places ->
                        val dayKey = planData.planDay ?: "전체 일정"
                        if (!planDataMap.containsKey(dayKey)) {
                            planDataMap[dayKey] = mutableListOf()
                        }
                        planDataMap[dayKey]?.addAll(places)
                    }
                } else {
                    Log.e("ModifyViewModel", "planId 값이 없음")
                }
            }

            _dailyPlanData.value = planDataMap  // StateFlow 업데이트
            Log.d("ModifyViewModel", "불러온 일정 데이터: $planDataMap")
        }
    }
}
