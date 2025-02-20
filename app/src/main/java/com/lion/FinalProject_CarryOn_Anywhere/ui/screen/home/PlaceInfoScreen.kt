package com.lion.FinalProject_CarryOn_Anywhere.ui.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
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
    placeSearchViewModel: PlaceSearchViewModel,
    placeInfoViewModel: PlaceInfoViewModel = hiltViewModel(),
) {

    // 찜 버튼 상태
    var isFavorite by remember { mutableStateOf(false) }

    // 검색된 장소 정보를 ViewModel에 설정
    LaunchedEffect(contentId) {
        placeInfoViewModel.fetchPlaceInfo(contentId)
    }

    val placeDetail by placeInfoViewModel.placeDetail.collectAsState()

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
                .padding(horizontal = 10.dp),
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
                                .padding(
                                    horizontal = 10.dp
                                )
                        ) {
                            Text(
                                text = place["title"] as String,
                                style = Typography.titleLarge,

                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            Text(
                                text = place["contentTypeId"] as String,
                                style = Typography.titleMedium,
                                color = GrayColor,
                                modifier = Modifier
                                    .padding(
                                        start = 10.dp,
                                    )
                            )
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.Transparent),
                            contentAlignment = Alignment.BottomEnd
                        ) {
                            // 장소 이미지
                            AsyncImage(
                                model = place["imageRes"] as String,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp)
                                    .clip(RoundedCornerShape(10.dp)),
                                contentScale = ContentScale.Crop,
                            )

                            // 찜 버튼
                            // TODO: 버튼 속성 배경 수정
                            IconButton(
                                onClick = { isFavorite = !isFavorite },
                                modifier = Modifier
                                    .padding(16.dp)
                                    .size(50.dp)
                                    .background(Color.Transparent, shape = CircleShape)
                            ) {
                                Icon(
                                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                    contentDescription = "Favorite",
                                    modifier = Modifier
                                        .size(30.dp),
                                    tint = if (isFavorite) Color(0xFFFF5255) else Color.Gray
                                )
                            }
                        }


                        Spacer(modifier = Modifier.height(10.dp))

                        // 주소
                        Text(
                            text = "주소",
                            style = Typography.labelLarge,
                            color = SubTextColor
                        )
                        Text(
                            text = place["address"] as String,
                            color = Color.Black
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // 연락처
                        Text(
                            text = "연락처",
                            style = Typography.labelLarge,
                            color = SubTextColor
                        )
                        Text(
                            text = place["call"] as String,
                            color = Color.Black
                        )

                        Spacer(modifier = Modifier.height(10.dp))


                        // TODO : 내용 (어떤 값들 불러오는지 확인)
                        Text(
                            text = "내용",
                            style = Typography.labelLarge,
                            color = SubTextColor
                        )
                        Text(
                            text = place["content"] as String,
                            color = Color.Black
                        )
                    }

                } ?: Text(text = "장소 정보를 불러오는 중...", color = GrayColor)
            }
        }

    }
}