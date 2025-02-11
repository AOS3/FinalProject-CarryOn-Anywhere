package com.lion.FinalProject_CarryOn_Anywhere.component

import android.R.attr.inputType
import android.R.attr.paddingTop
import android.R.attr.singleLine
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun LikeLionOutlinedTextField(
    // 입력값에 대한 상태관리 변수
    textFieldValue: MutableState<String> = mutableStateOf(""),
    // hint
    label:String = "",
    // 포커스가 주여지고 입력된 내용이 없을 경우 보여줄 안내 문구
    placeHolder:String = "",
    // 입력 제한을 주기위한 정규식
    inputCondition:String? = null,
    // 입력 요소 앞의 아이콘
    leadingIcon: ImageVector? = null,
    modifier: Modifier = Modifier,
    // 우측 끝의 아이콘
    trailingIconMode: LikeLionOutlinedTextFieldEndIconMode = LikeLionOutlinedTextFieldEndIconMode.NONE,
    // 한줄 입력 여부
    singleLine:Boolean = false,
    // 상단 여백
    paddingTop:Dp = 0.dp,
    // 입력 모드
    inputType: LikeLionOutlinedTextFieldInputType = LikeLionOutlinedTextFieldInputType.TEXT,
    // 입력 가능 여부
    readOnly: Boolean = false,
    // 포커싱 관리
    focusRequest: MutableState<FocusRequester>? = null,
    // 입력 요소 하단에 나오는 메세지
    supportText:MutableState<String>? = null,
    // 에러 표시
    isError:MutableState<Boolean> = mutableStateOf(false),
    // 만약 입력에 대한 검사를 체크하는 기능이 필요하다면
    isCheckValue:MutableState<Boolean>? = null,
    // 키보드 옵션 및 동작 추가 (기본값 제공)
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Default),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    // 입력값이 변경될 때 호출되는 콜백 함수
    onTrailingIconClick: (() -> Unit)? = null,
    // interactionSource: MutableInteractionSource? = null,
    onValueChange: (String) -> Unit = {},
) {

    // 비밀번호가 보이는지...
    var isShowingPasswordFlag by rememberSaveable {
        mutableStateOf(false)
    }

    // Modify 생성
    var defaultModifier = modifier.padding(top = paddingTop)
    if(focusRequest != null){
        Modifier.focusRequester(focusRequest.value)
    }

    OutlinedTextField(
        modifier = defaultModifier,
        value = textFieldValue.value,
        label = {
            if (textFieldValue.value.isEmpty()) {
                Text(
                    text = placeHolder,
                    color = MaterialTheme
                        .colorScheme
                        .onSurface
                        .copy(alpha = 0.5f)) // 플레이스홀더 텍스트
            }
        },
        placeholder = null,
        onValueChange = {
            val filteredValue = if (inputCondition == null) {
                // 조건이 없으면 원래 값 그대로 사용
                it
            } else {
                // 정규식으로 필터링
                it.replace(inputCondition.toRegex(), "")
            }

            // 필터링된 값을 상태에 반영
            textFieldValue.value = filteredValue

            // isCheckValue가 null이 아니면 false로 설정
            if (isCheckValue != null) {
                isCheckValue.value = false
            }

            // 필터링된 값을 콜백으로 전달
            onValueChange(filteredValue)
        },
        leadingIcon = if(leadingIcon != null) {
            {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null
                )
            }
        } else {
            null
        },
        trailingIcon = when(trailingIconMode){
            LikeLionOutlinedTextFieldEndIconMode.NONE -> null
            LikeLionOutlinedTextFieldEndIconMode.TEXT -> {
                {
                    if(textFieldValue.value.isNotEmpty()){
                        IconButton(
                            onClick = {
                                textFieldValue.value = ""
                                onTrailingIconClick?.invoke()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Clear,
                                contentDescription = null
                            )
                        }
                    } else {
                        null
                    }
                }
            }
            LikeLionOutlinedTextFieldEndIconMode.PASSWORD -> {
                {
                    IconButton(
                        onClick = {
                            isShowingPasswordFlag = !isShowingPasswordFlag
                        }
                    ) {
                        if(isShowingPasswordFlag){
                            Icon(Icons.Filled.Visibility, contentDescription = null)
                        } else {
                            Icon(Icons.Filled.VisibilityOff, contentDescription = null)
                        }
                    }
                }
            }
        },
        singleLine = singleLine,
        visualTransformation =  if(isShowingPasswordFlag == false && inputType == LikeLionOutlinedTextFieldInputType.PASSWORD){
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        },
        readOnly = readOnly,
        supportingText = if(supportText != null){
            {
                Text(text = supportText.value)
            }
        } else {
            null
        },
        isError = isError.value,
        // keyboardOptions = keyboardOptions, // 키보드 옵션 추가
        keyboardActions = keyboardActions,  // 키보드 동작 추가
        keyboardOptions = if (inputType == LikeLionOutlinedTextFieldInputType.NUMBER) {
            KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        } else {
            KeyboardOptions.Default
        },
        colors =  OutlinedTextFieldDefaults.colors(),
        // interactionSource = interactionSource
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