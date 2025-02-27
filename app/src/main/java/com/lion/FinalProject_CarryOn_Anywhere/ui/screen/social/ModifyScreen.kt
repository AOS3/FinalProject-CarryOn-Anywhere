package com.lion.FinalProject_CarryOn_Anywhere.ui.screen.social

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.rememberAsyncImagePainter
import com.google.android.gms.maps.model.LatLng
import com.lion.FinalProject_CarryOn_Anywhere.R
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionAddPlaceItem
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionAlertDialog
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionDivider
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionEmptyPhoto
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionFilledButton
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionFilterChip
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionOutlinedTextField
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionOutlinedTextFieldEndIconMode
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionTopAppBar
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.ScreenName
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.TalkTag
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.GrayColor
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.SubColor
import com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.ModifyViewModel
import com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.social.ReviewViewModel
import com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.social.StoryViewModel
import com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.trip.TripInfoViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModifyScreen(
    navController: NavController,
    reviewDocumentId: String?,
    storyDocumentId: String?,
    storyViewModel: StoryViewModel = hiltViewModel(),
    reviewViewModel: ReviewViewModel = hiltViewModel(),
    modifyViewModel: ModifyViewModel = hiltViewModel(),
    tripInfoViewModel: TripInfoViewModel = hiltViewModel(),
    onAddClick: () -> Unit
) {
    // 로딩 상태 감지
    val isLoading by modifyViewModel.isLoading.collectAsState()
    val context = LocalContext.current

    // Firestore에서 가져온 기존 리뷰 또는 게시글 데이터
    val reviews by reviewViewModel.reviews.collectAsState()
    val posts by storyViewModel.posts.collectAsState()

    // 수정할 게시글 정보 저장할 변수들
    val titleState: MutableState<String>
    val contentState: MutableState<String>
    val allImages: MutableList<String>
    val postItems: List<String>
    val chipItems: List<String>
    val shareTitle = remember { mutableStateOf("") }
    val tripDate = remember { mutableStateOf("") }
    val sharePlace = remember { mutableStateListOf<String>() }
    val sharePlan = remember { mutableStateListOf<Map<String, String>>() }

    // "여행 후기" 수정할 경우 데이터 로드
    if (reviewDocumentId != null && storyDocumentId == null) {
        val review = reviews.find { it.documentId == reviewDocumentId } ?: return

        titleState = remember { mutableStateOf(review.title) }
        contentState = remember { mutableStateOf(review.content) }
        allImages = remember { mutableStateListOf(*review.imageUrls.toTypedArray()) }
        shareTitle.value = review.shareTitle
        tripDate.value = review.tripDate
        sharePlace.clear()
        sharePlace.addAll(review.sharePlace)
        sharePlan.clear()
        sharePlan.addAll(review.sharePlan)


        postItems = modifyViewModel.postItems
        chipItems = modifyViewModel.chipItems

        modifyViewModel.updateSelectedPostChip("여행 후기")

        // "여행 이야기" 수정할 경우 데이터 로드
    } else if (storyDocumentId != null && reviewDocumentId == null) {
        val post = posts.find { it.documentId == storyDocumentId } ?: return

        titleState = remember { mutableStateOf(post.title) }
        contentState = remember { mutableStateOf(post.content) }
        allImages = remember { mutableStateListOf(*post.imageUrls.toTypedArray()) }

        postItems = modifyViewModel.postItems
        chipItems = modifyViewModel.chipItems

        modifyViewModel.updateSelectedPostChip("여행 이야기") // 이야기 기본값 설정
        modifyViewModel.updateSelectedChip(post.tag)

    } else {
        return
    }

    val scrollState = rememberScrollState()
    val selectedPostChip = modifyViewModel.selectedPostChip.collectAsState()
    val selectedChip = modifyViewModel.selectedChip.collectAsState()
    val imageUris = modifyViewModel.imageUris.collectAsState()

    // 초기 선택된 카테고리를 기억하기 위한 변수
    val initialCategory = remember { selectedPostChip.value }

    // 다이얼로그 상태 변수 (초기값: false)
    val showDialogBackState = remember { mutableStateOf(false) }
    val showDialogCompleteState = remember { mutableStateOf(false) }

    // 이미지 선택을 위한 ActivityResultLauncher
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.clipData?.let { clipData ->
                    modifyViewModel.addImages(clipData, context)
                } ?: result.data?.data?.let { uri ->
                    modifyViewModel.addSingleImage(uri)
                }
            }
        }

    LaunchedEffect(Unit) {
        navController.currentBackStackEntry?.savedStateHandle?.let { savedState ->
            savedState.getLiveData<String>("selectedTitle").observeForever { title ->
                Log.d("ModifyScreen", "🔹 selectedTitle: $title")
                shareTitle.value = title
            }

            savedState.getLiveData<String>("startDateTime").observeForever { start ->
                savedState.getLiveData<String>("endDateTime").observeForever { end ->
                    Log.d("ModifyScreen", "🔹 tripDate: $start ~ $end")
                    tripDate.value = "$start ~ $end"
                }
            }

            savedState.getLiveData<List<String>>("tripCityList").observeForever { places ->
                Log.d("ModifyScreen", "🔹 tripCityList: $places")
                sharePlace.clear()
                sharePlace.addAll(places)
            }

            savedState.getLiveData<List<Map<String, String>>>("planList").observeForever { plans ->
                Log.d("ModifyScreen", "🔹 planList:")
                plans.forEachIndexed { index, plan ->
                    Log.d("ModifyScreen", "   Day${index + 1}: $plan")
                }
                sharePlan.clear()
                sharePlan.addAll(plans)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            // 상단 AppBar
            LikeLionTopAppBar(
                title = "글 수정",
                backColor = Color.White,
                navigationIconImage = Icons.Default.ArrowBack,
                navigationIconOnClick = {
                    showDialogBackState.value = true
                },
                menuItems = {
                    IconButton(onClick = { showDialogCompleteState.value = true }) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "완료",
                            tint = Color.Black
                        )
                    }
                }
            )

            // "뒤로 가기" 다이얼로그 표시
            LikeLionAlertDialog(
                showDialogState = showDialogBackState,
                title = "뒤로 나가시겠습니까?",
                text = "뒤로 가면 수정 중이던 글이 모두 사라집니다.",
                confirmButtonTitle = "뒤로 가기",
                confirmButtonOnClick = {
                    showDialogBackState.value = false
                    navController.popBackStack()
                },
                dismissButtonTitle = "취소",
                dismissButtonOnClick = {
                    showDialogBackState.value = false
                },
                titleAlign = TextAlign.Center, // 제목 중앙 정렬
                textAlign = TextAlign.Center, // 본문 텍스트 중앙 정렬
                titleModifier = Modifier.fillMaxWidth(), // 제목 가로 중앙 정렬
                textModifier = Modifier.fillMaxWidth(), // 본문 가로 중앙 정렬
                confirmButtonModifier = Modifier.width(120.dp),
                dismissButtonModifier = Modifier.width(120.dp)
            )

            // "수정" 다이얼로그 표시
            LikeLionAlertDialog(
                showDialogState = showDialogCompleteState,
                title = "글을 수정하겠습니까?",
                text = "수정된 글은 마이 페이지 및 게시판에서\n확인 가능합니다.",
                confirmButtonTitle = "수정",
                confirmButtonOnClick = {
                    val newTitle = titleState.value.trim()
                    val newContent = contentState.value.trim()

                    // Firestore에 저장된 기존 이미지 (URL만 포함)
                    val existingImageUrls =
                        allImages.filter { it.startsWith("http") }.toMutableList()

                    // 사용자가 새로 추가한 로컬 이미지 (URI 타입)
                    val newImageUris = imageUris.value

                    val isImageRequired = selectedPostChip.value == "여행 후기" // 여행 후기는 이미지 필수
                    val isTitleAndContentFilled = newTitle.isNotEmpty() && newContent.isNotEmpty()
                    val isImageFilled =
                        newImageUris.isNotEmpty() || existingImageUrls.isNotEmpty() || !isImageRequired

                    if (!isTitleAndContentFilled) {
                        Toast.makeText(context, "제목과 내용을 모두 입력해주세요!", Toast.LENGTH_SHORT).show()
                    } else if (!isImageFilled) {
                        Toast.makeText(context, "여행 후기는 최소 1개의 이미지를 첨부해야 합니다.", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        // Firestore에 수정된 데이터 저장
                        modifyViewModel.setLoading(true)

                        CoroutineScope(Dispatchers.IO).launch {
                            val uploadedImageUrls =
                                if (newImageUris.isNotEmpty()) {
                                    ModifyViewModel.ImageUploader.uploadImages(newImageUris)
                                } else {
                                    emptyList()
                                }

                            // 기존 Firestore 이미지 + 새로 업로드된 이미지 URL 합치기
                            val finalImageUrls = existingImageUrls + uploadedImageUrls

                            withContext(Dispatchers.Main) {
                                if (!isImageRequired || finalImageUrls.isNotEmpty()) {
                                    if (selectedPostChip.value == "여행 후기" && reviewDocumentId != null) {
                                        reviewViewModel.editTripReview(
                                            documentId = reviewDocumentId,
                                            newTitle = newTitle,
                                            newContent = newContent,
                                            newImageUrls = finalImageUrls,
                                            newShareTitle = shareTitle.value,
                                            newTripDate = tripDate.value,
                                            newSharePlace = sharePlace,
                                            newSharePlan = sharePlan
                                        )

                                        Toast.makeText(context, "여행 후기 수정 완료!", Toast.LENGTH_SHORT)
                                            .show()

                                    } else if (selectedPostChip.value == "여행 이야기" && storyDocumentId != null) {
                                        storyViewModel.editCarryTalk(
                                            documentId = storyDocumentId,
                                            newTag = when (modifyViewModel._selectedChip.value) {
                                                "맛집" -> TalkTag.TALK_TAG_RESTAURANT.name
                                                "숙소" -> TalkTag.TALK_TAG_ACCOMMODATION.name
                                                "여행 일정" -> TalkTag.TALK_TAG_TRIP_PLAN.name
                                                "모임" -> TalkTag.TALK_TAG_MEET.name
                                                else -> TalkTag.TALK_TAG_ALL.name
                                            },
                                            newTitle = newTitle,
                                            newContent = newContent,
                                            newImageUrls = finalImageUrls
                                        )
                                        Toast.makeText(context, "여행 이야기 수정 완료!", Toast.LENGTH_SHORT)
                                            .show()
                                    }

                                    showDialogCompleteState.value = false
                                    navController.popBackStack()
                                } else {
                                    Toast.makeText(
                                        context,
                                        "여행 후기는 최소 1개의 이미지를 첨부해야 합니다.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                                modifyViewModel.setLoading(false)
                            }
                        }

                        // UI 갱신을 위해 다이얼로그 닫기 전 약간의 지연 추가
                        CoroutineScope(Dispatchers.Main).launch {
                            kotlinx.coroutines.delay(100) // UI 업데이트 시간 확보
                            showDialogCompleteState.value = false
                        }
                    }
                },
                dismissButtonTitle = "취소",
                dismissButtonOnClick = {
                    showDialogCompleteState.value = false
                },
                titleAlign = TextAlign.Center, // 제목 중앙 정렬
                textAlign = TextAlign.Center, // 본문 텍스트 중앙 정렬
                titleModifier = Modifier.fillMaxWidth(), // 제목 가로 중앙 정렬
                textModifier = Modifier.fillMaxWidth(), // 본문 가로 중앙 정렬
                confirmButtonModifier = Modifier.width(120.dp),
                dismissButtonModifier = Modifier.width(120.dp)
            )

            // "여행 후기", "여행 이야기", "태그" 선택
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
                item {
                    Spacer(modifier = Modifier.height(5.dp))

                    // "여행 후기", "여행 이야기" 선택
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(scrollState),
                        horizontalArrangement = Arrangement.spacedBy(0.dp)
                    ) {
                        postItems.forEach { chipText ->
                            LikeLionFilterChip(
                                text = chipText,
                                selected = selectedPostChip.value == chipText,
                                selectedColor = SubColor,
                                unselectedColor = Color.White,
                                borderColor = SubColor,
                                chipTextStyle = TextStyle(
                                    color = if (selectedPostChip.value == chipText) Color.White else SubColor,
                                    textAlign = TextAlign.Center
                                ),
                                selectedTextColor = Color.White,
                                unselectedTextColor = SubColor,
                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp),
                                chipModifier = Modifier
                                    .padding(4.dp)
                                    .width(80.dp),
                                cornerRadius = 100,
                                onChipClicked = { text, _ ->
                                    if (text == initialCategory) {
                                        modifyViewModel.updateSelectedPostChip(text)
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "카테고리는 변경할 수 없습니다.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                },
                                onDeleteButtonClicked = null
                            )
                        }
                    }

                    // "여행 이야기"에서 "태그" 선택
                    if (selectedPostChip.value == "여행 이야기") {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(scrollState),
                            horizontalArrangement = Arrangement.spacedBy(0.dp)
                        ) {
                            chipItems.forEach { chipText ->
                                LikeLionFilterChip(
                                    text = chipText,
                                    selected = selectedChip.value == chipText,
                                    selectedColor = SubColor,
                                    unselectedColor = Color.White,
                                    borderColor = SubColor,
                                    chipTextStyle = TextStyle(
                                        color = if (selectedChip.value == chipText) Color.White else SubColor,
                                        textAlign = TextAlign.Center
                                    ),
                                    selectedTextColor = Color.White,
                                    unselectedTextColor = SubColor,
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp),
                                    chipModifier = Modifier
                                        .padding(4.dp)
                                        .width(60.dp),
                                    cornerRadius = 100,
                                    onChipClicked = { text, _ ->
                                        modifyViewModel.updateSelectedChip(text)
                                    },
                                    onDeleteButtonClicked = null
                                )
                            }
                        }
                    }

                    LikeLionDivider(
                        modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 10.dp),
                        color = Color.LightGray,
                        thickness = 1.dp
                    )

                    // 제목 입력 텍스트 필드
                    LikeLionOutlinedTextField(
                        textFieldValue = titleState,
                        label = "제목",
                        placeHolder = "제목을 입력하세요",
                        maxLength = 30,
                        showCharCount = true,
                        onValueChange = { modifyViewModel.updateTitle(it) },
                        singleLine = true,
                        trailingIconMode = LikeLionOutlinedTextFieldEndIconMode.TEXT,
                        onTrailingIconClick = { modifyViewModel.clearData() },
                        modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 10.dp)
                    )

                    LikeLionDivider(
                        modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 10.dp),
                        color = Color.LightGray,
                        thickness = 1.dp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // 새로 추가한 이미지 업데이트
                    LaunchedEffect(imageUris.value) {
                        // 새로 추가한 이미지 뒤쪽에 붙이기
                        allImages.addAll(imageUris.value.map { it.toString() })
                    }

                    // 기존 업로드된 사진 + 새로 추가한 사진을 한 줄로 정렬
                    if (allImages.isNotEmpty()) {
                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(allImages) { imageUrl ->
                                Box(
                                    modifier = Modifier
                                        .size(90.dp)
                                        .padding(top = 4.dp, end = 4.dp)
                                ) {
                                    Image(
                                        painter = rememberAsyncImagePainter(imageUrl),
                                        contentDescription = "Review Image",
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(RoundedCornerShape(10.dp)),
                                        contentScale = ContentScale.Crop
                                    )

                                    // X 버튼 (삭제 기능 추가)
                                    IconButton(
                                        onClick = {
                                            // UI에서 삭제
                                            allImages.remove(imageUrl)
                                        },
                                        modifier = Modifier
                                            .size(20.dp)
                                            .align(Alignment.TopEnd)
                                            .offset(x = 4.dp, y = (-8).dp)
                                    ) {
                                        Icon(
                                            painter = painterResource(R.drawable.cancel_24px),
                                            contentDescription = "Remove Image",
                                            tint = Color.Black
                                        )
                                    }
                                }
                            }
                        }
                    } else if (selectedPostChip.value == "여행 후기") {
                        LikeLionEmptyPhoto(
                            message = "최소 한 개 이상의 사진을 첨부해야 합니다.\n" +
                                    "한번에 최대 10개의 사진 업로드가 가능합니다.",
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else if (selectedPostChip.value == "여행 이야기") {
                        LikeLionEmptyPhoto(
                            message = "등록된 사진이 없습니다.\n" +
                                    "한번에 최대 10개의 사진 업로드가 가능합니다.",
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    // "사진 추가" 버튼
                    LikeLionFilledButton(
                        text = "사진 추가",
                        cornerRadius = 10,
                        modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 7.dp),
                        onClick = {
                            val intent = Intent(
                                Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                            ).apply {
                                type = "image/*"
                                putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true) // 다중 선택 허용
                            }
                            launcher.launch(intent)
                        }
                    )

                    LikeLionDivider(
                        modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 10.dp),
                        color = Color.LightGray,
                        thickness = 1.dp
                    )

                    // 내용 입력 텍스트 필드
                    LikeLionOutlinedTextField(
                        textFieldValue = contentState,
                        label = "내용",
                        placeHolder = "내용을 입력하세요",
                        maxLength = 500,
                        showCharCount = true,
                        onValueChange = { modifyViewModel.updateContent(it) },
                        modifier = Modifier
                            .padding(start = 20.dp, end = 20.dp, top = 10.dp)
                            .height(300.dp),
                        singleLine = false,
                        trailingIconMode = LikeLionOutlinedTextFieldEndIconMode.TEXT,
                        onTrailingIconClick = { modifyViewModel.clearData() }
                    )

                    // "여행 후기"를 선택하면 보이는 버튼
                    if (selectedPostChip.value == "여행 후기" && reviews.isNotEmpty()) {
                        LikeLionDivider(
                            modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 10.dp),
                            color = Color.LightGray,
                            thickness = 1.dp
                        )

                        // 일정 공유 버튼
                        LikeLionFilledButton(
                            text = "일정 공유",
                            cornerRadius = 10,
                            modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 10.dp),
                            onClick = {
                                navController.navigate(ScreenName.SHARE_SCREEN.name)
                            }
                        )

                        if (shareTitle.value.isNotEmpty() || sharePlace.isNotEmpty()) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp, vertical = 10.dp)
                            ) {
                                // 제목
                                shareTitle.value.takeIf { it.isNotEmpty() }?.let {
                                    Text(
                                        text = it,
                                        style = MaterialTheme.typography.titleLarge,
                                        modifier = Modifier.padding(bottom = 10.dp)
                                    )
                                }

                                // 일정 날짜
                                tripDate.value.takeIf { it.isNotEmpty() }?.let {
                                    Text(
                                        text = it,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = GrayColor,
                                        modifier = Modifier.padding(bottom = 10.dp)
                                    )
                                }

                                // 지역 정보
                                if (sharePlace.isNotEmpty()) {
                                    sharePlace.forEach { place ->
                                        Text(
                                            text = "📍 여행 지역: $place",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = GrayColor,
                                            modifier = Modifier.padding(bottom = 5.dp)
                                        )
                                    }
                                }
                            }

                            // "일별 일정 목록"
                            if (sharePlan.isNotEmpty()) {
                                sharePlan
                                    .groupBy { it["date"] ?: "날짜 없음" }
                                    .entries
                                    .forEachIndexed { index, (day, places) ->
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 20.dp, vertical = 10.dp)
                                        ) {
                                            // DayX 표시 + 날짜
                                            Text(
                                                text = "Day${index + 1}  $day",
                                                style = MaterialTheme.typography.bodyLarge,
                                                color = Color.Black,
                                                modifier = Modifier.padding(bottom = 10.dp)
                                            )

                                            places.forEachIndexed { placeIndex, place ->
                                                val placeName = place["place"] ?: "장소 없음"
                                                val addr1 = place["addr"] ?: "주소 정보 없음"
                                                val addr2 = place["addr2"] ?: ""

                                                val mapX =
                                                    place["mapx"]?.toString()?.toDoubleOrNull()
                                                        ?: 0.0
                                                val mapY =
                                                    place["mapy"]?.toString()?.toDoubleOrNull()
                                                        ?: 0.0

                                                val distanceToNext =
                                                    places.getOrNull(placeIndex + 1)
                                                        ?.let { nextPlace ->
                                                            val nextMapX =
                                                                nextPlace["mapx"]?.toString()
                                                                    ?.toDoubleOrNull() ?: 0.0
                                                            val nextMapY =
                                                                nextPlace["mapy"]?.toString()
                                                                    ?.toDoubleOrNull() ?: 0.0

                                                            tripInfoViewModel.calculateDistance(
                                                                LatLng(mapY, mapX),
                                                                LatLng(nextMapY, nextMapX)
                                                            )
                                                        }

                                                LikeLionAddPlaceItem(
                                                    index = placeIndex,
                                                    lastIndex = places.lastIndex,
                                                    place = mapOf(
                                                        "title" to placeName,
                                                        "addr1" to addr1,
                                                        "addr2" to addr2
                                                    ),
                                                    distanceToNext = distanceToNext
                                                )
                                            }

                                            // 구분선
                                            LikeLionDivider(
                                                modifier = Modifier.padding(vertical = 10.dp),
                                                color = Color.LightGray,
                                                thickness = 1.dp
                                            )
                                        }
                                    }
                            }
                        }
                    }
                }
            }
            // 로딩 상태 compose 호출
            if (isLoading) {
                Loading()
            }
        }
    }
}

// 로딩 상태 compose
@Composable
private fun Loading() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray.copy(alpha = 0.8f)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // 로딩 애니메이션 추가
            CircularProgressIndicator(color = Color.Black)
            Spacer(modifier = Modifier.height(10.dp))
            Text("수정한 게시글을 저장하는 중...", color = Color.Black)

        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ModifyScreenPreview() {
    ModifyScreen(
        navController = NavController(LocalContext.current),
        onAddClick = {},
        reviewDocumentId = "reviewDocumentId",
        storyDocumentId = "storyDocumentId"
    )
}