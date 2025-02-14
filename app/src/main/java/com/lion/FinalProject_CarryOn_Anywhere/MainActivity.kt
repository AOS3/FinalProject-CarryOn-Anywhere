package com.lion.FinalProject_CarryOn_Anywhere

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lion.FinalProject_CarryOn_Anywhere.ui.screen.home.MainScreen
import com.lion.FinalProject_CarryOn_Anywhere.ui.screen.home.PlaceInfoScreen
import com.lion.FinalProject_CarryOn_Anywhere.ui.screen.home.PlaceSearchScreen
import com.lion.FinalProject_CarryOn_Anywhere.ui.screen.login.ChangePwScreen
import com.lion.FinalProject_CarryOn_Anywhere.ui.screen.login.CompletedFindIdScreen
import com.lion.FinalProject_CarryOn_Anywhere.ui.screen.login.FindIdScreen
import com.lion.FinalProject_CarryOn_Anywhere.ui.screen.login.FindPwScreen
import com.lion.FinalProject_CarryOn_Anywhere.ui.screen.login.LoginScreen
import com.lion.FinalProject_CarryOn_Anywhere.ui.screen.login.UserJoinScreen
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.FinalProject_CarryOn_AnywhereTheme
import com.lion.FinalProject_CarryOn_Anywhere.util.ScreenName
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val windowInsetsController =
            WindowCompat.getInsetsController(window, window.decorView)

        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())

        enableEdgeToEdge()
        setContent {
            FinalProject_CarryOn_AnywhereTheme {
                CarryOnMain(windowInsetsController)
            }
        }
    }
}

@Composable
fun CarryOnMain(windowInsetsController: WindowInsetsControllerCompat) {
    // 네비게이션 객체
    val navHostController = rememberNavController()
    // Application 객체에 담는다.
    val carryOnApplication = LocalContext.current.applicationContext as CarryOnApplication
    carryOnApplication.navHostController = navHostController

    // 네비게이션 처리
    NavHost(
        navController = navHostController,
        startDestination = ScreenName.LOGIN_SCREEN.name,
        enterTransition = {
            fadeIn(
                tween(300)
            ) +
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Start,
                        tween(300)
                    )
        },
        popExitTransition = {
            fadeOut(
                tween(300)
            ) +
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.End,
                        tween(300)
                    )
        },
        exitTransition = {
            fadeOut(
                tween(300)
            )
        },
        popEnterTransition = {
            fadeIn(
                tween(300)
            )
        },
    ) {
        // 로그인 화면
        composable(
            route = ScreenName.LOGIN_SCREEN.name
        ){
            LoginScreen(windowInsetsController)
        }
        // 회원 가입 화면
        composable(
            route = ScreenName.USER_JOIN_SCREEN.name
        ){
            UserJoinScreen()
        }
        // 아이디 찾기 화면
        composable(
            route = ScreenName.FIND_ID_SCREEN.name
        ){
            FindIdScreen()
        }
        // 아이디 찾기 완료 화면
        composable(
            route = ScreenName.COMPLETED_FIND_ID_SCREEN.name
        ){
            CompletedFindIdScreen()
        }
        // 비밀번호 찾기 화면
        composable(
            route = ScreenName.FIND_PW_SCREEN.name
        ){
            FindPwScreen()
        }
        // 비밀번호 변경 화면
        composable(
            route = ScreenName.CHANGE_PW_SCREEN.name
        ){
            ChangePwScreen()
        }
        // 메인 화면
        composable(
            route = ScreenName.MAIN_SCREEN.name
        ){
            MainScreen(windowInsetsController)
        }
        // 검색 화면
        composable(
            route = ScreenName.PLACE_SEARCH_SCREEN.name
        ){
            PlaceSearchScreen()
        }
        // 검색 상세 화면
        composable(
            route = ScreenName.PLACE_INFO_SCREEN.name
        ){
            PlaceInfoScreen()
        }
    }
}