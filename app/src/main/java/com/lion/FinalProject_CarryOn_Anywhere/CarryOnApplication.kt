package com.lion.FinalProject_CarryOn_Anywhere

import android.app.Application
import androidx.compose.material3.DrawerState
import androidx.navigation.NavHostController
import com.lion.FinalProject_CarryOn_Anywhere.data.server.model.UserModel
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.UserState
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CarryOnApplication :Application(){
    // 네비게이션
    lateinit var navHostController: NavHostController
    // NavigationDrawer를 제어하기 위한 변수
    lateinit var navigationDrawerState : DrawerState

    // 로그인한 사용자 객체
    lateinit var loginCustomerModel: UserModel



    // 로그인 여부
    //var isLoggedIn = MutableStateFlow(false)

    override fun onCreate() {
        super.onCreate()

        // ✅ 테스트용 유저 정보
        loginCustomerModel = UserModel().apply {
            userDocumentId = "testUser123"
            userKakaoToken = "dummyToken123"
            userImage = ""
            userId = "testUser"
            userPw = "testPassword"
            userName = "테스트 유저"
            userPhoneNumber = "010-1234-5678"
            userLikeList = mutableListOf("여행지1", "여행지2")
            userTripList = mutableListOf("여행 계획1")
            userTripReviewList = mutableListOf("후기1")
            userTalkList = mutableListOf("게시글1")
            userReplyList = mutableListOf("댓글1")
            userAutoLoginToken = "dummyAutoLoginToken"
            userTimeStamp = System.currentTimeMillis()
            userState = UserState.USER_STATE_NORMAL // 정상 계정
            userAppPushAgree = "미동의" // 푸시 알림 동의
        }
    }
}