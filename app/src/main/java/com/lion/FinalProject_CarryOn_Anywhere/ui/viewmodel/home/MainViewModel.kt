package com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.home

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.remember
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lion.FinalProject_CarryOn_Anywhere.CarryOnApplication
import com.lion.FinalProject_CarryOn_Anywhere.data.server.model.BannerModel
import com.lion.FinalProject_CarryOn_Anywhere.data.server.model.TripReviewModel
import com.lion.FinalProject_CarryOn_Anywhere.data.server.service.BannerService
import com.lion.FinalProject_CarryOn_Anywhere.data.server.service.TripReviewService
import com.lion.FinalProject_CarryOn_Anywhere.data.server.service.UserService
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.ScreenName
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val bannerService: BannerService,
) : ViewModel() {

    val carryOnApplication = context as CarryOnApplication

    // 인기 후기
    private val _topTripReviews = MutableLiveData<List<Pair<TripReviewModel, String>>>()
    val topTripReviews: LiveData<List<Pair<TripReviewModel, String>>> get() = _topTripReviews

    // 배너 목록
    private val _bannerList = MutableLiveData<List<BannerModel>>()
    val bannerList: LiveData<List<BannerModel>> get() = _bannerList
    // 배너 로딩 상태
    private val _isBannerLoading = MutableStateFlow(true)
    val isBannerLoading: StateFlow<Boolean> = _isBannerLoading

    // 로그인 여부 확인
    val isLoggedIn = carryOnApplication.isLoggedIn

    init {
        gettingTopTripReviews()
        gettingBannerList()
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
    fun showTripReviewDetailScreen(reviewDocumentId: String){
        carryOnApplication.navHostController.navigate("reviewDetail/$reviewDocumentId")
    }

    private fun gettingBannerList() {
        viewModelScope.launch {
            // 배너 로딩 시작
            _isBannerLoading.value = true

            try {
                val bannerList = bannerService.gettingBannerList()
                // Log.d("BannerDebug", "배너 리스트 수: ${bannerList.size}")
                bannerList.forEach { banner ->
                    val imageUri = bannerService.gettingImage(banner.bannerImage)
                    banner.bannerImage = imageUri?.toString() ?: "https://your-default-image-url.com/default.jpg"
                }
                _bannerList.postValue(bannerList)
            } catch (e:Exception) {
                _bannerList.postValue(emptyList())
            }finally {
                _isBannerLoading.value = false
            }
        }
    }

    // 배너 클릭 시 딥링크 이동
    fun navigateToDeepLink(deepLink: String) {
        try {
            if (deepLink.startsWith("http")) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(deepLink))
                carryOnApplication.applicationContext.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                //Log.d("BannerClick", "외부 브라우저 실행됨: $deepLink")
            } else {
                carryOnApplication.navHostController.navigate(deepLink)
                //Log.d("BannerClick", "내부 네비게이션 성공: $deepLink")
            }
        } catch (e: Exception) {
            Log.e("BannerClick", "네비게이션 실패: ${e.message}")
        }
    }
}