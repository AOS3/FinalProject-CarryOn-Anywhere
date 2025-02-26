package com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.home

import android.content.Context
import androidx.compose.runtime.remember
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lion.FinalProject_CarryOn_Anywhere.CarryOnApplication
import com.lion.FinalProject_CarryOn_Anywhere.data.server.model.TripReviewModel
import com.lion.FinalProject_CarryOn_Anywhere.data.server.service.TripReviewService
import com.lion.FinalProject_CarryOn_Anywhere.data.server.service.UserService
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.ScreenName
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    @ApplicationContext context: Context
) : ViewModel() {

    val carryOnApplication = context as CarryOnApplication

    private val _topTripReviews = MutableLiveData<List<Pair<TripReviewModel, String>>>()
    val topTripReviews: LiveData<List<Pair<TripReviewModel, String>>> get() = _topTripReviews

    // 로그인 여부 확인
    val isLoggedIn = carryOnApplication.isLoggedIn

    init {
        gettingTopTripReviews()
    }

    // Top5 여행 후기 가져오기
    private fun gettingTopTripReviews() {
        viewModelScope.launch {
            val reviews = TripReviewService.fetchAllTripReviews()
                // 좋아요 수 내림차순 정렬
                .sortedByDescending { it.tripReviewLikeCount }
                // 상위 5개만 가져오기
                .take(5)

            val reviewWriterIds = reviews.map { review ->
                val userId = try {
                    UserService.selectUserDataByUserDocumentIdOne(review.userDocumentId).userId
                } catch (e: Exception) {
                    "UnknownUser" // 예외 발생 시 기본값 설정
                }
                Pair(review, userId)
            }

            _topTripReviews.postValue(reviewWriterIds)
        }
    }

    // 내 일정 보기 버튼 클릭
    fun buttonMainUserTripList(onLoginRequired: () -> Unit) {
        if (!isLoggedIn.value) {
            onLoginRequired()
            return
        }
        carryOnApplication.navHostController.navigate(ScreenName.MY_TRIP_PLAN.name)
    }

    // 일정 등록 버튼 클릭
    fun buttonMainAddTrip(onLoginRequired: () -> Unit) {
        if (!isLoggedIn.value) {
            onLoginRequired()
            return
        }

        carryOnApplication.previousScreen.value = ScreenName.MAIN_SCREEN.name
        carryOnApplication.navHostController.navigate(ScreenName.SELECT_TRIP_REGION.name)
    }

    // search 로 이동하는 메서드
    fun searchOnClick(){
        carryOnApplication.navHostController.navigate(ScreenName.PLACE_SEARCH_SCREEN.name)
    }



    // 여행 후기로 이동하는 메서드
    fun showTripReviewDetailScreen(){
        //carryOnApplication.navHostController.navigate()
    }
}