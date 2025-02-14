package com.lion.FinalProject_CarryOn_Anywhere.component

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lion.FinalProject_CarryOn_Anywhere.data.server.model.myposts.ReplyModel

// ✅ 개별 댓글 아이템
@Composable
fun LikeLionMyComment(
    comment: ReplyModel,
    onDeleteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f) // ✅ 텍스트가 남은 공간을 차지하도록 설정
                .padding(end = 10.dp)
                .padding(bottom = 10.dp)

        ) {
            // ✅ 댓글 내용
            Text(
                text = comment.replyContent,
                fontSize = 15.sp,
            )

            Spacer(modifier = Modifier.height(4.dp))

            // ✅ 날짜
            Text(
                text = comment.replyTimeStamp,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }

        // ✅ 삭제 아이콘
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Delete Comment",
            modifier = Modifier
                .size(25.dp)
                .clickable { onDeleteClick() },
            tint = Color.Gray
        )
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun LikeLionMyCommentList(
    commentList: List<ReplyModel>,
    onDeleteConfirmed: (ReplyModel) -> Unit
) {
    // ✅ 다이얼로그 상태 및 삭제할 댓글 상태
    val showDialog = remember { mutableStateOf(false) }
    val selectedComment = remember { mutableStateOf<ReplyModel?>(null) }

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(commentList) { comment ->
            LikeLionMyComment(
                comment = comment,
                onDeleteClick = {
                    selectedComment.value = comment
                    showDialog.value = true // ✅ 삭제 다이얼로그 표시
                }
            )
        }
    }

    // ✅ 삭제 확인 다이얼로그
    if (showDialog.value && selectedComment.value != null) {
        LikeLionAlertDialog(
            showDialogState = showDialog,
            title = "댓글을 삭제하시겠어요?",
            text = "데이터가 모두 삭제되며 복구할 수 없습니다.",
            confirmButtonTitle = "삭제",
            confirmButtonOnClick = {
                selectedComment.value?.let { onDeleteConfirmed(it) }
                showDialog.value = false
            },
            dismissButtonTitle = "취소",
            dismissButtonOnClick = {
                showDialog.value = false
            }
        )
    }
}

// ✅ 미리보기
@Preview(showBackground = true)
@Composable
fun PreviewLikeLionMyCommentList() {
    val testComments = listOf(
        ReplyModel("토토로", "날씨가 좋아서 다행이에요. 너무 부럽습니다. 진짜 좋아보여요.", "2023-04-25 14:15:22"),
        ReplyModel("토토로", "터질 것만 같은 행복한 기분으로 틀에 박힌 관념 다 버리고 이제 또 맨 주먹 정신 다시 또 시작하면 나 이루리라 다 나 바라는대로", "2022-04-25 14:15:22"),
        ReplyModel(
            "토토로", "파란 하늘위로 훨훨 날아가겠죠\n" +
                    "어려서 꿈꾸었던 비행기 타고\n" +
                    "기다리는 동안 아무말도 못해요 내 생각 말할 순 없어요", "2021-04-25 14:15:22"
        )
    )

    LikeLionMyCommentList(
        commentList = testComments,
        onDeleteConfirmed = { deletedComment ->
            // ✅ 삭제 처리 로직 (현재는 테스트용 출력)
            println("Deleted: ${deletedComment.replyContent}")
        }
    )
}