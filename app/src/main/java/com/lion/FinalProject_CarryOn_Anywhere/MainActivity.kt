package com.lion.FinalProject_CarryOn_Anywhere

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionBottomNavItems
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionBottomNavigation
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
import com.lion.FinalProject_CarryOn_Anywhere.ui.screen.mylike.MyLikeScreen
import com.lion.FinalProject_CarryOn_Anywhere.ui.screen.mypage.EditPwScreen
import com.lion.FinalProject_CarryOn_Anywhere.ui.screen.mypage.MyPageScreen
import com.lion.FinalProject_CarryOn_Anywhere.ui.screen.mypage.MyPostsScreen
import com.lion.FinalProject_CarryOn_Anywhere.ui.screen.mypage.MyTripPlanScreen
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
import com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.home.PlaceSearchViewModel
import com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.trip.TripInfoViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //enableEdgeToEdge()
        val windowInsetsController =
            WindowCompat.getInsetsController(window, window.decorView)

        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())

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

    // BottomNavigation 표시 화면 목록
    val bottomNaviScreens = listOf(
        // 메인 화면
        ScreenName.MAIN_SCREEN.name,
        // 마이페이지 화면
        ScreenName.MY_PAGE.name,
        // 찜 화면
        ScreenName.MY_LIKE.name,
        // 캐리톡 화면
        ScreenName.STORY_SCREEN.name,
    )

    // 현재 네비게이션 상태 확인
    val currentBackStackEntry = navHostController.currentBackStackEntryAsState().value
    val currentRoute = currentBackStackEntry?.destination?.route
    //val isLoggedIn by carryOnApplication.isLoggedIn.collectAsState()

    Scaffold(
        bottomBar = {
            // 로그인 상태와 Bottom Navigation이 필요한 화면인지 확인
            if (currentRoute in bottomNaviScreens) {
                LikeLionBottomNavigation(
                    navController = navHostController,
                    items = LikeLionBottomNavItems()
                )
            }
        }
    ) { paddingValues ->
        // 네비게이션 처리
        NavHost(
            navController = navHostController,
            modifier = Modifier
                .padding(paddingValues),
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
                MainScreen()
            }
            // 검색 화면
            composable(
                route = ScreenName.PLACE_SEARCH_SCREEN.name
            ) {
                PlaceSearchScreen(navController = navHostController)
            }
            // 검색 상세 화면
            composable(
                route = "${ScreenName.PLACE_INFO_SCREEN.name}/{title}",
            ) { backStackEntry ->
                val title = backStackEntry.arguments?.getString("title") ?: ""
                val placeSearchViewModel: PlaceSearchViewModel = hiltViewModel()

                PlaceInfoScreen(
                    navController = navHostController,
                    title = title,
                    placeSearchViewModel = placeSearchViewModel
                )
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
            ) {
                SocialScreen(
                    navController = navHostController,
                    onAddClick = {
                        navHostController.navigate(ScreenName.POST_SCREEN.name)
                    }
                )
            }
            composable(
                route = ScreenName.REVIEW_SCREEN.name
            ) {
                ReviewScreen(
                    navController = navHostController
                )
            }
            composable(
                route = ScreenName.STORY_SCREEN.name
            ) {
                StoryScreen(
                    navController = navHostController
                )
            }
            composable(
                route = ScreenName.POST_SCREEN.name
            ) {
                PostScreen(
                    navController = navHostController,
                    onAddClick = {
                        navHostController.navigate(ScreenName.REVIEW_SCREEN.name)
                    }
                )
            }
            // ReviewDetailScreen
            composable("reviewDetail/{reviewIndex}") { backStackEntry ->
                val reviewIndex =
                    backStackEntry.arguments?.getString("reviewIndex")?.toIntOrNull() ?: 0
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
                val reviewIndex =
                    backStackEntry.arguments?.getString("storyIndex")?.toIntOrNull() ?: 0
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
            ) {
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
            ) {
                CommentScreen(
                    navController = navHostController,
                )
            }
            // 마이페이지 화면
            composable(
                route = ScreenName.MY_PAGE.name
            ) {
                MyPageScreen(navHostController)
            }

            // 계정 설정 화면
            composable(
                route = ScreenName.EDIT_MY_INFO.name
            ) {
                // EditMyInfoSreen()
            }

            // 비밀번호 변경 화면
            composable(
                route = ScreenName.EDIT_PW.name
            ) {
                EditPwScreen()
            }


            // 나의 글 화면
            composable(
                route = ScreenName.MY_POSTS.name
            ) {
                MyPostsScreen(navHostController)
            }

            // 내 일정 화면
            composable(
                route = ScreenName.MY_TRIP_PLAN.name
            ) {
                MyTripPlanScreen(navHostController)
            }

            // 나의 찜 화면
            composable(
                route = ScreenName.MY_LIKE.name
            ) {
                MyLikeScreen(navHostController)
            }

        }
    }
}
