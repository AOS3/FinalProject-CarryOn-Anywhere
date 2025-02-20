package com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.home

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonSyntaxException
import com.lion.FinalProject_CarryOn_Anywhere.CarryOnApplication
import com.lion.FinalProject_CarryOn_Anywhere.R
import com.lion.FinalProject_CarryOn_Anywhere.data.api.TourAPI.TourAPIRetrofitClient
import com.lion.FinalProject_CarryOn_Anywhere.data.api.TourAPI.TourApiModel
import com.lion.FinalProject_CarryOn_Anywhere.data.api.TourApiHelper
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.ScreenName
import com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.social.Review
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import retrofit2.HttpException
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
    // 찜 상태 관리
    var isFavoriteEnable = mutableStateOf(false)
    // 검색 버튼 누름 여부
    var isSearchTriggered = mutableStateOf(false)


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

    // 검색 결과 처리
    fun fetchPlace() {
        val keyword = searchValue.value.trim()

        if (keyword.isEmpty()) {
            return
        }

        viewModelScope.launch {
            try {
                val response = TourAPIRetrofitClient.instance.getSearchPlaces(
                    serviceKey = "6d5mkmqFyluWJNMUzIer6qA43/S6w+LWlCCspcQwyeSs9fesUnARurM+nBCqBxQ982Sl0OoHXILuM8nFrjKsjQ==",
                    keyword = keyword
                )

                if (response.isSuccessful) {
                    val places = response.body()?.response?.body?.items?.item ?: emptyList()
                    Log.d("API_ERROR", "결과 수: ${places.size}")

                    // 변환 후 저장
                    _placeSearchList.value = places.map { place ->
                        val regionName = TourApiHelper.getAreaName(place.areacode)
                        val categoryName = TourApiHelper.getContentType(place.contenttypeid)

                        val imageUrl = place.firstimage ?: "https://example.com/default_image.jpg"

                        Log.d("API_CALL", "설정된 이미지 URL: $imageUrl") // ✅ 값이 정상적으로 들어가는지 확인

                        mapOf(
                            "contentid" to (place.contentid ?: ""),
                            "firstimage" to (place.firstimage ?: ""),
                            "title" to (place.title ?: "장소 정보 없음"),
                            "region" to regionName,
                            "category" to categoryName,
                            "address" to (place.addr1 ?: "주소 정보 없음"),
                            "call" to (place.tel ?: "전화번호 정보 없음")
                        )
                    }
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