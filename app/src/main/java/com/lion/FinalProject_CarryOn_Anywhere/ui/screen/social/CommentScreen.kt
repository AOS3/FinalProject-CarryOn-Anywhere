package com.lion.FinalProject_CarryOn_Anywhere.ui.screen.social

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionAlertDialog
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionBottomSheet
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionBottomSheetDivider
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionDivider
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionEmptyView
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionOutlinedTextField
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionOutlinedTextFieldEndIconMode
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionTopAppBar
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.MainColor
import com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.social.Comment
import com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.social.CommnetViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentScreen(
    navController: NavController,
    commentViewModel: CommnetViewModel = hiltViewModel()
) {
    val inputBarHeight = 60.dp

    val commnets by commentViewModel.comments.collectAsState()

    val textState = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White) // 배경색을 흰색으로 설정
    ) {
        LikeLionTopAppBar(
            title = "댓글",
            backColor = Color.White,
            navigationIconImage = Icons.Default.ArrowBack,
            navigationIconOnClick = { navController.popBackStack() },
        )

        Box(
            modifier = Modifier
                .weight(1f) // LazyColumn이 키보드 영향을 받지 않도록
                .fillMaxWidth()
        ) {
            if (commnets.isEmpty()) {
                LikeLionEmptyView(message = "댓글이 없습니다.")
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 20.dp),
                ) {
                    items(commnets.size) { index ->
                        CommentItem(commnets[index], navController, index)

                        LikeLionDivider(
                            modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 5.dp),
                            color = Color.LightGray,
                            thickness = 1.dp
                        )
                    }
                }
            }
        }

        // 하단 입력창만 키보드에 따라 올라가게 설정
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .imePadding() // 키보드가 나타날 때 이 영역만 이동
                .windowInsetsPadding(WindowInsets.ime), // 추가로 입력창만 위로 이동
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 입력 필드
            LikeLionOutlinedTextField(
                textFieldValue = textState,
                label = "댓글을 입력해 주세요.",
                singleLine = true,
                trailingIconMode = LikeLionOutlinedTextFieldEndIconMode.TEXT,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            )

            // 전송 버튼
            IconButton(
                onClick = {
                    val text = textState.value.trim()
                    if (text.isNotEmpty()) {
                        textState.value = ""
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "댓글 전송",
                    tint = MainColor
                )
            }
        }
    }

}

@Composable
private fun CommentItem(comment: Comment, navController: NavController, index: Int) {

    var isBottomSheetVisible by remember { mutableStateOf(false) }

    val showDialogDeleteState = remember { mutableStateOf(false) }
    val showDialogNotifyState = remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp, horizontal = 15.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .fillMaxHeight(), // Row의 높이를 자동으로 맞추도록 설정
            verticalAlignment = Alignment.CenterVertically // 세로 중앙 정렬 추가
        ) {
            // 왼쪽 Column (태그, 제목, 내용, 작성자 정보)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(), // Row의 높이를 상속
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // 제목
                Text(
                    text = comment.author,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                // 내용
                Text(
                    text = comment.content,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                // 작성 날짜
                Text(
                    text = comment.commentDate,
                    fontSize = 12.sp,
                    color = Color.LightGray
                )
            }

            // 오른쪽 Column (선택 버튼)
            // 오른쪽 Column (선택 버튼 + More 아이콘)
            Column(
                modifier = Modifier
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // More 아이콘 추가
                IconButton(
                    onClick = { isBottomSheetVisible = true },
                    modifier = Modifier
                        .align(Alignment.End)
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More Options",
                        tint = Color.Black
                    )
                }

                // 본인 댓글일 때
                if (isBottomSheetVisible) {
                    LikeLionBottomSheetDivider(
                        onDismissRequest = { isBottomSheetVisible = false },
                        text1 = "수정",
                        text1Color = Color.Black,
                        text1OnClick = {
                            isBottomSheetVisible = false
                            // 수정 로직 구현
                        },
                        text2 = "삭제",
                        text2Color = Color.Red,
                        text2OnClick = {
                            isBottomSheetVisible = false
                            showDialogDeleteState.value = true
                        }
                    )
                }
                LikeLionAlertDialog(
                    showDialogState = showDialogDeleteState,
                    title = "댓글을 삭제하시겠습니까?",
                    text = "삭제되면 복구할 수 없습니다.",
                    confirmButtonTitle = "삭제",
                    confirmButtonOnClick = {
                        showDialogDeleteState.value = false
                    },
                    dismissButtonTitle = "취소",
                    dismissButtonOnClick = {
                        showDialogDeleteState.value = false
                    },
                    titleAlign = TextAlign.Center, // 제목 중앙 정렬
                    textAlign = TextAlign.Center, // 본문 텍스트 중앙 정렬
                    titleModifier = Modifier.fillMaxWidth(), // 제목 가로 중앙 정렬
                    textModifier = Modifier.fillMaxWidth(), // 본문 가로 중앙 정렬
                    confirmButtonModifier = Modifier.width(120.dp),
                    dismissButtonModifier = Modifier.width(120.dp)
                )

//                // 타인 댓글일 때
//                if (isBottomSheetVisible) {
//                    LikeLionBottomSheetDivider(
//                        onDismissRequest = { isBottomSheetVisible = false },
//                        text1 = "신고하기",
//                        text1Color = Color.Red,
//                        text1OnClick = {
//                            isBottomSheetVisible = false
//                            showDialogNotifyState.value = true
//                        }
//                    )
//                }
//                LikeLionAlertDialog(
//                    showDialogState = showDialogNotifyState,
//                    title = "댓글을 신고하시겠습니까?",
//                    text = "신고가 접수되면 검토 후 필요한 조치를 취하겠습니다.",
//                    confirmButtonTitle = "신고",
//                    confirmButtonOnClick = {
//                        showDialogNotifyState.value = false
//                    },
//                    dismissButtonTitle = "취소",
//                    dismissButtonOnClick = {
//                        showDialogNotifyState.value = false
//                    },
//                    titleAlign = TextAlign.Center, // 제목 중앙 정렬
//                    textAlign = TextAlign.Center, // 본문 텍스트 중앙 정렬
//                    titleModifier = Modifier.fillMaxWidth(), // 제목 가로 중앙 정렬
//                    textModifier = Modifier.fillMaxWidth(), // 본문 가로 중앙 정렬
//                    confirmButtonModifier = Modifier.width(120.dp),
//                    dismissButtonModifier = Modifier.width(120.dp)
//                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ReviewScreenPreview() {
    CommentScreen(
        navController = NavController(LocalContext.current)
    )
}