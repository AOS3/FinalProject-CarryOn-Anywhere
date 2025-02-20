package com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lion.FinalProject_CarryOn_Anywhere.CarryOnApplication
import com.lion.FinalProject_CarryOn_Anywhere.data.api.TourAPI.TourAPIRetrofitClient
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

    private val _placeDetail = MutableStateFlow<Map<String, Any>?>(null)
    val placeDetail: StateFlow<Map<String, Any>?> = _placeDetail

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

    fun fetchPlaceInfo(contentId: String) {
        viewModelScope.launch {
            try {
                val response = TourAPIRetrofitClient.instance.getSearchPlaces(
                    serviceKey = "6d5mkmqFyluWJNMUzIer6qA43/S6w+LWlCCspcQwyeSs9fesUnARurM+nBCqBxQ982Sl0OoHXILuM8nFrjKsjQ==",
                    keyword = "",
                )

                if (response.isSuccessful) {
                    val place = response.body()?.response?.body?.items?.item?.firstOrNull()

                    place?.let {
                        _placeDetail.value = mapOf(
                            "imageRes" to (it.firstimage ?: ""),
                            "title" to (it.title ?: "장소 정보 없음"),
                            "region" to (it.areacode ?: "지역 정보 없음"),
                            "category" to (it.contenttypeid ?: ""),
                            "address" to (it.addr1 ?: "주소 정보 없음"),
                            "call" to (it.tel ?: "전화번호 정보 없음"),
                            "contentid" to (it.contentid ?: "ID 없음")
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


}