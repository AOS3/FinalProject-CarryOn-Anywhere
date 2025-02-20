//package com.lion.FinalProject_CarryOn_Anywhere
//
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.unit.dp
//import androidx.hilt.navigation.compose.hiltViewModel
//
//@Composable
//fun StartScreen(startViewModel: StartViewModel = hiltViewModel()) {
//
//    startViewModel.autoLoginProcess()
//
//    if(startViewModel.showLoginMessageState.value) {
//        Scaffold {
//            Column (
//                modifier = Modifier.fillMaxSize().padding(it).padding(start = 10.dp, end = 10.dp),
//                verticalArrangement = Arrangement.Center,
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Image(
//                    painter = painterResource(id = R.drawable.carryonlogo),
//                    contentDescription = null
//                )
//            }
//        }
//    }
//
//}