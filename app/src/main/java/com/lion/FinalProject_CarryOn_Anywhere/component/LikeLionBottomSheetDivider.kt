package com.lion.FinalProject_CarryOn_Anywhere.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LikeLionBottomSheetDivider(
    onDismissRequest: () -> Unit,
    text1: String = "텍스트1",
    text1Color: Color = Color.Black,
    text1OnClick: () -> Unit = { },
    text2: String = "텍스트2",
    text2Color: Color = Color.Black,
    text2OnClick: () -> Unit = { },
    text3: String = "텍스트3",
    text3Color: Color = Color.Black,
    text3OnClick: () -> Unit = { },
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
                    .clickable {
                        text1OnClick()
                    },
                textAlign = TextAlign.Center,
                text = text1,
                color = text1Color,
                style = MaterialTheme.typography.bodyLarge
            )

            if (text2 != "텍스트2") {
                LikeLionDivider(
                    modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 5.dp, bottom = 15.dp),
                    color = Color.LightGray,
                    thickness = 1.dp
                )

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp)
                        .clickable {
                            text2OnClick()
                        },
                    textAlign = TextAlign.Center,
                    text = text2,
                    color = text2Color,
                    style = MaterialTheme.typography.bodyLarge
                )
            } else {
                Spacer(modifier = Modifier.padding(top = 10.dp))
            }

            if (text3 != "텍스트3") {
                LikeLionDivider(
                    modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 15.dp),
                    color = Color.LightGray,
                    thickness = 1.dp
                )

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp)
                        .clickable {
                            text3OnClick()
                        },
                    textAlign = TextAlign.Center,
                    text = text3,
                    color = text3Color,
                    style = MaterialTheme.typography.bodyLarge
                )
            } else {
                Spacer(modifier = Modifier.padding(top = 10.dp))
            }
        }
    }
}