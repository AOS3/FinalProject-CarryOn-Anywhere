package com.lion.FinalProject_CarryOn_Anywhere.ui.screen.review

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.lion.FinalProject_CarryOn_Anywhere.R
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionEmptyView
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionLikeButton
import com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.social.Review
import com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.social.ReviewViewModel

@Composable
fun ReviewScreen(
    reviewViewModel: ReviewViewModel = hiltViewModel(),
    navController: NavController
) {
    val reviews by reviewViewModel.reviews.collectAsState()

    if (reviews.isEmpty()) {
        LikeLionEmptyView(message = "작성된 후기가 없습니다.")
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(5.dp)
                .padding(bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(bottom = 20.dp)
        ) {
            items(reviews.size) { index ->
                ReviewCard(
                    review = reviews[index],
                    onClick = { navController.navigate("reviewDetail/$index") }
                )
            }
        }
    }
}

@Composable
private fun ReviewCard(review: Review, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Image(
                painter = painterResource(id = review.imageRes[0]),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .padding(bottom = 10.dp)
                    .clip(RoundedCornerShape(10.dp))
            )

            Text(
                text = review.title,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = review.date,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                modifier = Modifier.padding(vertical = 5.dp)
            )

            // 좋아요(왼쪽), 댓글(오른쪽) 정렬
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 좋아요 아이콘 & 숫자
                Row(verticalAlignment = Alignment.CenterVertically) {
                    LikeLionLikeButton()

                    Text(
                        text = " ${review.likes}",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 1.dp)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // 댓글 아이콘 & 숫자
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(R.drawable.chat_24px),
                        contentDescription = "Comments",
                        modifier = Modifier.size(20.dp),
                        colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(Color.Black)
                    )
                    Text(
                        text = " ${review.comments}",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 1.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ReviewScreenPreview() {
    ReviewScreen(
        navController = NavController(LocalContext.current)
    )
}