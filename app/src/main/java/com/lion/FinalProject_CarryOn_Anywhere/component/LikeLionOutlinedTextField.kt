package com.lion.FinalProject_CarryOn_Anywhere.component

import android.R.attr.inputType
import android.R.attr.paddingTop
import android.R.attr.singleLine
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun LikeLionOutlinedTextField(
    // 입력값 상태 관리 변수
    textFieldValue: MutableState<String> = mutableStateOf(""),
    // 라벨
    label: String = "",
    // 플레이스홀더
    placeHolder: String = "",
    // 입력 제한을 위한 정규식
    inputCondition: String? = null,
    // 최대 글자 수 제한
    maxLength: Int = Int.MAX_VALUE,
    // 현재 글자 수 표시 여부
    showCharCount: Boolean = false,
    // 입력 필드 앞 아이콘
    leadingIcon: ImageVector? = null,
    // 모디파이어
    modifier: Modifier = Modifier,
    // 우측 끝 아이콘 (텍스트 삭제, 비밀번호 보기)
    trailingIconMode: LikeLionOutlinedTextFieldEndIconMode = LikeLionOutlinedTextFieldEndIconMode.NONE,
    // 한 줄 입력 여부
    singleLine: Boolean = false,
    // 상단 여백
    paddingTop: Dp = 0.dp,
    // 입력 모드 (텍스트, 비밀번호, 숫자)
    inputType: LikeLionOutlinedTextFieldInputType = LikeLionOutlinedTextFieldInputType.TEXT,
    // 읽기 전용 여부
    readOnly: Boolean = false,
    // 입력 요소 하단의 메시지
    supportText: MutableState<String>? = null,
    // 에러 표시 여부
    isError: MutableState<Boolean> = mutableStateOf(false),
    // 키보드 옵션
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Default),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    // 입력값이 변경될 때 호출되는 콜백
    onTrailingIconClick: (() -> Unit)? = null,
    // 입력값 변경 이벤트
    onValueChange: (String) -> Unit = {},
) {
    // 비밀번호 보기 여부
    var isShowingPasswordFlag by rememberSaveable { mutableStateOf(false) }

    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = paddingTop)
            // 최소 높이 유지
            .heightIn(min = if (singleLine) 56.dp else 120.dp),
        value = textFieldValue.value,
        // 항상 상단에 고정되는 라벨
        label = { Text(label) },
        // 입력값이 없을 때만 첫 줄에 표시되는 플레이스홀더
        placeholder = {
            if (textFieldValue.value.isEmpty()) {
                Text(
                    text = placeHolder,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
        },
        onValueChange = { newValue ->
            val filteredValue = if (inputCondition == null) {
                newValue
            } else {
                newValue.replace(inputCondition.toRegex(), "")
            }

            if (filteredValue.length <= maxLength) {
                textFieldValue.value = filteredValue
                onValueChange(filteredValue)
            }
        },
        singleLine = singleLine,
        readOnly = readOnly,
        visualTransformation = if (!isShowingPasswordFlag && inputType == LikeLionOutlinedTextFieldInputType.PASSWORD) {
            // 비밀번호 숨김
            PasswordVisualTransformation()
        } else {
            // 비밀번호 보이기
            VisualTransformation.None
        },
        isError = isError.value,
        keyboardActions = keyboardActions,
        keyboardOptions = if (inputType == LikeLionOutlinedTextFieldInputType.NUMBER) {
            KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        } else {
            KeyboardOptions.Default
        },
        colors = OutlinedTextFieldDefaults.colors(),
        // 우측 끝 아이콘 (비밀번호 보기 / 텍스트 삭제)
        trailingIcon = {
            when (trailingIconMode) {
                // 아이콘 없음
                LikeLionOutlinedTextFieldEndIconMode.NONE -> {}
                LikeLionOutlinedTextFieldEndIconMode.TEXT -> {
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
                LikeLionOutlinedTextFieldEndIconMode.PASSWORD -> {
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
        supportingText = {
            if (showCharCount) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    // 오른쪽 정렬
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "${textFieldValue.value.length} / $maxLength",
                        color = if (textFieldValue.value.length == maxLength) Color.Red else Color.Gray,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    )
}

enum class LikeLionOutlinedTextFieldEndIconMode{
    NONE,
    TEXT,
    PASSWORD,
}

enum class LikeLionOutlinedTextFieldInputType{
    TEXT,
    PASSWORD,
    NUMBER,
}