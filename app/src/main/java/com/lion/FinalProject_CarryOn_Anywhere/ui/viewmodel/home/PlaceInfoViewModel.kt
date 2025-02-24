package com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.home

import android.content.Context
import android.util.Log
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
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class PlaceInfoViewModel @Inject constructor(
    @ApplicationContext context: Context
) : ViewModel()  {

    val carryOnApplication = context as CarryOnApplication

    // 상세 정보
    private val _placeDetail = MutableStateFlow<List<Map<String, Any>>>(emptyList())
    val placeDetail: StateFlow<List<Map<String, Any>>> = _placeDetail

    // 로딩 상태 관리
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

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
            "homepage" to (place.homepage ?: "홈페이지 정보 없음"),
            "tel" to (place.tel ?: "전화번호 정보 없음"),
            "overview" to (place.overview ?: "상세 설명 없음"),
            )
    }

    fun fetchPlaceInfo(contentId: String, contentTypeId: String) {
        viewModelScope.launch {
            try {
                val response = TourAPIRetrofitClient.instance.getDetailCommon1(
                    serviceKey = "6d5mkmqFyluWJNMUzIer6qA43/S6w+LWlCCspcQwyeSs9fesUnARurM+nBCqBxQ982Sl0OoHXILuM8nFrjKsjQ==",
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


}