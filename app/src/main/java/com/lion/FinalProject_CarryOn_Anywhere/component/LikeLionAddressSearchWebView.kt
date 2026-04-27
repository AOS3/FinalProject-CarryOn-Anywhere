package com.lion.FinalProject_CarryOn_Anywhere.component

import android.app.Dialog
import android.content.Context
import android.os.Message
import android.util.Log
import android.view.ViewGroup
import android.webkit.ConsoleMessage
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.webkit.WebViewAssetLoader

@Composable
fun LikeLionAddressSearchWebView(
    context: Context,
    onAddressSelected: (String) -> Unit,
    onClosed: () -> Unit,
) {
    AndroidView(
        modifier = Modifier.height(1700.dp),
        factory = { context ->
            // WebViewAssetLoader 설정
            val assetLoader = WebViewAssetLoader.Builder()
                .setDomain("example.com") // 임의의 도메인 설정
                .addPathHandler("/assets/", WebViewAssetLoader.AssetsPathHandler(context))
                .build()

            WebView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, // 🔹 가로는 부모 기준
                    1700
                )
                // WebView 설정
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                settings.useWideViewPort = true
                settings.loadWithOverviewMode = true
                settings.javaScriptCanOpenWindowsAutomatically = true
                settings.setSupportMultipleWindows(true)

                // WebViewClient 설정 (AssetLoader 연동)
                webViewClient = object : WebViewClient() {
                    override fun shouldInterceptRequest(view: WebView?, request: WebResourceRequest?): WebResourceResponse? {
                        return request?.url?.let { assetLoader.shouldInterceptRequest(it) }
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        Log.d("test100", "Page loaded: $url")
                    }

                    override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?
                    ) {
                        Log.e("test100", "Error loading page: ${error?.description}")
                    }
                }

                // Dialog 상태를 관리하기 위한 변수
                var popupDialog: Dialog? = null

                // WebChromeClient 설정
                webChromeClient = object : WebChromeClient() {
                    override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
                        //Log.d("LikeLionWebViewConsole", consoleMessage?.message() ?: "No message")
                        return super.onConsoleMessage(consoleMessage)
                    }

                    override fun onCreateWindow(view: WebView?, isDialog: Boolean, isUserGesture: Boolean,
                                                resultMsg: Message?): Boolean {
                        // 새 WebView 생성
                        val newWebView = WebView(context).apply {
                            settings.javaScriptEnabled = true
                            settings.domStorageEnabled = true
                        }

                        // 팝업 WebView를 Dialog로 표시
                        popupDialog = Dialog(context).apply { setContentView(newWebView)
                            window?.setLayout(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                1700
                            )
                            setOnDismissListener {
                                popupDialog = null
                                popupDialog?.dismiss()
                                onClosed()
                            }
                            show()
                        }

                        // 팝업 WebView의 ChromeClient 설정
                        newWebView.webChromeClient = object : WebChromeClient() {
                            override fun onCloseWindow(window: WebView?) {
                                popupDialog?.dismiss()
                                popupDialog = null
                                onClosed()
                            }
                        }

                        (resultMsg?.obj as? WebView.WebViewTransport)?.webView = newWebView
                        resultMsg?.sendToTarget()
                        return true
                    }
                }

                // JavaScript 인터페이스 추가
                addJavascriptInterface(
                    object {
                        @JavascriptInterface
                        fun onAddressSelected(address: String) {
                            //Log.d("LikeLionWebView", "Address selected: $address")
                            // 주소 선택 시 콜백 호출
                            onAddressSelected(address)
                            // 팝업 Dialog 닫기
                            popupDialog?.dismiss()
                        }

                        @JavascriptInterface
                        fun onClosePopup() {
                            Log.d("test100", "주소 팝업 닫힘 요청")
                            // JavaScript에서 닫기 요청 시 팝업 닫기
                            onClosed()
                            popupDialog?.dismiss()
                        }
                    },
                    "Android"
                )

                // 로컬 HTML 파일 로드 (https://example.com/assets/address_search.html)
                loadUrl("https://example.com/assets/address_search.html")
            }
        },
    )
}
