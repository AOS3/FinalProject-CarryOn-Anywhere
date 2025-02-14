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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.ScreenName
import com.lion.FinalProject_CarryOn_Anywhere.ui.screen.home.MainScreen
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

    // 네비게이션 처리
    NavHost(
        navController = navHostController,
        startDestination = ScreenName.SOCIAL_SCREEN.name,
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
        // 메인 화면
        composable(
            route = ScreenName.MAIN_SCREEN.name
        ){
            MainScreen()
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