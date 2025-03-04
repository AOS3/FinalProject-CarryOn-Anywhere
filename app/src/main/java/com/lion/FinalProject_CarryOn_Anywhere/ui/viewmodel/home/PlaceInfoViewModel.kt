package com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lion.FinalProject_CarryOn_Anywhere.CarryOnApplication
import com.lion.FinalProject_CarryOn_Anywhere.data.api.TourAPI.TourAPIRetrofitClient
import com.lion.FinalProject_CarryOn_Anywhere.data.api.TourAPI.TourApiHelper
import com.lion.FinalProject_CarryOn_Anywhere.data.api.TourAPI.TourApiModel
import com.lion.FinalProject_CarryOn_Anywhere.data.server.service.UserService
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.ScreenName
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class PlaceInfoViewModel @Inject constructor(
    @ApplicationContext context: Context
) : ViewModel()  {

    val carryOnApplication = context as CarryOnApplication

    // 로그인 여부 확인
    val isLoggedIn = carryOnApplication.isLoggedIn
    // 로그인한 유저 문서 아이디
    private val userDocumentId: String?
        get() = carryOnApplication.loginUserModel?.userDocumentId

    // 상세 정보
    private val _placeDetail = MutableStateFlow<List<Map<String, Any>>>(emptyList())
    val placeDetail: StateFlow<List<Map<String, Any>>> = _placeDetail

    // 로딩 상태 관리
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // 사용자 찜 목록 저장 변수
    private val _userLikeList = MutableStateFlow<List<Map<String, String>>>(emptyList())
    val userLikeList: StateFlow<List<Map<String, String>>> = _userLikeList

    // Back 버튼 동작 메서드
    fun navigationBackIconOnClick() {
        carryOnApplication.navHostController.popBackStack(
            ScreenName.PLACE_INFO_SCREEN.name,
            inclusive = true
        )
//        carryOnApplication.navHostController.navigate(ScreenName.MAIN_SCREEN.name) {
//            launchSingleTop = true
//        }
    }

    init {
        // 로그인한 경우에만 찜 목록 불러오기, `loginUserModel` 초기화
        if (isLoggedIn.value && carryOnApplication.loginUserModel != null) {
            gettingUserLikeList()
        }
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
            "address" to (place.addr1 ?: "주소 정보 없음"),
            "homepage" to TourApiHelper.extractUrl(place.homepage.toString()),
            "tel" to (place.tel ?: "전화번호 정보 없음"),
            "overview" to (place.overview ?: "상세 설명 없음"),
            )
    }

    fun fetchPlaceInfo(contentId: String, contentTypeId: String) {
        viewModelScope.launch {
            try {
                val response = TourAPIRetrofitClient.instance.getDetailCommon1(
                    serviceKey = carryOnApplication.tourApiKey,
                    contentId = contentId,
                    contentTypeId = contentTypeId,
                )

                if (response.isSuccessful) {
                    val placeInfo = response.body()?.response?.body?.items?.item ?: emptyList()

                    _placeDetail.value = placeInfo.map{ convertToMap(it) } // 첫 번째 아이템을 변환

                } else {
                    Log.e("API_ERROR", "API 응답 실패: ${response.errorBody()?.string()}")
                }
            } catch (e: IOException) {
                Log.e("API_ERROR", "네트워크 오류: ${e.message}")
            } catch (e: HttpException) {
                Log.e("API_ERROR", "HTTP 오류: ${e.message}")
            }
        }
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
                onComplete(false)
            }
        }

    }



}