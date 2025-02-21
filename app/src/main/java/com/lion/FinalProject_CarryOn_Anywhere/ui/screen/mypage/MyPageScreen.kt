package com.lion.FinalProject_CarryOn_Anywhere.ui.screen.mypage

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bumptech.glide.Glide
import com.lion.FinalProject_CarryOn_Anywhere.R
import com.lion.FinalProject_CarryOn_Anywhere.component.*
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.ScreenName
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.nanumSquareBold
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.nanumSquareRegular
import com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.UserSettingViewModel
import com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.mypage.MyPageViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Composable
fun MyPageScreen(
    navController: NavController,
    myPageViewModel: MyPageViewModel = hiltViewModel()) {


    val showLogoutDialog = remember { mutableStateOf(false) } // 로그아웃 다이얼로그 상태

    // LaunchedEffect로 프로필 이미지를 초기 로드
    LaunchedEffect(Unit) {
        myPageViewModel.loadProfileImage() // Glide로 프로필 이미지 로드
    }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        LikeLionTopAppBar(
            title = "마이 페이지",
            backColor = Color.White,
            navigationIconImage = null, // 네비게이션 아이콘 없음
            scrollValue = 0,
            navigationIconOnClick = {},
            menuItems = {},
            isTitleRightAligned = false,
            textOnClick = {}
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp) // ✅ 20dp 패딩 추가
                .padding(top = 10.dp)
        ) {
            // 프로필 정보
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 5.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp), // 전체 높이 설정
                    contentAlignment = Alignment.Center
                ) {
                    // 이미지 요소
                    // 첨부 이미지가 없는 경우
                    if (myPageViewModel.showImage1State.value) {
                        LikeLionProfileImg(
                            iconTint = Color.Gray,
                            profileBack = Color.LightGray,
                            profileSize = 130.dp
                        )
                    }

                    // 서버로부터 받은 이미지 표시
                    if (myPageViewModel.showImage2State.value) {
                        val bitmapState = remember { mutableStateOf<Bitmap?>(null) }

                        // Glide로 이미지를 비동기 로드
                        LaunchedEffect(myPageViewModel.imageUriState.value) {
                            myPageViewModel.imageUriState.value?.let { uri ->
                                val bitmap = withContext(Dispatchers.IO) {
                                    Glide.with(context)
                                        .asBitmap()
                                        .load(uri)
                                        .submit()
                                        .get()
                                }
                                bitmapState.value = bitmap
                            }
                        }

                        LikeLionImage(
                            bitmap = bitmapState.value, // 로드된 Bitmap 전달
                            painter = painterResource(R.drawable.ic_empty_person_24), // 기본 이미지
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(130.dp), // 이미지 크기
                            isCircular = true // 원형 이미지,

                        )
                    }

                    // 카메라나 앨범에서 사진 데이터를 가져온 경우
                    if (myPageViewModel.showImage3State.value) {
                        LikeLionImageBitmap(
                            imageBitmap = myPageViewModel.imageBitmapState.value!!.asImageBitmap(),
                            modifier = Modifier
                                .size(130.dp)
                                .clip(CircleShape)
                                .border(0.dp, Color.Transparent, CircleShape),
                            contentScale = ContentScale.Crop,
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 회원 이름 프로필 이미지 아래 중앙에 배치
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = myPageViewModel.textFieldNameValue.value + "님",
                    textAlign = TextAlign.Center,
                    fontSize = 22.sp,
                    color = Color.Black,
                    fontFamily = nanumSquareBold
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            // 메뉴 리스트
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                SectionTitle("내 정보 설정")
                MenuItem("계정 설정") { myPageViewModel.ClickEditMyInfo() }
                MenuItem("내가 쓴 글") { myPageViewModel.ClickMyPosts() }
                MenuItem("내 일정") { myPageViewModel.ClickMyUserTripList()  }
                MenuItem("로그아웃") { showLogoutDialog.value = true }

                Spacer(modifier = Modifier.height(20.dp))

                SectionTitle("서비스 약관")
                MenuItem("서비스 이용 약관") { myPageViewModel.ClickDocument() }
                MenuItem("개인정보 처리방침") { myPageViewModel.ClickDocument2() }
            }
        }
    }

    // 로그아웃 다이얼로그
    if (showLogoutDialog.value) {
        LikeLionAlertDialog(
            showDialogState = showLogoutDialog,
            title = "로그아웃",
            text = "로그아웃 하시겠습니까?",
            confirmButtonTitle = "로그아웃하기",
            dismissButtonTitle = "취소",
            confirmButtonOnClick = {
                showLogoutDialog.value = false
                myPageViewModel.logoutOnClick(context)
            }
        )
    }
}

// ✅ 섹션 타이틀
@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 21.sp,
        color = Color.Gray,
        modifier = Modifier.padding(vertical = 8.dp),
        fontFamily = nanumSquareRegular,

    )
}

@Composable
fun MenuItem(text: String, onClick: () -> Unit) {
    Text(
        text = text,
        fontSize = 17.sp,
        color = Color.Black,
        fontFamily = nanumSquareRegular,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .clickable { onClick() }
    )
}

