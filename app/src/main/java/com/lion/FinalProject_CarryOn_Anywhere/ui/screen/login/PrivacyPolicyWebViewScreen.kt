package com.lion.FinalProject_CarryOn_Anywhere.ui.screen.login

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionFilledButton
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.MainColor

@Composable
fun PrivacyPolicyWebViewScreen(
    url: String = "https://sites.google.com/view/carryon-privacypolicy/",
    onBackPressed: () -> Unit
) {
    val context = LocalContext.current
    val webView = remember { WebView(context) }

    Scaffold(
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.surface,
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                LikeLionFilledButton(
                    text = "확인",
                    modifier = Modifier
                        .fillMaxWidth(),
                    cornerRadius = 5,
                    containerColor = MainColor,
                    buttonHeight = 50.dp,
                    onClick = onBackPressed
                )
            }

        }
    ) { innerPadding ->
        AndroidView(
            factory = {
                webView.apply {
                    webViewClient = WebViewClient()
                    settings.javaScriptEnabled = true
                    loadUrl(url)
                }
            },
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        )
    }

}