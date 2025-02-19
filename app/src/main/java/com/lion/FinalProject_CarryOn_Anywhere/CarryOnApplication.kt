package com.lion.FinalProject_CarryOn_Anywhere

import android.app.Application
import androidx.compose.material3.DrawerState
import androidx.navigation.NavHostController
import com.kakao.sdk.common.KakaoSdk
import com.lion.FinalProject_CarryOn_Anywhere.data.server.model.UserModel
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CarryOnApplication :Application(){

    override fun onCreate() {
        super.onCreate()
        // 카카오 SDK 초기화
        KakaoSdk.init(this, BuildConfig.KAKAO_API_KEY)
    }

    // 네비게이션
    lateinit var navHostController: NavHostController
    // NavigationDrawer를 제어하기 위한 변수
    lateinit var navigationDrawerState : DrawerState

    // 로그인한 사용자 객체
    lateinit var loginUserModel: UserModel

    // 로그인했는지
    //var isLoggedIn = MutableStateFlow(false)
}