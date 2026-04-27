package com.lion.FinalProject_CarryOn_Anywhere.ui.screen.trip

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.lion.FinalProject_CarryOn_Anywhere.R
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionAddressSearchWebView
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionAlertDialog
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionFilledButton
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionIconButton
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionOutlinedTextField
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionOutlinedTextFieldEndIconMode
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionTopAppBar
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.GrayColor
import com.lion.FinalProject_CarryOn_Anywhere.ui.theme.MainColor
import com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.trip.TripSearchPlaceViewModel

@Composable
fun WriteRequestPlaceScreen(
    tripSearchPlaceViewModel: TripSearchPlaceViewModel = hiltViewModel(),
) {
    val interactionSource = remember { MutableInteractionSource() }

    Scaffold(
        topBar = {
            LikeLionTopAppBar(
                title = "장소 등록 요청하기",
                navigationIconImage = ImageVector.vectorResource(R.drawable.arrow_back_24px),
                navigationIconOnClick = {
                    tripSearchPlaceViewModel.requestPlaceNavigationOnClick()
                },
                menuItems = {
                    LikeLionIconButton(
                        icon = ImageVector.vectorResource(R.drawable.send_24px),
                        iconButtonOnClick = {
                            tripSearchPlaceViewModel.requestPlaceDialogState.value = true
                        },
                        iconColor = MainColor
                    )
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(it)
                .padding(horizontal = 20.dp)
        ) {
            Text(
                text = "장소 등록을 요청하시면 캐리온에서 검토 후 새로운 장소를 추가 할 수 있습니다.",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Black,
                modifier = Modifier.padding(top = 15.dp, bottom = 15.dp)
            )

            Text(
                text = "ㆍ장소 이름과 간단한 정보를 입력해주세요",
                style = MaterialTheme.typography.bodySmall,
                color = GrayColor,
                modifier = Modifier.padding(top = 15.dp, bottom = 5.dp)
            )

            Text(
                text = "ㆍ요청이 접수되면 캐리온(CarryOn)에서 검토를 진행합니다.",
                style = MaterialTheme.typography.bodySmall,
                color = GrayColor,
                modifier = Modifier.padding(bottom = 5.dp)
            )

            Text(
                text = "ㆍ승인된 장소는 서비스에 추가됩니다.",
                style = MaterialTheme.typography.bodySmall,
                color = GrayColor,
                modifier = Modifier.padding(bottom = 15.dp)
            )

            LikeLionOutlinedTextField(
                textFieldValue = tripSearchPlaceViewModel.textFieldPlaceName,
                showCharCount = true,
                maxLength = 30,
                singleLine = true,
                label = "장소 이름",
                placeHolder = "장소 이름 입력",
                inputCondition = "[^a-zA-Z0-9ㄱ-ㅎㅏ-ㅣ가-힣]",
                trailingIconMode = LikeLionOutlinedTextFieldEndIconMode.TEXT,
                onTrailingIconClick = {
                    tripSearchPlaceViewModel.textFieldPlaceName.value = ""
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 15.dp)
            )

            LikeLionOutlinedTextField(
                textFieldValue = tripSearchPlaceViewModel.textFieldAddress,
                singleLine = true,
                label = "주소",
                placeHolder = "주소를 검색해주세요.",
                trailingIconMode = LikeLionOutlinedTextFieldEndIconMode.TEXT,
                readOnly = true, // 입력 비활성화
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 15.dp)
            )

            LikeLionFilledButton(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalPadding = 0.dp,
                text = "주소 검색",
                cornerRadius = 5,
                onClick = {
                    tripSearchPlaceViewModel.showAddressSearch.value = true
                }
            )

            // 주소 검색 다이얼로그 (showAddressSearch를 직접 사용)
            if (tripSearchPlaceViewModel.showAddressSearch.value) {
                LikeLionAddressSearchWebView(
                    context = LocalContext.current,
                    onAddressSelected = { address ->
                        tripSearchPlaceViewModel.textFieldAddress.value = address
                        tripSearchPlaceViewModel.showAddressSearch.value = false // 주소 선택 후 다이얼로그 닫기
                    },
                    onClosed = { tripSearchPlaceViewModel.showAddressSearch.value = false }
                )
            }

            // 장소 요청 완료 다이얼로그
            LikeLionAlertDialog(
                showDialogState = tripSearchPlaceViewModel.requestPlaceDialogState,
                confirmButtonTitle = "확인",
                confirmButtonModifier = Modifier
                    .weight(1f)
                    .padding(start = 10.dp),
                confirmButtonOnClick = {
                    tripSearchPlaceViewModel.addPlaceRequst()
                    tripSearchPlaceViewModel.requestPlaceDialogState.value = false
                },
                dismissButtonTitle = "취소",
                dismissContainerColor = Color.Transparent,
                dismissButtonModifier = Modifier
                    .weight(1f)
                    .padding(end = 10.dp),
                dismissBorder = BorderStroke(1.dp, MainColor),
                dismissButtonOnClick = {
                    tripSearchPlaceViewModel.requestPlaceDialogState.value = false
                },
                title = "장소 등록 요청 완료",
                titleModifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                titleAlign = TextAlign.Center,
                text = "캐리온에서 검토 후 장소가 등록됩니다.",
                textModifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                textAlign = TextAlign.Center,
            )
        }
    }
}
