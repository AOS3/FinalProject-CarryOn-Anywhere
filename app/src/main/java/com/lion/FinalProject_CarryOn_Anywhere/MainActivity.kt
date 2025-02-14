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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.ScreenName
import com.lion.FinalProject_CarryOn_Anywhere.ui.screen.trip.AddTripPlanScreen
import com.lion.FinalProject_CarryOn_Anywhere.ui.screen.trip.EditPlanPlaceScreen
import com.lion.FinalProject_CarryOn_Anywhere.ui.screen.trip.SelectTripDateScreen
import com.lion.FinalProject_CarryOn_Anywhere.ui.screen.trip.SelectTripRegionScreen
import com.lion.FinalProject_CarryOn_Anywhere.ui.screen.trip.ShowTripMapScreen
import com.lion.FinalProject_CarryOn_Anywhere.ui.screen.trip.TripSearchPlaceScreen
import com.lion.FinalProject_CarryOn_Anywhere.ui.screen.trip.WriteRequestPlaceScreen
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.FinalProject_CarryOn_AnywhereTheme
import com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.trip.TripInfoViewModel

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            FinalProject_CarryOn_AnywhereTheme {
                BoardMain()
            }
        }
    }
}

@Composable
fun BoardMain() {
    // 네비게이션 객체
    val navHostController = rememberNavController()
    // Application 객체에 담는다.
    val carryOnApplication = LocalContext.current.applicationContext as CarryOnApplication
    carryOnApplication.navHostController = navHostController

    val tripInfoViewModel = hiltViewModel<TripInfoViewModel>()

    // 네비게이션 처리
    NavHost(
        navController = navHostController,
        startDestination = ScreenName.SELECT_TRIP_REGION.name,
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
        // 지역 선택 화면
        composable(
            route = ScreenName.SELECT_TRIP_REGION.name
        ){
            SelectTripRegionScreen(tripInfoViewModel)
        }

        // 날짜 선택 화면
        composable(
            route = ScreenName.SELECT_TRIP_DATE.name
        ){
            SelectTripDateScreen(tripInfoViewModel)
        }

        // 지도 출력 화면
        composable(
            route = ScreenName.SHOW_TRIP_MAP.name
        ){
            ShowTripMapScreen(tripInfoViewModel)
        }

        // 여행 장소 검색 화면
        composable(
            route = "${ScreenName.TRIP_SEARCH_PLACE.name}/{selectedDay}"
        ){
            val selectedDay  = it.arguments?.getString("selectedDay")!!
            TripSearchPlaceScreen(tripInfoViewModel, selectedDay)
        }

        // 장소 등록 요청 화면
        composable(
            route = ScreenName.WRITE_REQUEST_PLACE.name
        ){
            WriteRequestPlaceScreen()
        }

        // 일정 편집 화면
        composable(
            route = "${ScreenName.EDIT_PLAN_PLACE.name}/{selectedDay}/{selectedIndex}"
        ){
            val selectedDay  = it.arguments?.getString("selectedDay")!!
            val selectedIndex = it.arguments?.getString("selectedIndex")?.toIntOrNull() ?: 0
            EditPlanPlaceScreen(tripInfoViewModel, selectedDay, selectedIndex)
        }

        // 일정 생성 화면
        composable(
            route = ScreenName.ADD_TRIP_PLAN.name
        ){
            AddTripPlanScreen(tripInfoViewModel)
        }
    }
}