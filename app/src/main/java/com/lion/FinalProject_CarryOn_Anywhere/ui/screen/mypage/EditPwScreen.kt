package com.lion.FinalProject_CarryOn_Anywhere.ui.screen.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.lion.FinalProject_CarryOn_Anywhere.component.*
import com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.UserSettingViewModel
import com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.mypage.EditPwViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EditPwScreen(
    //navController: NavController,
    editPwViewModel: EditPwViewModel = hiltViewModel()
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    // ⚡ MutableState<String>으로 선언하여 상태를 유지
    val currentPassword = remember { mutableStateOf("") }
    val newPassword = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            LikeLionTopAppBar(
                title = "비밀번호 변경",
                backColor = Color.White,
                navigationIconImage = Icons.AutoMirrored.Filled.ArrowBack,
                navigationIconOnClick = { editPwViewModel.navigationIconOnClick() },
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding)
                .padding(20.dp)
                .imePadding() // ✅ 키보드가 올라와도 UI를 적절하게 조정
        ) {
           // Spacer(modifier = Modifier.height(10.dp)) // 상단 여백 추가

            // ✅ 비밀번호 입력 필드 섹션
            Column {

                // 현재 비밀번호 입력
                LikeLionOutlinedTextField(
                    textFieldValue = editPwViewModel.textFieldCurrentPw,
                    onValueChange = { editPwViewModel.textFieldCurrentPw.value = it },
                    label = "현재 비밀번호",
                    placeHolder = "현재 비밀번호를 입력해 주세요.",
                    trailingIconMode = LikeLionOutlinedTextFieldEndIconMode.PASSWORD,
                    singleLine = true,
                    inputType = LikeLionOutlinedTextFieldInputType.PASSWORD,
                    inputCondition = "[^a-zA-Z0-9_]",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                )

                // 새 비밀번호 입력
                LikeLionOutlinedTextField(
                    textFieldValue = editPwViewModel.textFieldNewPw,
                    onValueChange = { editPwViewModel.textFieldNewPw.value = it },
                    label = "새 비밀번호",
                    placeHolder = "새 비밀번호를 입력해 주세요.",
                    trailingIconMode = LikeLionOutlinedTextFieldEndIconMode.PASSWORD,
                    singleLine = true,
                    inputType = LikeLionOutlinedTextFieldInputType.PASSWORD,
                    inputCondition = "[^a-zA-Z0-9_]",
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next // ✅ "다음" 버튼 활성화
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 3.dp)
                )

                Row(modifier = Modifier.padding(bottom = 10.dp)) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Check",
                        modifier = Modifier
                            .size(20.dp)
                            .padding(end = 4.dp)
                    )
                    Text(
                        text = "영문 숫자 포함 10자 이상",
                        fontSize = 14.sp
                    )
                }

                // 새 비밀번호 확인 입력
                LikeLionOutlinedTextField(
                    textFieldValue = editPwViewModel.textFieldNewPw2,
                    onValueChange = { editPwViewModel.textFieldNewPw2.value = it },
                    label = "새 비밀번호 확인",
                    placeHolder = "새 비밀번호를 입력해 주세요.",
                    trailingIconMode = LikeLionOutlinedTextFieldEndIconMode.PASSWORD,
                    singleLine = true,
                    inputType = LikeLionOutlinedTextFieldInputType.PASSWORD,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done // ✅ "완료" 버튼 활성화
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { keyboardController?.hide() } // ✅ "완료" 누르면 키보드 숨김
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 5.dp)
                )


                // ✅ 등록 버튼
                LikeLionFilledButton(
                    text = "등록",
                    cornerRadius = 5,
                    onClick = {
                        editPwViewModel.buttonChangePwDoneOnClick()
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }

    // 비밀번호 변경 성공 다이얼로그
    LikeLionAlertDialog(
        showDialogState = editPwViewModel.showDialogPwOk,
        confirmButtonTitle = "확인",
        confirmButtonOnClick = {
            editPwViewModel.navigationConfirmButtonClick()
        },
        title = "비밀번호 변경",
        titleAlign = TextAlign.Center,
        text = "비밀번호 변경에 성공하셨습니다.",
        textAlign = TextAlign.Center,
        titleModifier = Modifier
            .fillMaxWidth(),
        textModifier = Modifier
            .fillMaxWidth()
    )

    // ✅ 유효성 검사 다이얼로그
    if (editPwViewModel.showDialogPwEmpty.value) {
        LikeLionAlertDialog(
            showDialogState = editPwViewModel.showDialogPwEmpty,
            title = "입력 오류",
            text = "현재 비밀번호를 입력해 주세요.",
            confirmButtonTitle = "확인"
        )
    }

    if (editPwViewModel.showDialogPw1Mismatch.value) {
        LikeLionAlertDialog(
            showDialogState = editPwViewModel.showDialogPw1Mismatch,
            title = "입력 오류",
            text = "현재 비밀번호가 일치하지 않습니다.",
            confirmButtonTitle = "확인"
        )
    }

    if (editPwViewModel.showDialogPwShort.value) {
        LikeLionAlertDialog(
            showDialogState = editPwViewModel.showDialogPwShort,
            title = "입력 오류",
            text = "새 비밀번호는 10자 이상 입력해야 합니다.",
            confirmButtonTitle = "확인"
        )
    }

    if (editPwViewModel.showDialogPw2Mismatch.value) {
        LikeLionAlertDialog(
            showDialogState = editPwViewModel.showDialogPw2Mismatch,
            title = "입력 오류",
            text = "새 비밀번호가 일치하지 않습니다.",
            confirmButtonTitle = "확인"
        )
    }
}

//// ✅ 프리뷰 추가
//@Preview(showBackground = true)
//@Composable
//fun PreviewEditPwScreen() {
//    EditPwScreen()
//}
