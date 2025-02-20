package com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.home

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lion.FinalProject_CarryOn_Anywhere.CarryOnApplication
import com.lion.FinalProject_CarryOn_Anywhere.data.api.TourAPI.TourAPIRetrofitClient
import com.lion.FinalProject_CarryOn_Anywhere.data.api.TourAPI.TourApiHelper
import com.lion.FinalProject_CarryOn_Anywhere.data.api.TourAPI.TourApiModel
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.ScreenName
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaceSearchViewModel @Inject constructor(
    @ApplicationContext context: Context
) : ViewModel()  {

    val carryOnApplication = context as CarryOnApplication

    // 검색 결과
    private val _placeSearchList = MutableStateFlow<List<Map<String, Any>>>(emptyList())
    val placeSearchList: StateFlow<List<Map<String, Any>>> = _placeSearchList

    // 검색어
    var searchValue = mutableStateOf("")
    // 찜 상태 관리
    var isFavoriteEnable = mutableStateOf(false)
    // 검색 버튼 누름 여부
    var isSearchTriggered = mutableStateOf(false)
    // 현재 페이지 번호
    var currentPage = 1
    // 로딩 상태 관리
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Back 버튼 동작 메서드
    fun navigationBackIconOnClick() {
        carryOnApplication.navHostController.popBackStack(
            ScreenName.PLACE_SEARCH_SCREEN.name,
            inclusive = true
        )
        carryOnApplication.navHostController.navigate(ScreenName.MAIN_SCREEN.name) {
            launchSingleTop = true
        }
    }

    // `TouristSpotItem`을 `Map<String, Any>`로 변환하는 메서드
    private fun convertToMap(place: TourApiModel.TouristSpotItem): Map<String, Any> {
        return mapOf(
            "contentid" to (place.contentid ?: ""),
            "firstimage" to (place.firstimage ?: ""),
            "title" to (place.title ?: "장소 정보 없음"),
            "region" to TourApiHelper.getAreaName(place.areacode),
            "category" to TourApiHelper.getContentType(place.contenttypeid),
            "address" to (place.addr1 ?: "주소 정보 없음"),
            "call" to (place.tel ?: "전화번호 정보 없음")
        )
    }

    // 검색 실행 (첫 페이지)
    fun fetchPlace() {
        val keyword = searchValue.value.trim()
        if (keyword.isEmpty() || _isLoading.value) return

        _isLoading.value = true
        currentPage = 1

        viewModelScope.launch {
            try {
                val response = TourAPIRetrofitClient.instance.getSearchPlaces(
                    serviceKey = "Dv9oAbX/dy1WYtUtdQlhwy6o0rZyscllzmIsF9l4iLwlLtX2YeGQo9vzZl7ZUz4ez4BzWLCoBIvih9MgPFpiYQ==",
                    keyword = keyword,
                    pageNo = currentPage
                )

                if (response.isSuccessful) {
                    val places = response.body()?.response?.body?.items?.item ?: emptyList()

                    Log.d("API_CALL", "첫 페이지 로드 완료. 개수: ${places.size}")

                    _placeSearchList.value = places.map { convertToMap(it) }
                } else {
                    Log.e("API_ERROR", "API 응답 실패: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("API_ERROR", "네트워크 오류: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    // 다음 페이지 로드
    fun fetchNextPage() {
        if (_isLoading.value) return

        _isLoading.value = true
        currentPage++  // 다음 페이지 요청

        viewModelScope.launch {
            try {
                val response = TourAPIRetrofitClient.instance.getSearchPlaces(
                    serviceKey = "Dv9oAbX/dy1WYtUtdQlhwy6o0rZyscllzmIsF9l4iLwlLtX2YeGQo9vzZl7ZUz4ez4BzWLCoBIvih9MgPFpiYQ==",
                    keyword = searchValue.value.trim(),
                    pageNo = currentPage
                )

                if (response.isSuccessful) {
                    val newPlaces = response.body()?.response?.body?.items?.item ?: emptyList()

                    if (newPlaces.isNotEmpty()) {
                        _placeSearchList.value = _placeSearchList.value + newPlaces.map { convertToMap(it) }
                        Log.d("API_CALL", "다음 페이지 로드 완료. 현재 페이지: $currentPage")
                    }
                }
            } catch (e: Exception) {
                Log.e("API_ERROR", "네트워크 오류: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    // 검색 버튼 동작 메서드
    fun searchAndHideKeyboard(keyboardController: SoftwareKeyboardController?) {
        fetchPlace()
        keyboardController?.hide()
    }

    // 검색 필드 초기화 메서드
    fun clearSearch() {
        searchValue.value = ""
        _placeSearchList.value = emptyList()
        isSearchTriggered.value = false
    }



    // 찜 아이콘 상태 변경
    fun toggleFavorite() {
        isFavoriteEnable.value = !isFavoriteEnable.value
    }


}