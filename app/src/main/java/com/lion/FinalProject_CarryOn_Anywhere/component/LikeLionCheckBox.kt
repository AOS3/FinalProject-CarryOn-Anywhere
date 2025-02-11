package com.lion.FinalProject_CarryOn_Anywhere.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.MainColor

@Composable
fun LikeLionCheckBox(
    text:String = "CheckBox",
    checkedValue: MutableState<Boolean> = mutableStateOf(false),
    checkedColor: Color = MainColor,
    uncheckedColor: Color = Color.LightGray,
    paddingTop:Dp = 0.dp,
    fontSize: TextUnit = TextUnit.Unspecified,
    textModifier: Modifier,
    modifier: Modifier,
    onCheckedChange:() -> Unit = {},
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = paddingTop)
    ){
        Checkbox(
            checked = checkedValue.value,
            colors = CheckboxDefaults.colors(
                checkedColor = checkedColor,
                uncheckedColor = uncheckedColor
            ),
            modifier = modifier,
            onCheckedChange = {
                checkedValue.value = it
                onCheckedChange()
            }
        )

        Text(
            modifier = textModifier.padding().clickable {
                checkedValue.value = !checkedValue.value
                onCheckedChange()
            },
            text = text,
            fontSize = fontSize,
        )
    }
}