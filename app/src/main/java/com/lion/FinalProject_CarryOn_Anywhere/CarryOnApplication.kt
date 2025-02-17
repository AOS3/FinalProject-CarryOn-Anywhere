package com.lion.FinalProject_CarryOn_Anywhere

import android.app.Application
import androidx.compose.material3.DrawerState
import androidx.navigation.NavHostController
import com.lion.FinalProject_CarryOn_Anywhere.data.server.model.UserModel
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CarryOnApplication :Application(){
    // 네비게이션
    lateinit var navHostController: NavHostController
    // NavigationDrawer를 제어하기 위한 변수
    lateinit var navigationDrawerState : DrawerState

    // 로그인한 사용자 객체
    lateinit var loginCustomerModel: UserModel

    // 로그인했는지
    //var isLoggedIn = MutableStateFlow(false)
}