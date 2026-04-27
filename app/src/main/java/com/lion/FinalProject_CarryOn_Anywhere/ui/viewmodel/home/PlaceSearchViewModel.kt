package com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.home

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonSyntaxException
import com.kakao.sdk.user.model.User
import com.lion.FinalProject_CarryOn_Anywhere.CarryOnApplication
import com.lion.FinalProject_CarryOn_Anywhere.data.api.TourAPI.TourAPIRetrofitClient
import com.lion.FinalProject_CarryOn_Anywhere.data.api.TourAPI.TourApiHelper
import com.lion.FinalProject_CarryOn_Anywhere.data.api.TourAPI.TourApiModel
import com.lion.FinalProject_CarryOn_Anywhere.data.server.service.UserService
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.ScreenName
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException
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

    // 검색어를 리스트로 저장
    val searchKeywords = mutableStateOf<List<String>>(emptyList())

    // 사용자 찜 목록 저장 변수
    private val _userLikeList = MutableStateFlow<List<Map<String, String>>>(emptyList())
    val userLikeList: StateFlow<List<Map<String, String>>> = _userLikeList

    // 로그인한 유저 문서 아이디
    private val userDocumentId: String?
        get() = carryOnApplication.loginUserModel?.userDocumentId

    // 검색 버튼 누름 여부
    var isSearchTriggered = mutableStateOf(false)

    // 현재 페이지 번호
    var currentPage = 1

    // 로그인 여부 확인
    val isLoggedIn = carryOnApplication.isLoggedIn

    // 로딩 상태 관리
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        // 로그인한 경우에만 찜 목록 불러오기, `loginUserModel` 초기화
        if (isLoggedIn.value && carryOnApplication.loginUserModel != null) {
            gettingUserLikeList()
        }
    }

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
            "contenttypeid" to (place.contenttypeid ?: ""),
            "firstimage" to (place.firstimage ?: ""),
            "title" to (place.title ?: "장소 정보 없음"),
            "areacode" to TourApiHelper.getAreaName(place.areacode),
            "address" to (place.addr1 ?: "주소 정보 없음"),
            "call" to (place.tel ?: "전화번호 정보 없음"),
            "isLoading" to true // 로딩 값
        )
    }

    // 로딩 상태 업데이트
    private fun updateLoadingState() {
        _placeSearchList.value = _placeSearchList.value.map { place ->
            place + ("isLoading" to false)
        }
    }

    // 검색 실행 (첫 페이지)
    fun fetchPlace() {
        val keyword = searchValue.value.trim()

        if (keyword.isEmpty() || _isLoading.value) return

        _isLoading.value = true
        currentPage = 1
        // 검색 시도
        isSearchTriggered.value = true

        // 검색어 공백 기준으로 분리
        searchKeywords.value = keyword.split(" ").filter { it.isNotEmpty() }

        viewModelScope.launch {
            try {
                _placeSearchList.value = emptyList()
                val response = TourAPIRetrofitClient.instance.getSearchPlaces(
                    serviceKey = carryOnApplication.tourApiKey,
                    keyword = searchKeywords.value.joinToString(" "),
                    pageNo = currentPage
                )

                if (response.isSuccessful) {
                    val places = response.body()?.response?.body?.items?.item ?: emptyList()

                    //Log.d("API_CALL", "첫 페이지 로드 완료. 개수: ${places.size}")

                    // 데이터 변환 시, `isLoading = true`로 초기화
                    _placeSearchList.value = places.map { convertToMap(it).toMutableMap().apply { put("isLoading", true) } }

                    // 일정 시간 후 로딩 상태를 false로 변경
                    delay(500) // 데이터 로드 후 약간의 딜레이 (UI 업데이트 보장)
                    updateLoadingState()
                } else {
                    // 검색 결과가 없을 경우 빈 리스트 유지
                    _placeSearchList.value = emptyList()
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
                    serviceKey = carryOnApplication.tourApiKey,
                    keyword = searchValue.value.trim(),
                    pageNo = currentPage
                )

                if (response.isSuccessful) {
                    val newPlaces = response.body()?.response?.body?.items?.item ?: emptyList()

                    if (newPlaces.isNotEmpty()) {
                        val newPlaceList = newPlaces.map { convertToMap(it).toMutableMap().apply { put("isLoading", true) } }

                        // 기존 리스트에 새 리스트 추가
                        _placeSearchList.value = _placeSearchList.value + newPlaceList

                        // 로딩 상태 업데이트
                        delay(500)
                        updateLoadingState()

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

    // 사용자의 찜 목록 가져오기 (Firestore에서 불러오기)
    fun gettingUserLikeList() {
        // `userDocumentId`가 `null`이면 실행하지 않음
        val userId = userDocumentId ?: return

        viewModelScope.launch {
            try {
                val userLikes = UserService.getUserLikeList(userId)
                _userLikeList.value = userLikes
            } catch (e: Exception) {
                Log.e("PlaceSearchViewModel", "찜 목록 가져오기 실패: ${e.message}")
            }
        }
    }

    // 찜 목록 추가/삭제
    fun toggleFavorite(
        contentId: String,
        contentTypeId: String,
        onLoginRequired: () -> Unit,
        onComplete: (Boolean) -> Unit,
    ) {
        // 로그인 유도 팝업 호출
        if (!isLoggedIn.value) {
            onLoginRequired()
            return
        }

        // `userDocumentId`가 `null`이면 실행하지 않음
        val userId = userDocumentId ?: return

        viewModelScope.launch {
            try {
                val userLikes = _userLikeList.value.toMutableList()
                val isLiked = userLikes.any { it["contentid"] == contentId }

                if (isLiked) {
                    // 찜 삭제
                    UserService.deleteUserLikeList(userId, contentId)
                    userLikes.removeAll { it["contentid"] == contentId }
                } else {
                    // 찜 추가
                    UserService.addUserLikeList(userId, contentId, contentTypeId)
                    userLikes.add(mapOf("contentid" to contentId, "contenttypeid" to contentTypeId))
                }

                _userLikeList.value = userLikes
                onComplete(!isLiked)
            } catch (e: Exception) {
                Log.e("PlaceSearchViewModel", "찜 목록 업데이트 실패: ${e.message}")
            }
        }

    }

    // 장소 추가 요청 버튼 눌렀을 때
    fun requestPlaceOnClick() {
        carryOnApplication.previousScreen.value = ScreenName.PLACE_SEARCH_SCREEN.name

        carryOnApplication.navHostController.popBackStack()
        carryOnApplication.navHostController.navigate(ScreenName.WRITE_REQUEST_PLACE.name)
    }
}