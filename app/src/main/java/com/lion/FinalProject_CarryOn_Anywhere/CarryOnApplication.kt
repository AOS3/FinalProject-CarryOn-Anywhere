package com.lion.FinalProject_CarryOn_Anywhere

import android.app.Application
import androidx.compose.material3.DrawerState
import androidx.navigation.NavHostController
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CarryOnApplication : Application(){
    // 네비게이션
    lateinit var navHostController: NavHostController
    // NavigationDrawer를 제어하기 위한 변수
    lateinit var navigationDrawerState : DrawerState
}