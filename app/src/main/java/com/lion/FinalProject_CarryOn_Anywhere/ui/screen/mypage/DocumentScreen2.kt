package com.lion.FinalProject_CarryOn_Anywhere.ui.screen.mypage

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
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
fun DocumentScreen2(navController: NavController) {
    Scaffold(
        topBar = {
            LikeLionTopAppBar(
                title = "개인정보 처리 방침",
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
            items(imageList2) { imageRes ->
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = "개인정보 처리 방침 페이지",
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

// ✅ 표시할 이미지 리스트
private val imageList2 = listOf(
    R.drawable.document2_1,
    R.drawable.document2_2,
    R.drawable.document2_3,
    R.drawable.document2_4,
    R.drawable.document2_5,
    R.drawable.document2_6,

    )

@Preview(showBackground = true)
@Composable
fun PreviewTermsImageScreen2() {
    val navController = rememberNavController()
    DocumentScreen2(navController = navController)
}