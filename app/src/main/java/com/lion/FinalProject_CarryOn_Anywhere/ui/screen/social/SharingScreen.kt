package com.lion.FinalProject_CarryOn_Anywhere.ui.screen.social

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.lion.FinalProject_CarryOn_Anywhere.CarryOnApplication
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionEmptyView
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionFilledButton
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionTopAppBar
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.ScreenName
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.SubColor
import com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.social.Share
import com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.social.SharingViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SharingScreen(
    sharingViewModel: SharingViewModel = hiltViewModel(),
    navController: NavController,
) {
    // 로딩 상태 감지
    val isLoading by sharingViewModel.isLoading.collectAsState()
    val shares by sharingViewModel.shares.collectAsState()

    val context = LocalContext.current

    // 현재 로그인한 사용자 정보 가져오기 (안전한 null 체크)
    val carryOnApplication = context.applicationContext as? CarryOnApplication
    val loginUserId = try {
        carryOnApplication?.loginUserModel?.userDocumentId ?: "guest"
    } catch (e: UninitializedPropertyAccessException) {
        "guest"
    }

    // `fetchUserTripReviews(loginUserId)` 호출 (loginUserId를 ViewModel로 넘김)
    LaunchedEffect(Unit) {
        sharingViewModel.fetchUserTripReviews(loginUserId)
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White) // 배경색을 흰색으로 설정
    ) {
        // 상단 AppBar
        LikeLionTopAppBar(
            title = "일정 공유",
            backColor = Color.White,
            navigationIconImage = Icons.Default.ArrowBack,
            navigationIconOnClick = { navController.popBackStack() },
            menuItems = {
            }
        )

        // Firestore에서 데이터를 가져오는 동안 로딩 표시
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = SubColor)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("데이터를 불러오는 중...", color = Color.Gray)
                }
            }
            return
        }

        if (shares.isEmpty()) {
            LikeLionEmptyView(message = "저장된 일정이 없습니다.")
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        bottom = WindowInsets.navigationBars.asPaddingValues()
                            .calculateBottomPadding()
                    ),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 20.dp)
            ) {
                items(shares.size) { index ->
                    if (navController.previousBackStackEntry?.destination?.route == ScreenName.POST_SCREEN.name) {
                        ShareItem(shares[index], navController, index)
                    } else {
                        ShareItemForModifyScreen(shares[index], navController)
                    }
                }
            }

        }
    }
}

@Composable
private fun ShareItem(share: Share, navController: NavController, index: Int) {
    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp, horizontal = 15.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 왼쪽 Column (태그, 제목, 내용, 작성자 정보)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(), // Row의 높이를 상속
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // 제목
                Text(
                    text = share.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                // 작성 날짜
                Text(
                    text = "${formattedDate(share.startDateTime)} ~ ${formattedDate(share.endDateTime)}",
                    fontSize = 12.sp,
                    color = Color.LightGray
                )
            }

            // 오른쪽 Column (선택 버튼)
            Column(
                modifier = Modifier
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // 사용자가 일정 선택 시 해당 정보를 PostScreen에 전달
                LikeLionFilledButton(
                    text = "선택",
                    cornerRadius = 100,
                    fillWidth = false,
                    modifier = Modifier.wrapContentSize(),
                    onClick = {
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set("selectedTitle", share.title)
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set("startDateTime", formattedDate(share.startDateTime))
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set("endDateTime", formattedDate(share.endDateTime))
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set("tripCityList", share.tripCityList)
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set("planList", share.planList)
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}

@Composable
private fun ShareItemForModifyScreen(share: Share, navController: NavController) {
    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp, horizontal = 15.dp)
    ) {
        Row(
            modifier = Modifier.padding(10.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                // 일정 제목
                Text(
                    text = share.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                // 일정 기간
                Text(
                    text = "${formattedDate(share.startDateTime)} ~ ${formattedDate(share.endDateTime)}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            // 선택 버튼
            LikeLionFilledButton(
                text = "선택",
                cornerRadius = 100,
                fillWidth = false,
                modifier = Modifier.wrapContentSize(),
                onClick = {
                    navController.previousBackStackEntry?.savedStateHandle?.apply {
                        set("selectedTitle", share.title)
                        set("startDateTime", formattedDate(share.startDateTime))
                        set("endDateTime", formattedDate(share.endDateTime))
                        set("tripCityList", share.tripCityList.mapNotNull { it["regionName"] as? String })
                        set("planList", share.planList.mapNotNull { it as? Map<String, String> })
                    }
                    navController.popBackStack()
                }
            )
        }
    }
}

// 날짜 변환
private fun formattedDate(timestamp: Long): String {
    val date = Date(timestamp)
    val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return format.format(date)
}

@Preview(showBackground = true)
@Composable
private fun SharingScreenPreview() {
    SharingScreen(
        navController = NavController(LocalContext.current),
    )
}