package com.lion.FinalProject_CarryOn_Anywhere.ui.screen.home

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType.Companion.Uri
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import com.lion.FinalProject_CarryOn_Anywhere.R
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionIconButton
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionTopAppBar
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.GrayColor
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.SubTextColor
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.Typography
import com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.home.PlaceInfoViewModel
import com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.home.PlaceSearchViewModel

@Composable
fun PlaceInfoScreen(
    navController: NavController,
    contentId: String,
    contentTypeId: String,
    placeSearchViewModel: PlaceSearchViewModel,
    placeInfoViewModel: PlaceInfoViewModel = hiltViewModel(),
) {

    // 로그인 여부, 다이얼로그
    val showDialog = remember { mutableStateOf(false) }

    // 사용자 찜 목록
    val userLikeList by placeSearchViewModel.userLikeList.collectAsState()
    var isLiked by remember { mutableStateOf(userLikeList.any { it["contentid"] == contentId }) }

    // 장소 정보
    val placeDetailList by placeInfoViewModel.placeDetail.collectAsState()
    val placeDetail = placeDetailList.firstOrNull()

    val context = LocalContext.current

    // 검색된 장소 정보를 ViewModel에 설정
    LaunchedEffect(contentId, contentTypeId) {
        placeInfoViewModel.fetchPlaceInfo(contentId, contentTypeId)
        placeInfoViewModel.gettingUserLikeList()
    }

    LaunchedEffect(userLikeList) {
        isLiked = userLikeList.any { it["contentid"] == contentId }
    }

    Scaffold(
        topBar = {
            LikeLionTopAppBar(
                backColor = Color.Transparent,
                title = "장소 정보",
                navigationIconImage = Icons.AutoMirrored.Filled.ArrowBack,
                navigationIconOnClick = {
                    navController.popBackStack()
                },
            )
        },

        ) {
        // 장소 정보
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(it)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(bottom = 60.dp)
        ) {
            item {
                placeDetail?.let { place ->
                    Column {
                        // 장소 이름 + 대분류 (카테고리)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 10.dp)
                        ) {
                            Text(
                                text = place["title"] as String,
                                style = Typography.titleLarge,

                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = place["category"] as String,
                                style = Typography.titleMedium,
                                color = GrayColor,
                                modifier = Modifier
                                    .padding(
                                        start = 5.dp,
                                    )
                                    .align(Alignment.CenterVertically),
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.Transparent)
                                .padding(top = 10.dp),
                            contentAlignment = Alignment.BottomEnd
                        ) {
                            // 장소 이미지
                            val imageUrl = placeDetail["firstimage"].toString()
                            // 이미지 url http ↔ https 변환
                            val fixedImageUrl = when {
                                imageUrl.startsWith("http://") -> imageUrl.replace("http://", "https://")
                                imageUrl.startsWith("https://") -> imageUrl.replace("https://", "http://")
                                else -> null
                            }

                            AsyncImage(
                                model = fixedImageUrl,
                                contentDescription = "장소 이미지",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp)),
                                contentScale = ContentScale.Crop,
                                placeholder = painterResource(R.drawable.noplaceimg), // 로드 발생중 보여줄 사진
                                error = painterResource(R.drawable.noplaceimg)
                            )

                            // 찜 버튼
                            // TODO: 버튼 속성 배경 수정

                        }

                        LikeLionIconButton(
                            icon =  if (isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            iconColor = Color(0xFFFF5255),
                            size = 35.dp,
                            iconButtonOnClick = {
                                placeInfoViewModel.toggleFavorite(contentId, contentTypeId, onLoginRequired = { showDialog.value = true }) {onLiked ->
                                    isLiked = onLiked
                                    Toast.makeText(
                                        context,
                                        if (onLiked) "내 장소에 추가되었습니다" else "내 장소에서 삭제되었습니다",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            },
                            iconBackColor = Color.Transparent,
                            modifier = Modifier
                                .padding(top = 10.dp)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // 주소
                        Text(
                            text = "주소",
                            style = Typography.labelLarge,
                            color = SubTextColor
                        )
                        Text(
                            text = place["address"] as String,
                            color = Color.Black,
                            modifier = Modifier.padding(top = 5.dp)

                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // 연락처
                        Text(
                            text = "연락처",
                            style = Typography.labelLarge,
                            color = SubTextColor
                        )
                        Text(
                            text = place["tel"] as String,
                            color = Color.Black,
                            modifier = Modifier.padding(top = 5.dp)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // 홈페이지
                        Text(
                            text = "홈페이지",
                            style = Typography.labelLarge,
                            color = SubTextColor
                        )
                        val homepageUrl = place["homepage"] as String

                        if (homepageUrl.isNotEmpty() && homepageUrl != "홈페이지 정보 없음") {
                            Text(
                                text = homepageUrl,
                                color = Color.Blue,
                                modifier = Modifier
                                    .padding(top = 5.dp)
                                    .clickable {
                                        val intent = Intent(Intent.ACTION_VIEW, android.net.Uri.parse(homepageUrl))
                                        context.startActivity(intent)
                                    }
                            )
                        } else {
                            Text(
                                text = "홈페이지 정보 없음",
                                color = Color.Gray,
                                modifier = Modifier.padding(top = 5.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // 내용
                        Text(
                            text = "내용",
                            style = Typography.labelLarge,
                            color = SubTextColor
                        )
                        Text(
                            text = place["overview"] as String,
                            color = Color.Black,
                            modifier = Modifier.padding(top = 5.dp)
                        )

                        // TODO : 상세 정보 어떤 것들 불러와야할지 확인 후 추가 예정 (오픈시간, 입장료 등)
                    }

                } ?: Text(text = "장소 정보를 불러오는 중...", color = GrayColor)
            }
        }

    }
}