package com.lion.FinalProject_CarryOn_Anywhere.ui.screen.social

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState
import com.lion.FinalProject_CarryOn_Anywhere.CarryOnApplication
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
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.GrayColor
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.SubColor
import com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.PostViewModel
import com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.trip.TripInfoViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostScreen(
    navController: NavController,
    postViewModel: PostViewModel = hiltViewModel(),
    tripInfoViewModel: TripInfoViewModel = hiltViewModel(),
    onAddClick: () -> Unit
) {
    val context = LocalContext.current

    // 현재 로그인한 사용자 정보 가져오기 (안전한 null 체크)
    val carryOnApplication = context.applicationContext as? CarryOnApplication
    val loginUserId = try {
        carryOnApplication?.loginUserModel?.userDocumentId ?: "guest"
    } catch (e: UninitializedPropertyAccessException) {
        "guest"
    }
    val loginUserName = try {
        carryOnApplication?.loginUserModel?.userName ?: "익명"
    } catch (e: UninitializedPropertyAccessException) {
        "익명"
    }

    // 로딩 상태 감지
    val isLoading by postViewModel.isLoading.collectAsState()

    // ViewModel에서 가져온 데이터
    val imageUris = postViewModel.imageUris.collectAsState()

    val scrollState = rememberScrollState()
    val selectedPostChip = postViewModel.selectedPostChip.collectAsState()
    val selectedChip = postViewModel.selectedChip.collectAsState()

    // 다이얼로그 상태 변수 (초기값: false)
    val showDialogBackState = remember { mutableStateOf(false) }
    val showDialogCompleteState = remember { mutableStateOf(false) }

    // 이미지 선택을 위한 ActivityResultLauncher
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.clipData?.let { clipData ->
                    postViewModel.addImages(clipData, context)
                } ?: result.data?.data?.let { uri ->
                    postViewModel.addSingleImage(uri)
                }
            }
        }

    // NavController에서 전달된 데이터를 가져와서 ViewModel에 반영
    LaunchedEffect(Unit) {
        navController.currentBackStackEntry?.savedStateHandle?.let { savedState ->
            savedState.get<String>("selectedTitle")?.let { postViewModel.updateSelectedTitle(it) }
            savedState.get<String>("startDateTime")
                ?.let { postViewModel.updateSelectedStartDate(it) }
            savedState.get<String>("endDateTime")?.let { postViewModel.updateSelectedEndDate(it) }
            savedState.get<List<Map<String, Any>>>("tripCityList")
                ?.let { postViewModel.updateTripCityList(it) }
            savedState.get<List<Map<String, Any>>>("planList")
                ?.let { postViewModel.updatePlanList(it) }
        }
    }

    // ViewModel에서 선택한 일정 정보 가져오기
    val selectedTitle by postViewModel.selectedTitle.collectAsState()
    val selectedStartDate by postViewModel.selectedStartDate.collectAsState()
    val selectedEndDate by postViewModel.selectedEndDate.collectAsState()
    val tripCityList by postViewModel.tripCityList.collectAsState()
    val dailyPlanData by postViewModel.dailyPlanData.collectAsState()

    // `postViewModel`에서 마커 데이터 가져오기
    val (selectedDayPlaces, markerTitles, markerSnippets) = postViewModel.getMarkerDataForSelectedDay(
        tripInfoViewModel.selectedDay.value
    )

    // 기본 지도 위치 설정 (서울을 기본값으로 설정)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            selectedDayPlaces.firstOrNull() ?: LatLng(37.5665, 126.9780), 12f
        )
    }

    // 지도 위치 업데이트 (마커가 있을 때만)
    LaunchedEffect(selectedDayPlaces) {
        if (selectedDayPlaces.isNotEmpty()) {
            cameraPositionState.position = CameraPosition.fromLatLngZoom(selectedDayPlaces.first(), 12f)
        }
    }

    // selectedPlaceLocation 값이 변경되면 지도 위치 업데이트
    LaunchedEffect(tripInfoViewModel.selectedPlaceLocation.value) {
        cameraPositionState.position =
            CameraPosition.fromLatLngZoom(tripInfoViewModel.selectedPlaceLocation.value, 8f)
    }

    // 여행 날짜 목록 업데이트
    LaunchedEffect(tripInfoViewModel.startDate.value, tripInfoViewModel.endDate.value) {
        tripInfoViewModel.updateFormattedDates()
        tripInfoViewModel.updateTripDays()
    }

    // 디버깅 로그 출력 (마커 정보 확인)
    Log.d("PostScreen", "마커 개수: ${selectedDayPlaces.size}")
    selectedDayPlaces.forEachIndexed { index, latLng ->
        Log.d("PostScreen", "마커 위치 $index: ${latLng.latitude}, ${latLng.longitude}")
        Log.d("PostScreen", "마커 제목: ${markerTitles.getOrNull(index)}, 주소: ${markerSnippets.getOrNull(index)}")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White) // 배경색을 흰색으로 설정
        ) {
            // 상단 AppBar
            LikeLionTopAppBar(
                title = "글 작성",
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
                text = "뒤로 가면 작성 중이던 글이 모두 사라집니다.",
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
                title = "글을 게시하겠습니까?",
                text = "게시된 글은 마이 페이지 및 게시판에서\n확인 가능합니다.",
                confirmButtonTitle = "게시",
                confirmButtonOnClick = {
                    val title = postViewModel.titleState.value.trim()
                    val content = postViewModel.contentState.value.trim()
                    val imageUrisList = imageUris.value

                    val isImageRequired = selectedPostChip.value == "여행 후기"
                    val isTitleAndContentFilled = title.isNotEmpty() && content.isNotEmpty()
                    val isImageFilled = imageUrisList.isNotEmpty() || !isImageRequired

                    if (!isTitleAndContentFilled) {
                        Toast.makeText(context, "제목과 내용을 모두 입력해주세요!", Toast.LENGTH_SHORT).show()
                    } else if (!isImageFilled) {
                        Toast.makeText(context, "여행 후기는 최소 1개의 이미지를 첨부해야 합니다.", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        // 로딩 UI를 먼저 활성화
                        postViewModel.setLoading(true)

                        CoroutineScope(Dispatchers.IO).launch {
                            val uploadedImageUrls =
                                if (imageUrisList.isNotEmpty()) {
                                    PostViewModel.ImageUploader.uploadImages(imageUrisList)
                                } else {
                                    emptyList()
                                }

                            withContext(Dispatchers.Main) {
                                if (!isImageRequired || uploadedImageUrls.isNotEmpty()) {
                                    postViewModel.savePost(
                                        title = title,
                                        content = content,
                                        userDocumentId = loginUserId,
                                        userName = loginUserName,
                                        imageUrls = uploadedImageUrls,
                                        shareTitle = selectedTitle,
                                        shareDate = "${selectedStartDate} ~ ${selectedEndDate}",
                                        sharePlace = tripCityList.map {
                                            (it["regionName"] ?: "알 수 없음").toString() + " / " + (it["subRegionName"] ?: "알 수 없음").toString()
                                        },
                                        sharePlan = dailyPlanData.entries.map { (day, places) ->
                                            places.map { place ->
                                                val placeName = place["title"]?.toString() ?: "장소 없음"
                                                val addr = place["addr1"]?.toString() ?: "주소 정보 없음"
                                                val mapX = place["mapx"]?.toString()?.toDoubleOrNull()?.toString() ?: "0.0"
                                                val mapY = place["mapy"]?.toString()?.toDoubleOrNull()?.toString() ?: "0.0"

                                                mapOf(
                                                    "date" to day,
                                                    "place" to placeName,
                                                    "addr" to addr,
                                                    "mapx" to mapX,
                                                    "mapy" to mapY
                                                )
                                            }
                                        }.flatten()

                                    )
                                    Toast.makeText(
                                        context,
                                        "${selectedPostChip.value} 게시 완료!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    navController.popBackStack()
                                } else {
                                    Toast.makeText(
                                        context,
                                        "여행 후기는 최소 1개의 이미지를 첨부해야 합니다.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                                // Firestore 저장이 끝난 후 로딩 상태 해제
                                postViewModel.setLoading(false)
                            }
                        }

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
                titleAlign = TextAlign.Center,
                textAlign = TextAlign.Center,
                titleModifier = Modifier.fillMaxWidth(),
                textModifier = Modifier.fillMaxWidth(),
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
                        postViewModel.postItems.forEach { chipText ->
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
                                    postViewModel.updateSelectedPostChip(text)
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
                            postViewModel.chipItems.forEach { chipText ->
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
                                        postViewModel.updateSelectedChip(text)
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
                        textFieldValue = remember { mutableStateOf(postViewModel.titleState.value) },
                        label = "제목",
                        placeHolder = "제목을 입력하세요",
                        maxLength = 30,
                        showCharCount = true,
                        onValueChange = { postViewModel.updateTitle(it) },
                        singleLine = true,
                        trailingIconMode = LikeLionOutlinedTextFieldEndIconMode.TEXT,
                        onTrailingIconClick = { postViewModel.updateTitle("") },
                        modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 10.dp)
                    )

                    LikeLionDivider(
                        modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 10.dp),
                        color = Color.LightGray,
                        thickness = 1.dp
                    )

                    Spacer(modifier = Modifier.height(10.dp))


                    // 사진 업로드
                    if (imageUris.value.isNotEmpty()) {
                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(imageUris.value.size) { index ->
                                Box(
                                    modifier = Modifier
                                        .size(90.dp) //
                                        .padding(top = 4.dp, end = 4.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(80.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                    ) {
                                        Image(
                                            painter = rememberAsyncImagePainter(imageUris.value[index]),
                                            contentDescription = "Uploaded Image",
                                            modifier = Modifier.fillMaxSize(),
                                            contentScale = ContentScale.Crop
                                        )
                                    }

                                    // X 버튼
                                    IconButton(
                                        onClick = { postViewModel.removeImage(index) },
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
                                putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
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
                        textFieldValue = remember { mutableStateOf(postViewModel.contentState.value) },
                        label = "내용",
                        placeHolder = "내용을 입력하세요",
                        maxLength = 500,
                        showCharCount = true,
                        onValueChange = { postViewModel.updateContent(it) },
                        modifier = Modifier
                            .padding(start = 20.dp, end = 20.dp, top = 10.dp)
                            .height(300.dp),
                        singleLine = false,
                        trailingIconMode = LikeLionOutlinedTextFieldEndIconMode.TEXT,
                        onTrailingIconClick = { postViewModel.updateContent("") }
                    )

                    // "여행 후기"를 선택하면 보이는 버튼
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

                        // "일정 공유" 버튼 아래에 선택된 일정 표시
                        if (selectedTitle.isNotEmpty()) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp, vertical = 10.dp)
                            ) {
                                // 제목
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 15.dp, bottom = 15.dp),
                                    verticalAlignment = Alignment.Bottom
                                ) {
                                    Text(
                                        text = selectedTitle,
                                        style = MaterialTheme.typography.titleLarge,
                                        modifier = Modifier.padding(end = 10.dp)
                                    )
                                }

                                // 일정 날짜
                                Text(
                                    text = "$selectedStartDate ~ $selectedEndDate",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = GrayColor,
                                    modifier = Modifier.padding(bottom = 15.dp)
                                )

                                // 지역 정보
                                tripCityList.forEach { trip ->
                                    val regionName = trip["regionName"] as? String ?: "도시 없음"
                                    val subRegionName = trip["subRegionName"] as? String ?: "도시 없음"

                                    Text(
                                        text = "📍 여행 지역: $regionName / $subRegionName",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = GrayColor,
                                        modifier = Modifier.padding(bottom = 5.dp)
                                    )
                                }

                                // Google Map
//                                Column(
//                                    modifier = Modifier
//                                        .height(300.dp)
//                                        .padding(bottom = 10.dp)
//                                ) {
//                                    LikeLionGoogleMap(
//                                        cameraPositionState = cameraPositionState,
//                                        modifier = Modifier.fillMaxSize(),
//                                        selectedPlaces = selectedDayPlaces,
//                                        isAddTripPlan = true,
//                                        markerTitle = markerTitles,
//                                        markerSnippet = markerSnippets,
//                                    )
//                                }

                                // "일별 일정 목록"
                                dailyPlanData.entries.forEachIndexed { index, (day, places) ->
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(bottom = 10.dp)
                                    ) {
                                        // DayX 표시 + 날짜
                                        Row(
                                            modifier = Modifier.padding(top = 10.dp, bottom = 20.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = "Day${index + 1}  $day",
                                                style = MaterialTheme.typography.bodyLarge,
                                                color = Color.Black
                                            )
                                        }

                                        places.forEachIndexed { placeIndex, place ->
                                            val distanceToNext = if (placeIndex < places.lastIndex) {
                                                // 거리 계산
                                                tripInfoViewModel.calculateDistance(
                                                    LatLng(
                                                        (place["mapy"] as? String)?.toDoubleOrNull() ?: 0.0,
                                                        (place["mapx"] as? String)?.toDoubleOrNull() ?: 0.0
                                                    ),
                                                    LatLng(
                                                        (places[placeIndex + 1]["mapy"] as? String)?.toDoubleOrNull() ?: 0.0,
                                                        (places[placeIndex + 1]["mapx"] as? String)?.toDoubleOrNull() ?: 0.0
                                                    )
                                                )
                                            } else {
                                                null // 마지막 장소는 거리 표시 X
                                            }

                                            LikeLionAddPlaceItem(
                                                index = placeIndex,
                                                lastIndex = places.lastIndex,
                                                place = place,
                                                distanceToNext = distanceToNext
                                            )
                                        }

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
        }
        // 로딩 상태 compose 호출
        if (isLoading) {
            Loading()
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
            Text("게시글을 저장하는 중...", color = Color.Black)

        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PostScreenPreview() {
    PostScreen(
        navController = NavController(LocalContext.current),
        onAddClick = {}
    )
}