package com.lion.FinalProject_CarryOn_Anywhere.ui.screen.mypage

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bumptech.glide.Glide
import com.lion.FinalProject_CarryOn_Anywhere.R
import com.lion.FinalProject_CarryOn_Anywhere.component.*
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.AppPushState
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.Tools
import com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.UserSettingViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditMyInfoScreen(
    navController: NavController,
    userSettingViewModel: UserSettingViewModel = hiltViewModel()) {

    val context = LocalContext.current
    // 촬영된 사진의 uri를 담을 객체
    lateinit var contentUri: Uri

    val coroutineScope = rememberCoroutineScope()


    // LaunchedEffect로 프로필 이미지를 초기 로드
    LaunchedEffect(Unit) {
        userSettingViewModel.loadProfileImage() // Glide로 프로필 이미지 로드
        userSettingViewModel.checkKakaoToken()
    }

    // ViewModel의 상태 값 관찰
    val isKakaoLinked by userSettingViewModel.isKakaoLinkedState

    // ✅ 바텀시트 상태 추가
    var showBottomSheet by remember { mutableStateOf(false) }

    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.Hidden, // 초기 상태는 Hidden
            skipHiddenState = false // Hidden 상태를 허용
        )
    )

    // 앨범용 런처
    val albumLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) {
            Tools.takeAlbumData(context, it, userSettingViewModel.imageBitmapState)
            if (it != null) {
                userSettingViewModel.showImage1State.value = false
                userSettingViewModel.showImage2State.value = false
                userSettingViewModel.showImage3State.value = true
            }
        }


    Scaffold(
        topBar = {
            LikeLionTopAppBar(
                backColor = Color.White,
                navigationIconImage = Icons.AutoMirrored.Filled.ArrowBack,
                navigationIconOnClick = {
                    userSettingViewModel.navigationIconOnClick()
                },
                title = "계정 설정",
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(it)
                .imePadding()
                .padding(horizontal = 20.dp)
                .verticalScroll(state = rememberScrollState())
        ) {
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
                    if (userSettingViewModel.showImage1State.value) {
                        LikeLionProfileImg(
                            iconTint = Color.Gray,
                            profileBack = Color.LightGray,
                            profileSize = 130.dp
                        )
                    }
                    // 서버로부터 받은 이미지 표시
                    if (userSettingViewModel.showImage2State.value) {
                        val bitmapState = remember { mutableStateOf<Bitmap?>(null) }

                        // Glide로 이미지를 비동기 로드
                        LaunchedEffect(userSettingViewModel.imageUriState.value) {
                            userSettingViewModel.imageUriState.value?.let { uri ->
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
                    if (userSettingViewModel.showImage3State.value) {
                        LikeLionImageBitmap(
                            imageBitmap = userSettingViewModel.imageBitmapState.value!!.asImageBitmap(),
                            modifier = Modifier
                                .size(130.dp)
                                .clip(CircleShape)
                                .border(0.dp, Color.Transparent, CircleShape),
                            contentScale = ContentScale.Crop,
                        )
                    }


                }
            }

            // 버튼을 프로필 이미지 **아래 중앙에 배치**
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                LikeLionFilledButton(
                    text = "등록",
                    horizontalPadding = 10.dp,
                    modifier = Modifier
                        .width(120.dp) // 버튼 크기 적절히 조정
                        .height(35.dp), // 높이 조정
                    onClick = {
                        coroutineScope.launch {
                            showBottomSheet = true
                        }
                    },
                    cornerRadius = 5
                )
            }


            Text(
                text = "5MB 이내의 이미지 파일을 업로드 해주세요.\n" +
                        "(이미지 형식 : JPG, JPEG, PNG)",
                color = Color.Gray,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp)
                    .padding(top = 10.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))


            Text(
                text = "SNS계정 연결",
                color = Color.Black,
                fontSize = 14.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            )

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp)
            ) {
                Row(
                    modifier = Modifier.padding(end = 10.dp)
                ) {

                    LikeLionImage(
                        painter = painterResource(id = R.drawable.kakao_login_logo),
                        modifier = Modifier.size(40.dp),
                        isCircular = true,
                        onClick = { },
                        isGrayscale = !isKakaoLinked // 카카오 계정이 연동되지 않은 경우 흑백 처리
                    )

                }
            }

            Text(
                text = "비밀번호",
                color = Color.Black,
                fontSize = 14.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            )

            LikeLionFilledButton(
                text = "비밀번호 변경",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                onClick = {
                    userSettingViewModel.modifyPwOnClick()
                },
                cornerRadius = 5
            )

            LikeLionOutlinedTextField(
                textFieldValue = userSettingViewModel.textFieldModifyIdValue,
                label = "아이디",
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                singleLine = true
            )

            LikeLionOutlinedTextField(
                textFieldValue = userSettingViewModel.textFieldModifyNameValue,
                label = "이름",
                placeHolder = "이름을 입력해 주세요.",
                modifier = Modifier.fillMaxWidth(),
                inputCondition = "[^ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z]",
                trailingIconMode = LikeLionOutlinedTextFieldEndIconMode.TEXT,
                inputType = LikeLionOutlinedTextFieldInputType.TEXT,
                singleLine = true,
                onValueChange = {
                    userSettingViewModel.textFieldModifyNameValue.value = it
                    userSettingViewModel.validateName() // 이름 유효성 검사 수행
                }
            )

            LikeLionOutlinedTextField(
                textFieldValue = userSettingViewModel.textFieldModifyPhoneValue,
                label = "연락처",
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                singleLine = true
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "앱 푸쉬 수신 동의",
                color = Color.Black,
                fontSize = 14.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
            )

            LikeLionRadioGroup(
                options = listOf("동의", "미동의"),
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 10.dp),
                textModifier = Modifier.padding(5.dp),
                selectedOption = userSettingViewModel.selectedPushAgree.value.str,
                onOptionSelected = { selectedOption ->
                    userSettingViewModel.selectedPushAgree.value = AppPushState.values().find { it.str == selectedOption }!!
                },
                orientation = Orientation.Horizontal, // 가로 방향
                itemSpacing = 10,
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "회원 탈퇴",
                color = Color.Gray,
                fontSize = 14.sp,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp)
                    .clickable {
                        userSettingViewModel.showDialogWithdrawalState.value = true
                    }
            )

            LikeLionFilledButton(
                text = "저장하기",
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    userSettingViewModel.validateName()
                    if (userSettingViewModel.isNameValid.value) {
                        userSettingViewModel.saveSettingButtonOnClick()
                    } else {
                        userSettingViewModel.showNameErrorDialog.value = true // 이름이 비어 있으면 다이얼로그 표시
                    }
                },
                cornerRadius = 5
            )



            // ✅ 바텀시트 (사진 추가 / 삭제)
            if (showBottomSheet) {
                LikeLionBottomSheet(
                    onDismissRequest = { showBottomSheet = false },
                    text1 = "사진 보관함",
                    text1OnClick = {
                        showBottomSheet = false
                        albumLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    text2 = "사진 삭제",
                    text2OnClick = {
                        showBottomSheet = false
                        userSettingViewModel.deleteImageOnClick()
                    }
                )
            }

            // ✅ 회원 탈퇴 다이얼로그
            if (userSettingViewModel.showDialogWithdrawalState.value) {
                LikeLionAlertDialog(
                    showDialogState = userSettingViewModel.showDialogWithdrawalState,
                    title = "회원탈퇴",
                    titleModifier = Modifier.fillMaxWidth(),
                    titleAlign = TextAlign.Center,
                    text = "회원탈퇴 시 회원 정보 및 서비스 이용기록이 모두 삭제되며, 삭제한 데이터는 복구가 불가능합니다.",
                    textModifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    confirmButtonTitle = "확인",
                    confirmButtonOnClick = {
                        userSettingViewModel.showDialogWithdrawalState.value = false
                        // 회원 탈퇴 로직(토큰삭제 + 화면 이동)
                        userSettingViewModel.withdrawalOnClick(context)
                    },
                    confirmButtonModifier = Modifier.padding(horizontal = 5.dp),
                    dismissButtonTitle = "취소",
                    dismissButtonOnClick = { userSettingViewModel.showDialogPwOk.value = false },
                    dismissButtonModifier = Modifier.padding(horizontal = 5.dp),
                    dismissBorder = BorderStroke(1.dp, Color.LightGray),
                )
            }


            // 이름 입력 여부에 대한 유효성 검사
            if (userSettingViewModel.showNameErrorDialog.value) {
                LikeLionAlertDialog(
                    showDialogState = userSettingViewModel.showNameErrorDialog,
                    title = "입력 오류",
                    text = "이름을 입력해 주세요.",
                    confirmButtonTitle = "확인",
                    confirmButtonOnClick = {
                        userSettingViewModel.showNameErrorDialog.value = false
                    }
                )
            }



        }
    }
}





