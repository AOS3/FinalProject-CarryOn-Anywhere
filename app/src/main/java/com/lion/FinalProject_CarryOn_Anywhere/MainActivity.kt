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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.ScreenName
import com.lion.FinalProject_CarryOn_Anywhere.ui.screen.home.MainScreen
import com.lion.FinalProject_CarryOn_Anywhere.ui.screen.home.PlaceInfoScreen
import com.lion.FinalProject_CarryOn_Anywhere.ui.screen.home.PlaceSearchScreen
import com.lion.FinalProject_CarryOn_Anywhere.ui.screen.login.ChangePwScreen
import com.lion.FinalProject_CarryOn_Anywhere.ui.screen.login.CompletedFindIdScreen
import com.lion.FinalProject_CarryOn_Anywhere.ui.screen.login.FindIdScreen
import com.lion.FinalProject_CarryOn_Anywhere.ui.screen.login.FindPwScreen
import com.lion.FinalProject_CarryOn_Anywhere.ui.screen.login.LoginScreen
import com.lion.FinalProject_CarryOn_Anywhere.ui.screen.login.UserJoinScreen
import com.lion.FinalProject_CarryOn_Anywhere.ui.screen.trip.AddTripPlanScreen
import com.lion.FinalProject_CarryOn_Anywhere.ui.screen.trip.EditPlanPlaceScreen
import com.lion.FinalProject_CarryOn_Anywhere.ui.screen.trip.SelectTripDateScreen
import com.lion.FinalProject_CarryOn_Anywhere.ui.screen.trip.SelectTripRegionScreen
import com.lion.FinalProject_CarryOn_Anywhere.ui.screen.trip.ShowTripMapScreen
import com.lion.FinalProject_CarryOn_Anywhere.ui.screen.trip.TripSearchPlaceScreen
import com.lion.FinalProject_CarryOn_Anywhere.ui.screen.trip.WriteRequestPlaceScreen
import com.lion.FinalProject_CarryOn_Anywhere.ui.screen.review.ReviewScreen
import com.lion.FinalProject_CarryOn_Anywhere.ui.screen.social.CommentScreen
import com.lion.FinalProject_CarryOn_Anywhere.ui.screen.social.ModifyScreen
import com.lion.FinalProject_CarryOn_Anywhere.ui.screen.social.PostScreen
import com.lion.FinalProject_CarryOn_Anywhere.ui.screen.social.ReviewDetailScreen
import com.lion.FinalProject_CarryOn_Anywhere.ui.screen.social.SharingScreen
import com.lion.FinalProject_CarryOn_Anywhere.ui.screen.social.SocialScreen
import com.lion.FinalProject_CarryOn_Anywhere.ui.screen.social.StoryDetailScreen
import com.lion.FinalProject_CarryOn_Anywhere.ui.screen.social.StoryScreen
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.FinalProject_CarryOn_AnywhereTheme
import com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.trip.TripInfoViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val windowInsetsController =
            WindowCompat.getInsetsController(window, window.decorView)

        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())

        enableEdgeToEdge()

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

    val tripInfoViewModel : TripInfoViewModel = hiltViewModel()

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
        ) {
            LoginScreen(windowInsetsController)
        }
        // 회원 가입 화면
        composable(
            route = ScreenName.USER_JOIN_SCREEN.name
        ) {
            UserJoinScreen()
        }
        // 아이디 찾기 화면
        composable(
            route = ScreenName.FIND_ID_SCREEN.name
        ) {
            FindIdScreen()
        }
        // 아이디 찾기 완료 화면
        composable(
            route = ScreenName.COMPLETED_FIND_ID_SCREEN.name
        ) {
            CompletedFindIdScreen()
        }
        // 비밀번호 찾기 화면
        composable(
            route = ScreenName.FIND_PW_SCREEN.name
        ) {
            FindPwScreen()
        }
        // 비밀번호 변경 화면
        composable(
            route = ScreenName.CHANGE_PW_SCREEN.name
        ) {
            ChangePwScreen()
        }
        // 메인 화면
        composable(
            route = ScreenName.MAIN_SCREEN.name
        ) {
            MainScreen(windowInsetsController)
        }
        // 검색 화면
        composable(
            route = ScreenName.PLACE_SEARCH_SCREEN.name
        ) {
            PlaceSearchScreen()
        }
        // 검색 상세 화면
        composable(
            route = ScreenName.PLACE_INFO_SCREEN.name
        ) {
            PlaceInfoScreen()
        }

        // 지역 선택 화면
        composable(
            route = ScreenName.SELECT_TRIP_REGION.name
        ) {
            SelectTripRegionScreen(tripInfoViewModel)
        }

        // 날짜 선택 화면
        composable(
            route = ScreenName.SELECT_TRIP_DATE.name
        ) {
            SelectTripDateScreen(tripInfoViewModel)
        }

        // 지도 출력 화면
        composable(
            route = ScreenName.SHOW_TRIP_MAP.name
        ) {
            ShowTripMapScreen(tripInfoViewModel)
        }

        // 여행 장소 검색 화면
        composable(
            route = "${ScreenName.TRIP_SEARCH_PLACE.name}/{selectedDay}"
        ) {
            val selectedDay = it.arguments?.getString("selectedDay")!!
            TripSearchPlaceScreen(tripInfoViewModel, selectedDay)
        }

        // 장소 등록 요청 화면
        composable(
            route = ScreenName.WRITE_REQUEST_PLACE.name
        ) {
            WriteRequestPlaceScreen()
        }

        // 일정 편집 화면
        composable(
            route = "${ScreenName.EDIT_PLAN_PLACE.name}/{selectedDay}/{selectedIndex}"
        ) {
            val selectedDay = it.arguments?.getString("selectedDay")!!
            val selectedIndex = it.arguments?.getString("selectedIndex")?.toIntOrNull() ?: 0
            EditPlanPlaceScreen(tripInfoViewModel, selectedDay, selectedIndex)
        }

        // 일정 생성 화면
        composable(
            route = ScreenName.ADD_TRIP_PLAN.name
        ) {
            AddTripPlanScreen(tripInfoViewModel)
        }
        composable(
            route = ScreenName.SOCIAL_SCREEN.name
        ){
            SocialScreen(
                navController = navHostController,
                onAddClick = {
                    navHostController.navigate(ScreenName.POST_SCREEN.name)
                }
            )
        }
        composable(
            route = ScreenName.REVIEW_SCREEN.name
        ){
            ReviewScreen(
                navController = navHostController
            )
        }
        composable(
            route = ScreenName.STORY_SCREEN.name
        ){
            StoryScreen(
                navController = navHostController
            )
        }
        composable(
            route = ScreenName.POST_SCREEN.name
        ){
            PostScreen(
                navController = navHostController,
                onAddClick = {
                    navHostController.navigate(ScreenName.REVIEW_SCREEN.name)
                }
            )
        }
        // ReviewDetailScreen
        composable("reviewDetail/{reviewIndex}") { backStackEntry ->
            val reviewIndex = backStackEntry.arguments?.getString("reviewIndex")?.toIntOrNull() ?: 0
            ReviewDetailScreen(
                reviewIndex = reviewIndex,
                navController = navHostController,
                onAddClick = {
                    navHostController.navigate(ScreenName.REVIEW_SCREEN.name)
                }
            )
        }
        // StoryDetailScreen
        composable("storyDetail/{storyIndex}") { backStackEntry ->
            val reviewIndex = backStackEntry.arguments?.getString("storyIndex")?.toIntOrNull() ?: 0
            StoryDetailScreen(
                reviewIndex = reviewIndex,
                navController = navHostController,
                onAddClick = {
                    navHostController.navigate(ScreenName.STORY_SCREEN.name)
                }
            )
        }
        composable(
            route = ScreenName.SHARE_SCREEN.name
        ){
            SharingScreen(
                navController = navHostController,
            )
        }
        composable("modifyScreen/{reviewIndex}") { backStackEntry ->
            val reviewIndex = backStackEntry.arguments?.getString("reviewIndex")?.toIntOrNull()
            ModifyScreen(
                navController = navHostController,
                onAddClick = {
                    navHostController.navigate(ScreenName.REVIEW_SCREEN.name)
                },
                reviewIndex = reviewIndex
            )
        }
        composable(
            route = ScreenName.COMMENT_SCREEN.name
        ){
            CommentScreen(
                navController = navHostController,
            )
        }

    }
}