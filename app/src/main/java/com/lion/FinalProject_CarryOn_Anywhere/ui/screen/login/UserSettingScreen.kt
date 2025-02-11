package com.lion.finalprojectshoppingmallservice3team.customer.ui.screen

import android.graphics.Bitmap
import android.net.Uri
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.Glide
import com.lion.finalprojectshoppingmallservice3team.Component.LikeLionAddressSearchWebView
import com.lion.finalprojectshoppingmallservice3team.Component.LikeLionAlertDialog
import com.lion.finalprojectshoppingmallservice3team.Component.LikeLionFilledButton
import com.lion.finalprojectshoppingmallservice3team.Component.LikeLionIconButton
import com.lion.finalprojectshoppingmallservice3team.Component.LikeLionImage
import com.lion.finalprojectshoppingmallservice3team.Component.LikeLionImageBitmap
import com.lion.finalprojectshoppingmallservice3team.Component.LikeLionOutlinedTextField
import com.lion.finalprojectshoppingmallservice3team.Component.LikeLionOutlinedTextFieldEndIconMode
import com.lion.finalprojectshoppingmallservice3team.Component.LikeLionOutlinedTextFieldInputType
import com.lion.finalprojectshoppingmallservice3team.Component.LikeLionProfileImg
import com.lion.finalprojectshoppingmallservice3team.Component.LikeLionRadioGroup
import com.lion.finalprojectshoppingmallservice3team.Component.LikeLionTopAppBar
import com.lion.finalprojectshoppingmallservice3team.R
import com.lion.finalprojectshoppingmallservice3team.customer.data.util.Tools
import com.lion.finalprojectshoppingmallservice3team.customer.ui.viewmodel.mypage.UserSettingViewModel
import com.lion.finalprojectshoppingmallservice3team.ui.theme.MainColor
import com.lion.finalprojectshoppingmallservice3team.ui.theme.SubColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserSettingScreen(userSettingViewModel: UserSettingViewModel = hiltViewModel()) {

    val context = LocalContext.current
    // 촬영된 사진의 uri를 담을 객체
    lateinit var contentUri: Uri

    val coroutineScope = rememberCoroutineScope()

    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.Hidden, // 초기 상태는 Hidden
            skipHiddenState = false // Hidden 상태를 허용
        )
    )

    // 사진 촬영용 런처
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
        if (it) {
            Tools.takePictureData(context, contentUri, userSettingViewModel.imageBitmapState)
            userSettingViewModel.showImage1State.value = false
            userSettingViewModel.showImage2State.value = false
            userSettingViewModel.showImage3State.value = true
        }
    }

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

    // LaunchedEffect로 프로필 이미지를 초기 로드
    LaunchedEffect(Unit) {
        userSettingViewModel.loadProfileImage() // Glide로 프로필 이미지 로드
    }

    // BottomSheetScaffold로 구성
    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .padding(WindowInsets.navigationBars.asPaddingValues())
            ) {
                Text(
                    text = "사진 선택",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )

                // 사진 보관함
                Text(
                    text = "사진 보관함",

                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            coroutineScope.launch { scaffoldState.bottomSheetState.hide() }
                            albumLauncher.launch(
                                androidx.activity.result.PickVisualMediaRequest(
                                    ActivityResultContracts.PickVisualMedia.ImageOnly
                                )
                            )
                        }
                        .padding(vertical = 8.dp)
                )

                // 사진 찍기
                Text(
                    text = "사진 찍기",

                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            coroutineScope.launch { scaffoldState.bottomSheetState.hide() }
                            val uri = Tools.gettingPictureUri(context)
                            contentUri = uri
                            cameraLauncher.launch(uri)
                        }
                        .padding(vertical = 8.dp)
                )

                // 사진 찍기
                Text(
                    text = "사진 삭제",

                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            coroutineScope.launch { scaffoldState.bottomSheetState.hide() }
                            userSettingViewModel.deleteImageOnClick()
                        }
                        .padding(vertical = 8.dp)
                )
            }
        },
        sheetPeekHeight = 56.dp, // 기본 peek 높이 조정
        modifier = Modifier.padding(WindowInsets.navigationBars.asPaddingValues()) // 전체 스캐폴드에도 패딩 추가
    ) {
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
                    .padding(horizontal = 10.dp)
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
                            .height(140.dp), // 전체 높이 설정
                        contentAlignment = Alignment.Center
                    ) {
                        // 이미지 요소
                        // 첨부 이미지가 없는 경우
                        if (userSettingViewModel.showImage1State.value) {
                            LikeLionImage(
                                painter = painterResource(R.drawable.account_circle_24px),
                                contentScale = ContentScale.Crop,
                                isCircular = true,
                                modifier = Modifier.size(140.dp) // 이미지 크기 설정
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
                                painter = painterResource(R.drawable.account_circle_24px), // 기본 이미지
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.size(140.dp), // 이미지 크기
                                isCircular = true // 원형 이미지
                            )
                        }
                        // 카메라나 앨범에서 사진 데이터를 가져온 경우
                        if (userSettingViewModel.showImage3State.value) {
                            LikeLionImageBitmap(
                                imageBitmap = userSettingViewModel.imageBitmapState.value!!.asImageBitmap(),
                                modifier = Modifier
                                    .size(140.dp)
                                    .clip(CircleShape)
                                    .border(0.dp, Color.Transparent, CircleShape),
                                contentScale = ContentScale.Crop,
                            )
                        }

                        // 버튼 (이미지 아래로 위치)
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.align(Alignment.BottomCenter) // 버튼을 아래로 정렬
                        ) {
                            LikeLionFilledButton(
                                text = "등록",
                                modifier = Modifier
                                    .width(80.dp)
                                    .height(35.dp),
                                onClick = {
                                    coroutineScope.launch {
                                        scaffoldState.bottomSheetState.expand() // 바텀시트를 표시
                                    }
                                }
                            )
                        }
                    }
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
                )

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
                            onClick = {

                            }
                        )
                    }

                    Row(
                        modifier = Modifier.padding(end = 10.dp)
                    ) {
                        LikeLionImage(
                            painter = painterResource(id = R.drawable.naver_logo),
                            modifier = Modifier.size(40.dp),
                            isCircular = true,
                            onClick = {

                            }
                        )
                    }

                    LikeLionImage(
                        painter = painterResource(id = R.drawable.google_logo),
                        modifier = Modifier.size(40.dp),
                        contentScale = ContentScale.Inside,
                        borderWidth = 1.dp, // 테두리 두께
                        borderColor = Color.LightGray, // 테두리 색상
                        isCircular = true,
                        onClick = {

                        }
                    )
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
                    containerColor = SubColor,
                    contentColor = MainColor,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    onClick = {
                        userSettingViewModel.modifyPwOnClick()
                    }
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth() // Row가 가로 전체를 채우도록 설정
                        .padding(bottom = 5.dp),
                ) {

                    LikeLionOutlinedTextField(
                        textFieldValue = userSettingViewModel.textFieldModifyNicknameValue,
                        label = "닉네임",
                        placeHolder = "닉네임을 입력해 주세요.",
                        modifier = Modifier.weight(1f),
                        inputCondition = "[^a-zA-Z0-9ㄱ-ㅎㅏ-ㅣ가-힣]",
                        trailingIconMode = LikeLionOutlinedTextFieldEndIconMode.TEXT,
                        onTrailingIconClick = {
                            userSettingViewModel.updateNicknameConditions()
                        },
                        singleLine = true,
                        onValueChange = {
                            userSettingViewModel.updateNicknameConditions()
                        }
                    )

                    // 중복확인 버튼
                    LikeLionFilledButton(
                        text = "중복확인",
                        isEnabled = userSettingViewModel.isButtonNicknameEnabled.value,
                        modifier = Modifier
                            .weight(0.4f)
                            .padding(top = 6.dp, start = 5.dp)
                            .height(56.dp),
                        onClick = {
                            userSettingViewModel.buttonCheckNickNameOnClick()
                        }
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(end = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            tint = when {
                                userSettingViewModel.textFieldModifyNicknameValue.value.isBlank() -> Color.LightGray
                                userSettingViewModel.isLengthValid.value -> Color(0xFF0DB00C) // 조건 충족
                                else -> Color(0xFFB00E0E)
                            },
                            contentDescription = "Check",
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "2~10자",
                            color = when {
                                userSettingViewModel.textFieldModifyNicknameValue.value.isBlank() -> Color.LightGray
                                userSettingViewModel.isLengthValid.value -> Color(0xFF0DB00C)
                                else -> Color(0xFFB00E0E)
                            },
                            fontSize = 14.sp
                        )
                    }

                    Row(
                        modifier = Modifier.padding(end = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            tint = when {
                                userSettingViewModel.textFieldModifyNicknameValue.value.isBlank() -> Color.LightGray
                                userSettingViewModel.isSpecialCharInvalid.value -> Color(0xFF0DB00C)
                                else -> Color(0xFFB00E0E)
                            },
                            contentDescription = "Check",
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "특수문자 불가",
                            color = when {
                                userSettingViewModel.textFieldModifyNicknameValue.value.isBlank() -> Color.LightGray
                                userSettingViewModel.isSpecialCharInvalid.value -> Color(0xFF0DB00C)
                                else -> Color(0xFFB00E0E)
                            },
                            fontSize = 14.sp
                        )
                    }

                    Row(
                        modifier = Modifier.padding(end = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            tint = when {
                                userSettingViewModel.textFieldModifyNicknameValue.value.isBlank() -> Color.LightGray
                                userSettingViewModel.isConsonantVowelValid.value -> Color(0xFF0DB00C)
                                else -> Color(0xFFB00E0E)
                            },
                            contentDescription = "Check",
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "자음 모음 단독 사용 불가",
                            color = when {
                                userSettingViewModel.textFieldModifyNicknameValue.value.isBlank() -> Color.LightGray
                                userSettingViewModel.isConsonantVowelValid.value -> Color(0xFF0DB00C)
                                else -> Color(0xFFB00E0E)
                            },
                            fontSize = 14.sp
                        )
                    }
                }

                LikeLionOutlinedTextField(
                    textFieldValue = userSettingViewModel.textFieldModifyNameValue,
                    label = "이름",
                    placeHolder = "이름을 입력해 주세요.",
                    modifier = Modifier.fillMaxWidth(),
                    inputCondition = "[^ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z]",
                    trailingIconMode = LikeLionOutlinedTextFieldEndIconMode.TEXT,
                    inputType = LikeLionOutlinedTextFieldInputType.TEXT,
                    singleLine = true,
                )


                LikeLionOutlinedTextField(
                    textFieldValue = userSettingViewModel.textFieldModifyPhoneValue,
                    paddingTop = 20.dp,
                    label = "연락처",
                    modifier = Modifier.fillMaxWidth(),
                    placeHolder = "하이픈(-) 없이 숫자만 입력해 주세요.",
                    trailingIconMode = LikeLionOutlinedTextFieldEndIconMode.TEXT,
                    inputCondition = "[^0-9]",
                    singleLine = true,
                    inputType = LikeLionOutlinedTextFieldInputType.NUMBER,
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp, bottom = 20.dp),
                ) {

                    LikeLionOutlinedTextField(
                        textFieldValue = userSettingViewModel.textFieldModifyAddressValue,
                        label = "기본 배송지",
                        placeHolder = "주소를 검색해주세요.",
                        modifier = Modifier.weight(1f),
                        readOnly = true,
                    )

                    // 주소검색 버튼
                    LikeLionFilledButton(
                        text = "주소검색",
                        modifier = Modifier
                            .weight(0.4f)
                            .padding(top = 6.dp, start = 5.dp)
                            .height(56.dp),
                        onClick = {
                            userSettingViewModel.showAddressSearch.value = true
                        }
                    )
                }

                if (userSettingViewModel.showAddressSearch.value) {
                    LikeLionAddressSearchWebView(
                        context = LocalContext.current,
                        onAddressSelected = { address ->
                            userSettingViewModel.textFieldModifyAddressValue.value = address
                        }
                    )
                    userSettingViewModel.showAddressSearch.value = false
                }

                LikeLionOutlinedTextField(
                    textFieldValue = userSettingViewModel.textFieldModifyDetailAddressValue,
                    label = "상세 주소",
                    placeHolder = "상세 주소를 입력해 주세요.",
                    modifier = Modifier.fillMaxWidth(),
                    inputCondition = "[^0-9ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z]",
                    trailingIconMode = LikeLionOutlinedTextFieldEndIconMode.TEXT,
                    inputType = LikeLionOutlinedTextFieldInputType.TEXT,
                    singleLine = true,
                )

                LikeLionOutlinedTextField(
                    textFieldValue = userSettingViewModel.textFieldModifyBirthValue,
                    paddingTop = 20.dp,
                    label = "생년월일",
                    modifier = Modifier.fillMaxWidth(),
                    placeHolder = "생년월일을 입력해 주세요.",
                    trailingIconMode = LikeLionOutlinedTextFieldEndIconMode.TEXT,
                    inputCondition = "[^0-9]",
                    singleLine = true,
                    inputType = LikeLionOutlinedTextFieldInputType.NUMBER,
                )

                Text(
                    text = "성별",
                    color = Color.Black,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp, bottom = 10.dp),
                )

                LikeLionRadioGroup(
                    options = listOf("남자", "여자", "상관없음"),
                    fontSize = 14.sp,
                    textModifier = Modifier.padding(5.dp),
                    modifier = Modifier.padding(bottom = 10.dp),
                    selectedOption = userSettingViewModel.selectedGender.value,
                    onOptionSelected = { gender ->
                        userSettingViewModel.selectedGender.value = gender
                    },
                    orientation = Orientation.Horizontal, // 가로 방향
                    itemSpacing = 10,
                )

                Text(
                    text = "문자 정보 수신 여부",
                    color = Color.Black,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp),
                )

                LikeLionRadioGroup(
                    options = listOf("동의", "미동의"),
                    fontSize = 14.sp,
                    textModifier = Modifier.padding(5.dp),
                    modifier = Modifier.padding(bottom = 10.dp),
                    selectedOption = userSettingViewModel.selectedSmsAgree.value,
                    onOptionSelected = { selectedOption ->
                        userSettingViewModel.selectedSmsAgree.value = selectedOption
                    },
                    orientation = Orientation.Horizontal, // 가로 방향
                    itemSpacing = 10,
                )

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
                    selectedOption = userSettingViewModel.selectedPushAgree.value,
                    onOptionSelected = { gender ->
                        userSettingViewModel.selectedPushAgree.value = gender
                    },
                    orientation = Orientation.Horizontal, // 가로 방향
                    itemSpacing = 10,
                )


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
                        userSettingViewModel.saveSettingButtonOnClick()
                    }
                )

                // 회원 탈퇴 시 띄우는 다이얼로그
                LikeLionAlertDialog(
                    showDialogState = userSettingViewModel.showDialogWithdrawalState,
                    confirmButtonTitle = "확인",
                    confirmbuttonModifier = Modifier
                        .weight(1f)
                        .padding(start = 5.dp),
                    confirmButtonOnClick = {
                        userSettingViewModel.withdrawalOnClick()
                    },
                    dismissButtonTitle = "취소",
                    dismissbuttonModifier = Modifier
                        .weight(1f)
                        .padding(end = 5.dp),
                    dismissBorder = BorderStroke(1.dp, Color.LightGray),
                    dismissButtonOnClick = {
                        userSettingViewModel.showDialogWithdrawalState.value = false
                    },
                    title = "마크샵을 탈퇴하시나요?",
                    titleModifier = Modifier.fillMaxWidth(),
                    titleAlign = TextAlign.Center,
                    text = "탈퇴시 회원정보가 삭제됩니다.",
                    textModifier = Modifier.fillMaxWidth(),
                )

                LikeLionAlertDialog(
                    showDialogState = userSettingViewModel.showDialogBackArrowState,
                    title = "수정 내용 미반영",
                    titleModifier = Modifier.fillMaxWidth(),
                    titleAlign = TextAlign.Center,
                    text = "페이지 이탈하면 수정한 정보가 \n" +
                            "반영되지 않습니다.\n" +
                            "마크샵으로 돌아가시겠습니까?",
                    textModifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    confirmButtonTitle = "바로가기",
                    confirmButtonOnClick = {
                        userSettingViewModel.dialogConfirmOnClick()
                    },
                    confirmbuttonModifier = Modifier
                        .weight(1f)
                        .padding(start = 5.dp),
                    dismissButtonTitle = "취소",
                    dismissButtonOnClick = {
                        userSettingViewModel.dialogDismissOnClick()
                    },
                    dismissbuttonModifier = Modifier
                        .weight(1f)
                        .padding(end = 5.dp),
                    dismissBorder = BorderStroke(1.dp, Color.LightGray),
                )

                // 닉네임 중복 확인을 하지 않은 경우
                LikeLionAlertDialog(
                    showDialogState = userSettingViewModel.showDialogNickNameIsNotCheckState,
                    confirmButtonTitle = "확인",
                    confirmButtonOnClick = {
                        userSettingViewModel.showDialogNickNameIsNotCheckState.value = false
                    },
                    title = "닉네임 중복 확인 오류",
                    text = "닉네임 중복 확인을 해주세요"
                )
            }
        }
    }

}