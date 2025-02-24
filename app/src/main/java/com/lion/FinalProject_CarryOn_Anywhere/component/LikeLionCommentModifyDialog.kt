package com.lion.FinalProject_CarryOn_Anywhere.component

import androidx.compose.foundation.layout.Column
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


@Composable
fun LikeLionCommentModifyDialog(
    showDialog: MutableState<Boolean>,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    // 입력된 댓글 텍스트를 저장할 상태
    val commentText = remember { mutableStateOf("") }

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = {
                Text(
                    text = "댓글 입력",
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
                Button(
                    onClick = {
                        onConfirm(commentText.value)
                        showDialog.value = false
                    }
                ) {
                    Text("확인")
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = {
                        showDialog.value = false
                        onDismiss()
                    }
                ) {
                    Text("취소", color = Color.Blue)
                }
            }
        )
    }
}
