package com.lion.FinalProject_CarryOn_Anywhere.component

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import com.lion.FinalProject_CarryOn_Anywhere.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LikeLionSearchTopAppBar(
    textFieldValue: MutableState<String> = mutableStateOf(""),
    onSearchTextChange: (String) -> Unit,
    // 입력값이 변경될 때 호출되는 콜백
    onTrailingIconClick: (() -> Unit)? = null,
    menuItems : @Composable RowScope.() -> Unit = {},
    onSearchClick: () -> Unit, // 추가: 검색 버튼 클릭 시 실행될 콜백
    // 키보드 옵션
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
    // 우측 끝 아이콘 (텍스트 삭제, 비밀번호 보기)
    trailingIconMode: LikeLionSearchTopAppBarTextFieldEndIconMode = LikeLionSearchTopAppBarTextFieldEndIconMode.NONE,
    onBackClick: () -> Unit = {}
) {
    // 비밀번호 보기 여부
    var isShowingPasswordFlag by rememberSaveable { mutableStateOf(false) }

    TopAppBar(
        title = {
            TextField(
                value = textFieldValue.value,
                onValueChange = { onSearchTextChange(it) },
                placeholder = { Text("관광지 / 맛집 / 숙소 검색", color = Color.Gray) },
                keyboardOptions = keyboardOptions,
                keyboardActions = KeyboardActions(
                    onSearch = {
                        onSearchClick() // 검색 버튼 클릭 시 실행
                    }
                ),
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent, // 배경 투명
                    unfocusedContainerColor = Color.Transparent, // 포커스 해제 시 배경 투명
                    cursorColor = Color.Black, // 커서 색상
                    focusedIndicatorColor = Color.Transparent, // 밑줄 제거 (포커스)
                    unfocusedIndicatorColor = Color.Transparent // 밑줄 제거 (비포커스)
                ),
                trailingIcon = {
                    when (trailingIconMode) {
                        // 아이콘 없음
                        LikeLionSearchTopAppBarTextFieldEndIconMode.NONE -> {}
                        LikeLionSearchTopAppBarTextFieldEndIconMode.TEXT -> {
                            if (textFieldValue.value.isNotEmpty()) {
                                IconButton(
                                    onClick = {
                                        textFieldValue.value = ""
                                        onTrailingIconClick?.invoke()
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Clear,
                                        contentDescription = "Clear text"
                                    )
                                }
                            }
                        }
                        LikeLionSearchTopAppBarTextFieldEndIconMode.PASSWORD -> {
                            IconButton(
                                onClick = {
                                    isShowingPasswordFlag = !isShowingPasswordFlag
                                }
                            ) {
                                if (isShowingPasswordFlag) {
                                    // 👁 비밀번호 보이기
                                    Icon(Icons.Filled.Visibility, contentDescription = "Hide password")
                                } else {
                                    // 👁‍🗨 비밀번호 숨기기
                                    Icon(Icons.Filled.VisibilityOff, contentDescription = "Show password")
                                }
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        },
        navigationIcon = {
            IconButton(onClick = { onBackClick() }) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.arrow_back_24px),
                    contentDescription = "뒤로 가기",
                    tint = Color.Black
                )
            }
        },
        actions = {
            menuItems()
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White // 앱바 배경 흰색
        )
    )
}

enum class LikeLionSearchTopAppBarTextFieldEndIconMode{
    NONE,
    TEXT,
    PASSWORD,
}