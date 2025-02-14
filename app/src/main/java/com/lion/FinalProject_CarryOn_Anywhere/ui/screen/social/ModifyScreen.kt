package com.lion.FinalProject_CarryOn_Anywhere.ui.screen.social

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
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
import com.lion.FinalProject_CarryOn_Anywhere.R
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionAlertDialog
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionDivider
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionEmptyPhoto
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionFilledButton
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionFilterChip
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionOutlinedTextField
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionOutlinedTextFieldEndIconMode
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionTopAppBar
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.ScreenName
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.SubColor
import com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.ReviewViewModel
import com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.StoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModifyScreen(
    navController: NavController,
    reviewIndex: Int?,
    storyViewModel: StoryViewModel = hiltViewModel(),
    reviewViewModel: ReviewViewModel = hiltViewModel(),
    onAddClick: () -> Unit
) {
    // 기존 데이터 불러오기 (여행 이야기 or 여행 후기)
    val posts by storyViewModel.posts.collectAsState()
    val reviews by reviewViewModel.reviews.collectAsState()

    val modify = reviewIndex?.let { posts.getOrNull(it) } ?: reviewIndex?.let { reviews.getOrNull(it) }

    val postItems = listOf("여행 후기", "여행 이야기")
    val chipItems = listOf("전체", "맛집", "숙소", "여행 일정", "모임")

    val scrollState = rememberScrollState()
    val selectedPostChip = remember { mutableStateOf(postItems[0]) }
    val selectedChip = remember { mutableStateOf(chipItems[0]) }

    // 다이얼로그 상태 변수 (초기값: false)
    val showDialogBackState = remember { mutableStateOf(false) }
    val showDialogCompleteState = remember { mutableStateOf(false) }

    val imageUris = remember { mutableStateListOf<Uri>() }
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.clipData?.let { clipData ->
                val selectedImages = mutableListOf<Uri>()
                val totalSelected = clipData.itemCount // 사용자가 선택한 전체 개수

                for (i in 0 until clipData.itemCount) {
                    if (selectedImages.size + imageUris.size < 10) { // 최대 10개까지만 추가
                        selectedImages.add(clipData.getItemAt(i).uri)
                    }
                }

                imageUris.addAll(selectedImages)

                // 사용자가 11개 이상 선택한 경우 토스트 메시지 표시
                if (totalSelected > 10) {
                    Toast.makeText(context, "한번에 최대 10개의 사진 업로드가 가능합니다.", Toast.LENGTH_SHORT).show()
                }
            } ?: result.data?.data?.let { uri ->
                if (imageUris.size < 10) {
                    imageUris.add(uri)
                }
            }
        }
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White) // 배경색을 흰색으로 설정
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

        // 다이얼로그 표시
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
            confirmButtonModifier = Modifier.width(140.dp),
            dismissButtonModifier = Modifier.width(140.dp)
        )

        // 다이얼로그 표시
        LikeLionAlertDialog(
            showDialogState = showDialogCompleteState,
            title = "글을 수정하겠습니까?",
            text = "수정된 글은 마이 페이지 및 게시판에서\n" +
                    "확인 가능합니다.",
            confirmButtonTitle = "게시",
            confirmButtonOnClick = {
                showDialogCompleteState.value = false
                navController.popBackStack()
            },
            dismissButtonTitle = "취소",
            dismissButtonOnClick = {
                showDialogCompleteState.value = false
            },
            titleAlign = TextAlign.Center, // 제목 중앙 정렬
            textAlign = TextAlign.Center, // 본문 텍스트 중앙 정렬
            titleModifier = Modifier.fillMaxWidth(), // 제목 가로 중앙 정렬
            textModifier = Modifier.fillMaxWidth(), // 본문 가로 중앙 정렬
            confirmButtonModifier = Modifier.width(140.dp),
            dismissButtonModifier = Modifier.width(140.dp)
        )

        // LazyColumn을 사용하여 세로 스크롤 가능하게 변경
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(bottom = 20.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(5.dp))

                // 여행 후기 / 여행 이야기 선택하는 Row
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
                                selectedPostChip.value = text
                            },
                            onDeleteButtonClicked = null
                        )
                    }
                }

                // 여행 후기를 선택하면 숨기고, 여행 이야기를 선택하면 보이게 처리
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
                                    selectedChip.value = text
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

                // 기본 텍스트 입력 필드 (제목)
                val textState = remember { mutableStateOf("") }
                LikeLionOutlinedTextField(
                    textFieldValue = textState,
                    label = "제목",
                    placeHolder = "제목을 입력하세요",
                    maxLength = 30,
                    showCharCount = true,
                    onValueChange = { textState.value = it },
                    singleLine = true,
                    trailingIconMode = LikeLionOutlinedTextFieldEndIconMode.TEXT,
                    onTrailingIconClick = { textState.value = "" },
                    modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 10.dp)
                )

                LikeLionDivider(
                    modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 10.dp),
                    color = Color.LightGray,
                    thickness = 1.dp
                )

                Spacer(modifier = Modifier.height(10.dp))


                // 추가한 사진
                if (imageUris.isNotEmpty()) {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 10.dp), // ✅ 좌우 여백 20.dp
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(imageUris.size) { index ->
                            Box(
                                modifier = Modifier
                                    .size(90.dp) //
                                    .padding(top = 4.dp, end = 4.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(80.dp) // ✅ 원래 이미지 크기 유지
                                        .clip(RoundedCornerShape(10.dp))
                                ) {
                                    Image(
                                        painter = rememberAsyncImagePainter(imageUris[index]),
                                        contentDescription = "Uploaded Image",
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                }

                                // X 버튼
                                IconButton(
                                    onClick = { imageUris.removeAt(index) },
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

                LikeLionFilledButton(
                    text = "사진 추가",
                    cornerRadius = 10,
                    modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 7.dp),
                    onClick = {
                        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
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

                // 여러 줄 입력 필드 (내용 입력)
                val contentState = remember { mutableStateOf("") }
                LikeLionOutlinedTextField(
                    textFieldValue = contentState,
                    label = "내용",
                    placeHolder = "내용을 입력하세요",
                    maxLength = 500,
                    showCharCount = true,
                    onValueChange = { contentState.value = it },
                    modifier = Modifier
                        .padding(start = 20.dp, end = 20.dp, top = 10.dp)
                        .height(300.dp),
                    singleLine = false,
                    trailingIconMode = LikeLionOutlinedTextFieldEndIconMode.TEXT,
                    onTrailingIconClick = { contentState.value = "" }
                )

                // 여행 후기를 선택하면 숨기고, 여행 이야기를 선택하면 보이게 처리
                if (selectedPostChip.value == "여행 후기") {
                    LikeLionDivider(
                        modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 10.dp),
                        color = Color.LightGray,
                        thickness = 1.dp
                    )

                    LikeLionFilledButton(
                        text = "일정 공유",
                        cornerRadius = 10,
                        modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 10.dp),
                        onClick = {
                            navController.navigate(ScreenName.SHARE_SCREEN.name)
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ModifyScreenPreview() {
    ModifyScreen(
        navController = NavController(LocalContext.current),
        onAddClick = {},
        reviewIndex = 0
    )
}