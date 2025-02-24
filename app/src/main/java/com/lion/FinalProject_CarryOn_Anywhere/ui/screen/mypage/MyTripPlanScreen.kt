package com.lion.FinalProject_CarryOn_Anywhere.ui.screen.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Group
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionMyTripPlanItem
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionTopAppBar
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionAlertDialog
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionCodeInputDialog
import com.lion.FinalProject_CarryOn_Anywhere.data.server.model.TripModel
import com.lion.FinalProject_CarryOn_Anywhere.data.server.model.myposts.TripPlanModel
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.ScreenName
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.SubColor
import com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.mypage.MyTripPlanViewModel
import dagger.hilt.android.lifecycle.HiltViewModel

// 마이페이지 -> 내 일정
// 툴바에 일정 추가하기 / 친구 일정 공유하기 기능
// 일정 삭제 가능

@Composable
fun MyTripPlanScreen(
    navController: NavController,
    myTripPlanViewModel: MyTripPlanViewModel = hiltViewModel()
) {
    val isLoading by myTripPlanViewModel.isLoading

    LaunchedEffect(Unit) {
        myTripPlanViewModel.gettingTripData()
    }

    val showDialog = remember { mutableStateOf(false) }
    val selectedPlan = remember { mutableStateOf<TripModel?>(null) }
    val showCodeDialog = remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize().background(color = Color.White)) {
            LikeLionTopAppBar(
                title = "내 일정",
                navigationIconImage = Icons.Default.ArrowBack,
                navigationIconOnClick = { navController.popBackStack() },
                menuItems = {
                    IconButton(onClick = { showCodeDialog.value = true }) {
                        Icon(imageVector = Icons.Default.Group, contentDescription = "친구 공유")
                    }
                    IconButton(onClick = { myTripPlanViewModel.addPlanOnClick() }) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "일정 추가")
                    }
                }
            )

            if (myTripPlanViewModel.contentListState.isEmpty() && !isLoading) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "등록된 일정이 없습니다.",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(bottom = 10.dp),
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "일정을 등록해주세요.",
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                ) {
                    items(myTripPlanViewModel.contentListState) { plan ->
                        LikeLionMyTripPlanItem(
                            onclick = {
                                myTripPlanViewModel.listItemOnClick(plan.tripDocumentId)
                            },
                            plan = plan,
                            onDeleteClick = {
                                selectedPlan.value = plan
                                showDialog.value = true
                            }
                        )
                    }
                }
            }
        }

        // ✅ 로딩 인디케이터 추가
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }

    // ✅ 삭제 다이얼로그
    if (showDialog.value && selectedPlan.value != null) {
        LikeLionAlertDialog(
            showDialogState = showDialog,
            title = "일정을 삭제하시겠어요?",
            text = "삭제 후 복구할 수 없습니다.",
            confirmButtonTitle = "삭제",
            confirmButtonOnClick = {
                selectedPlan.value?.let { plan ->
                    myTripPlanViewModel.deletePlanOnClick(plan.tripDocumentId)
                }
                showDialog.value = false
            },
            dismissButtonTitle = "취소",
            dismissButtonOnClick = {
                showDialog.value = false
            }
        )
    }

    // ✅ 일정 코드 입력 다이얼로그
    LikeLionCodeInputDialog(
        showDialog = showCodeDialog,
        onConfirm = { code ->
            println("입력된 일정 코드: $code")
        },
        onDismiss = { }
    )
}

// ✅ 미리보기 추가
@Preview(showBackground = true)
@Composable
fun PreviewMyTripPlanScreen() {
    val navController = rememberNavController() // ✅ 미리보기용 NavController 생성
    MyTripPlanScreen(navController = navController)
}
