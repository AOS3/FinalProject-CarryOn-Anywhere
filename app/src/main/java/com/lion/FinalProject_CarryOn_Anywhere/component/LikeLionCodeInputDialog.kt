package com.lion.FinalProject_CarryOn_Anywhere.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LikeLionCodeInputDialog(
    showDialog: MutableState<Boolean>,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val tripCode = remember { mutableStateOf("") } // 입력된 일정 코드 상태

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = {
                Text(
                    text = "일정 코드 입력",
                    fontSize = 20.sp
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "친구에게 공유받은 코드를 입력해주세요.",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = tripCode.value,
                        onValueChange = { newText ->
                            if (newText.length <= 5) { // ✅ 5자리 제한
                                tripCode.value = newText
                            }
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("일정 코드 입력") }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onConfirm(tripCode.value) // ✅ 입력된 코드 전달
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
