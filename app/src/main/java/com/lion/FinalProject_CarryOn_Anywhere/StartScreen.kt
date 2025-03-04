package com.lion.FinalProject_CarryOn_Anywhere

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.core.view.WindowInsetsControllerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.MainColor

@Composable
fun StartScreen(startViewModel: StartViewModel = hiltViewModel()) {

    val context = LocalContext.current
    val window = (context as? Activity)?.window
    val systemUiController = remember(window) { WindowInsetsControllerCompat(window!!, window.decorView) }

    // **Edge를 MainColor로 변경**
    DisposableEffect(Unit) {
        window?.statusBarColor = MainColor.hashCode()
        window?.navigationBarColor = MainColor.hashCode()
        systemUiController.isAppearanceLightStatusBars = false  // 상태바 아이콘 색상 조정

        onDispose {
            // **StartScreen을 벗어날 때 원래 색상으로 복구**
            window?.statusBarColor = Color.White.hashCode()
            window?.navigationBarColor = Color.White.hashCode()
            systemUiController.isAppearanceLightStatusBars = true
        }
    }

    startViewModel.autoLoginProcess()

    if(startViewModel.showLoginMessageState.value) {
        Scaffold {
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .background(MainColor),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.carryon_logo_final_nobackground),
                    contentDescription = null
                )
            }
        }
    }

}