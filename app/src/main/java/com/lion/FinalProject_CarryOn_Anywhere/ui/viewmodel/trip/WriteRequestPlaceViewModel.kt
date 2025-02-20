package com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.trip

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.lion.FinalProject_CarryOn_Anywhere.CarryOnApplication
import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.ScreenName
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class WriteRequestPlaceViewModel @Inject constructor(
    @ApplicationContext context: Context
): ViewModel() {

    val carryOnApplication = context as CarryOnApplication

    val textFieldPlaceName = mutableStateOf("")

    val textFieldAddress = mutableStateOf("")

    val showAddressSearch = mutableStateOf(false)

    val requestPlaceDialogState = mutableStateOf(false)

    // 장소 등록 요청 에서 뒤로가기 눌렀을 때
    fun requestPlaceNavigationOnClick(){
        carryOnApplication.navHostController.popBackStack(ScreenName.WRITE_REQUEST_PLACE.name, true)
        carryOnApplication.navHostController.navigate(ScreenName.TRIP_SEARCH_PLACE.name)
    }
}