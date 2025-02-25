package com.lion.FinalProject_CarryOn_Anywhere.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.OutlinedTextField
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.MainColor


@Composable
fun LikeLionCommentModifyDialog(
    showDialog: MutableState<Boolean>,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    val commentText = remember { mutableStateOf("") }

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = {
                Text(
                    text = "댓글 수정",
                    fontSize = 20.sp
                )
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "댓글을 입력해주세요.",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = commentText.value,
                        onValueChange = { newText ->
                            commentText.value = newText
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("댓글을 입력하세요.") }
                    )
                }
            },
            confirmButton = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,

                ) {
                    // 취소 버튼
                    LikeLionFilledButton(
                        horizontalPadding = 0.dp,
                        fillWidth = false,
                        containerColor = Color.Transparent,
                        contentColor = MainColor,
                        text = "취소",
                        border = BorderStroke(1.dp, MainColor),
                        onClick = {
                            showDialog.value = false
                            onDismiss()
                        },
                        cornerRadius = 5,
                        buttonWidth = 120.dp,
                        paddingBottom = 20.dp,
                    )
                    // 확인 버튼 (입력이 있을 때만 활성화)
                    LikeLionFilledButton(
                        horizontalPadding = 0.dp,
                        fillWidth = false,
                        containerColor = if (commentText.value.isNotBlank()) MainColor else Color.Gray,
                        contentColor = Color.White,
                        text = "확인",
                        onClick = {
                            if (commentText.value.isNotBlank()) {
                                onConfirm(commentText.value)
                                showDialog.value = false
                            }
                        },
                        cornerRadius = 5,
                        buttonWidth = 120.dp,
                        paddingBottom = 20.dp,
                    )

                }
            }
        )
    }
}