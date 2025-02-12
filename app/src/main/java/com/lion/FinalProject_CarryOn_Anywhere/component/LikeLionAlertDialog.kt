package com.lion.FinalProject_CarryOn_Anywhere.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.dp
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.MainColor
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.SubTextColor
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.Typography


@Composable
fun LikeLionAlertDialog(
    // 다이얼로그를 보여주는 상태
    showDialogState : MutableState<Boolean>,
    confirmButtonTitle : String = "확인",
    confirmButtonOnClick : () -> Unit = {
        showDialogState.value = false
    },
    dismissButtonTitle : String? = null,
    dismissButtonOnClick : () -> Unit = {
        showDialogState.value = false
    },
    confirmContainerColor: Color = MainColor,
    confirmContentColor: Color = Color.White,
    confirmButtonModifier: Modifier = Modifier,
    dismissContainerColor: Color = Color.Transparent,
    dismissContentColor: Color = MainColor,
    dismissButtonModifier: Modifier = Modifier, // 기본값 설정
    dismissBorder: BorderStroke = BorderStroke(1.dp, MainColor),
    titleAlign : TextAlign? = null,
    textModifier: Modifier = Modifier,
    titleModifier: Modifier = Modifier,
    textAlign: TextAlign? = null,
    icon : ImageVector? = null,
    title : String? = null,
    text : String? = null,
) {
    if(showDialogState.value){
        AlertDialog(
            modifier = Modifier.fillMaxWidth(),
            onDismissRequest = {
                showDialogState.value = false
            },
            // 확인 및 취소 버튼을 한 줄에 배치
            confirmButton = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly // 동일 간격 배치
                ) {
                    // 취소 버튼
                    if (dismissButtonTitle != null) {
                        LikeLionFilledButton(
                            modifier = dismissButtonModifier,
                            containerColor = dismissContainerColor,
                            contentColor = dismissContentColor,
                            text = dismissButtonTitle,
                            border = dismissBorder,
                            onClick = dismissButtonOnClick,
                            paddingTop = TODO(),
                            paddingBottom = TODO(),
                            horizontalPadding = TODO(),
                            fillWidth = TODO(),
                            buttonWidth = TODO(),
                            buttonHeight = TODO(),
                            cornerRadius = TODO(),
                            isEnabled = TODO(),
                            icon = TODO()
                        )
                    }
                    // 확인 버튼
                    LikeLionFilledButton(
                        modifier = confirmButtonModifier,

                        containerColor = confirmContainerColor,

                        contentColor = confirmContentColor,
                        text = confirmButtonTitle,
                        onClick = confirmButtonOnClick,
                        border = TODO(),
                        paddingTop = TODO(),
                        paddingBottom = TODO(),
                        horizontalPadding = TODO(),
                        fillWidth = TODO(),
                        buttonWidth = TODO(),
                        buttonHeight = TODO(),
                        cornerRadius = TODO(),
                        isEnabled = TODO(),
                        icon = TODO()
                    )
                }
            },
            icon = if (icon != null) {
                {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                    )
                }
            } else {
                null
            },
            title = if (title != null) {
                {
                    Text(
                        text = title,
                        textAlign = titleAlign,
                        style = Typography.titleMedium,
                        modifier = titleModifier
                    )
                }
            } else {
                null
            },
            text = if (text != null) {
                {
                    Text(
                        text = text,
                        textAlign = textAlign,
                        modifier = textModifier,
                        color = SubTextColor,
                        style = Typography.bodyLarge
                    )
                }
            } else {
                null
            },
        )
    }
}
