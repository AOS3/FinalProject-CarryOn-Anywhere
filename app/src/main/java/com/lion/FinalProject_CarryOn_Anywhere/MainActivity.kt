package com.lion.FinalProject_CarryOn_Anywhere

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.lion.FinalProject_CarryOn_Anywhere.ui.screen.home.HomeScreenPreView
import com.lion.FinalProject_CarryOn_Anywhere.ui.screen.home.MainScreenPreview
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.FinalProject_CarryOn_AnywhereTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)

        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())

        setContent {
            FinalProject_CarryOn_AnywhereTheme {
                MainScreenPreview()
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FinalProject_CarryOn_AnywhereTheme {
        Greeting("Android")
    }
}