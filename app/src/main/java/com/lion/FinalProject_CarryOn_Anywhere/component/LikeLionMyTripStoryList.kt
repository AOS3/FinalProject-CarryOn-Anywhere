package com.lion.FinalProject_CarryOn_Anywhere.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.rememberAsyncImagePainter
import com.lion.FinalProject_CarryOn_Anywhere.R
import com.lion.FinalProject_CarryOn_Anywhere.data.server.model.CarryTalkModel
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.TalkTag
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


// 나의 글 -> 여행 이야기에 들어가는 리스트 컴포넌트

@Composable
fun LikeLionTripStoryList(post: CarryTalkModel, onClick: () -> Unit) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp, horizontal = 10.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(Color.White),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                // 태그
                if (post.talkTag != TalkTag.TALK_TAG_ALL) {
                    Text(
                        text = post.talkTag.str, // 리스트를 문자열로 변환 + 연결
                        fontSize = 12.sp,
                        color = Color.White,
                        modifier = Modifier
                            .background(Color.Black, RoundedCornerShape(5.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(6.dp))
                }

                // ✅ 제목
                Text(
                    text = post.talkTitle,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                // ✅ 설명
                Text(
                    text = post.talkContent,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // ✅ 날짜 및 조회수
                    Text(
                        text = "${formattedDate(post.talkTimeStamp)}",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )

                    // ✅ 날짜 및 댓글 수
                    Text(
                        text = "댓글 ${post.talkReplyList.size}",
                        fontSize = 12.sp,
                        color = Color.Gray,

                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // 맨 처음 사진만 나오게 한다
            post.talkImage.firstOrNull()?.let { imageUrl ->
                Image(
                    painter = rememberAsyncImagePainter(imageUrl),
                    contentDescription = "여행 이야기 Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(100.dp)
                        .height(100.dp)
                        .clip(RoundedCornerShape(10.dp))

                )
            }
        }
    }
}

// 날짜 변환
private fun formattedDate(timestamp: Long): String {
    val date = Date(timestamp)
    val format = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    return format.format(date)
}

//// 미리보기
//@Preview(showBackground = true)
//@Composable
//fun PreviewTripStoryList() {
//    val samplePost = TripStoryModel(
//        TripStoryTitle = "제주도 여행 이야기",
//        TripStoryContent = "제주도에서 즐거운 여행을 하고 왔습니다!",
//        TripStoryDate = "2025-02-10",
//        TripStoryViewCount = 123,
//        TripStoryImages = listOf(R.drawable.test1.toString()), // ✅ 테스트용 이미지 URL
//        TripStoryTag = "여행 일정"
//    )
//
//    LikeLionTripStoryList(post = samplePost, onClick = {})
//}