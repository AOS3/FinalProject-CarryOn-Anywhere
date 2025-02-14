package com.lion.FinalProject_CarryOn_Anywhere.ui.screen.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionFilledButton
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionTopAppBar
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.MainColor
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.Typography
import com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.login.CompletedFindIViewModel

@Composable
fun CompletedFindIdScreen(completedFindIViewModel: CompletedFindIViewModel = hiltViewModel()) {

    val userId = "carryon123"

    Scaffold(
        topBar = {
            LikeLionTopAppBar(
                backColor = Color.Transparent,
                title = "아이디 찾기",
                navigationIconImage = Icons.AutoMirrored.Filled.ArrowBack,
                navigationIconOnClick = {
                    completedFindIViewModel.navigationIconOnClick()
                },
            )
        },

    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(it)
                .padding(horizontal = 10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = buildAnnotatedString {
                    append("귀하의 아이디는 ")
                    withStyle(style = SpanStyle(color = MainColor)) {
                        append(userId)
                    }
                    append(" 입니다.")
                },
                color = Color.Black,
                modifier = Modifier
                    .padding(top = 20.dp),
                style = Typography.titleLarge,
            )

            LikeLionFilledButton(
                text = "로그인 하러가기",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 10.dp,
                        end = 10.dp
                    ),
                paddingTop = 20.dp,
                onClick = {
                    completedFindIViewModel.buttonCompleteFindIdLoginOnClick()
                },
                cornerRadius = 5,
                containerColor = MainColor,
                buttonHeight = 60.dp,
            )
        }
    }
}