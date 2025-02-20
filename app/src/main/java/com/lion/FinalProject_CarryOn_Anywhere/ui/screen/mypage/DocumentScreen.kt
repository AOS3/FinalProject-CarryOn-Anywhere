package com.lion.FinalProject_CarryOn_Anywhere.ui.screen.mypage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.lion.FinalProject_CarryOn_Anywhere.R
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentScreen(navController: NavController) {
    Scaffold(
        topBar = {
            LikeLionTopAppBar(
                title = "서비스 이용 약관",
                backColor = Color.White,
                navigationIconImage = Icons.AutoMirrored.Filled.ArrowBack,
                navigationIconOnClick = { navController.popBackStack() }
            )
        }
    ) { paddingValues ->
        // ✅ LazyColumn으로 스크롤 가능하게 구현
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(0.dp), // 이미지 간 간격
            contentPadding = PaddingValues(0.dp) // 양쪽 여백 추가
        ) {
            items(imageList) { imageRes ->
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = "서비스 이용 약관 페이지",
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

// ✅ 표시할 이미지 리스트
private val imageList = listOf(
    R.drawable.document_1,
    R.drawable.document_2,
    R.drawable.document_3,
    R.drawable.document_4,
    R.drawable.document_5,
    R.drawable.document_6,

)

@Preview(showBackground = true)
@Composable
fun PreviewTermsImageScreen() {
    val navController = rememberNavController()
    DocumentScreen(navController = navController)
}