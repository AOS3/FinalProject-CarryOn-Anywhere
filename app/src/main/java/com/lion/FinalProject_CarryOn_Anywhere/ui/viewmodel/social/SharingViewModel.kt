package com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.social

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.lion.FinalProject_CarryOn_Anywhere.data.server.model.TripModel
import com.lion.FinalProject_CarryOn_Anywhere.data.server.service.TripService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// 일정 공유 데이터 클래스
data class Share(
    val documentId: String,
    val title: String,
    val resisterDate: Long,
    val startDateTime: Long,
    val endDateTime: Long,
    val tripCityList: List<Map<String, Any>> = emptyList(),
    val planList: List<Map<String, Any>> = emptyList()
)

@HiltViewModel
class SharingViewModel @Inject constructor(
    private val tripService: TripService
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _shares = MutableStateFlow<List<Share>>(emptyList())
    val shares: StateFlow<List<Share>> get() = _shares

    // loginUserId를 파라미터로 받도록 변경
    fun fetchUserTripReviews(loginUserId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                if (loginUserId == "guest") {
                    _shares.value = emptyList() // 비회원이면 일정 없음
                    return@launch
                }

                // TripService를 통해 특정 유저의 일정 가져오기
                val tripData: List<TripModel> = tripService.gettingTripList(loginUserId)

                // TripModel → Share 변환
                val shareList = tripData.map { trip ->
                    Share(
                        documentId = trip.tripDocumentId,
                        title = trip.tripTitle,
                        resisterDate = trip.tripTimeStamp,
                        startDateTime = trip.tripStartDate,
                        endDateTime = trip.tripEndDate,
                        tripCityList = trip.tripCityList.mapNotNull { it as? Map<String, Any> },
                        planList = trip.planList.map { plan ->
                            mapOf("id" to plan)
                        }
                    )
                }.sortedByDescending { it.resisterDate }

                _shares.value = shareList
            } catch (e: Exception) {
                Log.e("SharingViewModel", "데이터 불러오기 실패: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}
