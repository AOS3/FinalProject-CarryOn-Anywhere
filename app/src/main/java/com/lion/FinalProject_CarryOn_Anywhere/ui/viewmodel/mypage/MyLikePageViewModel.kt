package com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.mypage

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lion.FinalProject_CarryOn_Anywhere.CarryOnApplication
import com.lion.FinalProject_CarryOn_Anywhere.data.api.TourAPI.TourAPIRetrofitClient
import com.lion.FinalProject_CarryOn_Anywhere.data.api.TourAPI.TourApiHelper
import com.lion.FinalProject_CarryOn_Anywhere.data.api.TourAPI.TourApiModel
import com.lion.FinalProject_CarryOn_Anywhere.data.server.service.UserService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class MyLikePageViewModel @Inject constructor(
    @ApplicationContext context: Context
) : ViewModel()  {

    val carryOnApplication = context as CarryOnApplication

    // 로그인한 유저 문서 아이디
    private val userDocumentId = carryOnApplication.loginUserModel.userDocumentId

    // 사용자 찜 목록 저장 변수
    private val _userLikeList = MutableStateFlow<List<Map<String, String>>>(emptyList())
    val userLikeList: StateFlow<List<Map<String, String>>> = _userLikeList

    // 로딩 상태 관리
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // 선택한 장소 카테고리
    private val _selectedCategory = MutableStateFlow("전체")
    val selectedCategory: StateFlow<String> = _selectedCategory
    // 필터링 된 찜 리스트
    private val _filteredLikeList = MutableStateFlow<List<Map<String, Any>>>(emptyList())
    val filteredLikeList: StateFlow<List<Map<String, Any>>> = _filteredLikeList

    // 장소 정보
    private val _placeInfo = MutableStateFlow<List<Map<String, Any>>>(emptyList())
    val placeInfo: StateFlow<List<Map<String, Any>>> = _placeInfo

    init {
        gettingUserLikeList()
    }

    // `TouristSpotItem`을 `Map<String, Any>`로 변환하는 메서드
    private fun convertToMap(place: TourApiModel.TouristSpotDetailItem): Map<String, Any> {
        return mapOf(
            "contentid" to (place.contentid ?: ""),
            "contenttypeid" to (place.contenttypeid ?: ""),
            "firstimage" to (place.firstimage ?: ""),
            "title" to (place.title ?: "장소 정보 없음"),
            "region" to TourApiHelper.getAreaName(place.areacode),
            "category" to TourApiHelper.getContentType(place.contenttypeid),
            "isLoading" to true // 로딩 값,
        )
    }

    // 로딩 상태 업데이트
    private fun updateLoadingState() {
        _placeInfo.value = _placeInfo.value.map { place ->
            place.toMutableMap().apply { this["isLoading"] = false }
        }
        //Log.d("LOADING_STATE", "updateLoadingState 실행됨. 변경 후: ${_placeInfo.value}")

    }

    // 유저의 찜 목록을 가져온다.
    fun gettingUserLikeList() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val userLikes = UserService.getUserLikeList(userDocumentId)

                //Log.d("MY_LIKE_DEBUG", "Firebase에서 가져온 찜 목록: $userLikes")

                val likeList = userLikes.mapNotNull { like ->
                    val contentId = like["contentid"]
                    val contentTypeId = like["contenttypeid"]
                    if (!contentId.isNullOrEmpty() && !contentTypeId.isNullOrEmpty()) {
                        mapOf("contentid" to contentId, "contenttypeid" to contentTypeId)
                    } else {
                        null
                    }
                }

                _userLikeList.value = likeList
                fetchLikePlaceInfo(likeList)

            } catch (e: Exception) {
                Log.e("MyLikeViewModel", "찜 목록 가져오기 실패: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    // 장소를 불러온다.
    fun fetchLikePlaceInfo(likeList: List<Map<String, String>>) {
        viewModelScope.launch {

            _isLoading.value = true

            if (likeList.isEmpty()) {
                _placeInfo.value = emptyList()
                _isLoading.value = false
                return@launch
            }

            val placeInfoList = mutableListOf<Map<String, String>>()

            try {
                val deferredResults = likeList.map { place ->
                    async {
                        val contentId = place["contentid"] ?: return@async null
                        val contentTypeId = place["contenttypeid"] ?: return@async null

                        try {
                            val response = TourAPIRetrofitClient.instance.getDetailCommon1(
                                serviceKey = "Dv9oAbX/dy1WYtUtdQlhwy6o0rZyscllzmIsF9l4iLwlLtX2YeGQo9vzZl7ZUz4ez4BzWLCoBIvih9MgPFpiYQ==",
                                contentId = contentId,
                                contentTypeId = contentTypeId,
                            )

                            if (response.isSuccessful) {
                                val placeInfo = response.body()?.response?.body?.items?.item

                                placeInfo?.forEach {
                                    placeInfoList.add(convertToMap(it).mapValues { it.value.toString() })
                                }

                                // 로딩 상태 업데이트
                                delay(500)
                                updateLoadingState()
                            } else {
                                Log.e("API_ERROR", "API 응답 실패: ${response.errorBody()?.string()}")
                                null
                            }
                        } catch (e: Exception) {
                            Log.e("API_ERROR", "장소 정보 요청 실패: ${e.message}")
                            null
                        }
                    }
                }

                deferredResults.awaitAll()

            } finally {
                _placeInfo.value = placeInfoList
                _isLoading.value = false
            }
        }
    }

    // 찜 목록에서 contentTypeId 기준으로 필터링한다.
    fun filterByCategory(typeId: String) {
        viewModelScope.launch {
            Log.d("FILTER_DEBUG", "선택된 카테고리: $typeId") // ✅ 필터링 확인 로그 추가

            // 선택된 카테고리 업데이트
            _selectedCategory.value = TourApiHelper.contentTypeMap[typeId] ?: "전체"

            // "전체"가 선택되었을 때 모든 데이터 보여줌
            _filteredLikeList.value = if (typeId == "전체") {
                _placeInfo.value
            } else {
                _placeInfo.value.filter { it["contenttypeid"] == typeId }
            }

        }
    }


    // 찜 추가 삭제
    fun toggleFavorite(contentId: String, contentTypeId: String, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val userLikes = _userLikeList.value.toMutableList()
                val isLiked = userLikes.any { it["contentid"] == contentId }

                if (isLiked) {
                    UserService.deleteUserLikeList(userDocumentId, contentId)
                    userLikes.removeAll { it["contentid"] == contentId }
                } else {
                    UserService.addUserLikeList(userDocumentId, contentId, contentTypeId)
                    userLikes.add(mapOf("contentid" to contentId, "contenttypeid" to contentTypeId))
                }

                _userLikeList.value = userLikes
                onComplete(!isLiked)

                // 찜 목록 변경 후 fetchLikePlaceInfo() 실행
                if (userLikes.isNotEmpty()) {
                    fetchLikePlaceInfo(userLikes)
                } else {
                    _placeInfo.value = emptyList() // 빈 상태 처리
                }

            } catch (e: Exception) {
                Log.e("PlaceSearchViewModel", "찜 목록 업데이트 실패: ${e.message}")
            }
        }
    }

}