package com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.home

import android.content.Context
import androidx.lifecycle.ViewModel
import com.lion.FinalProject_CarryOn_Anywhere.CarryOnApplication

import com.lion.FinalProject_CarryOn_Anywhere.data.server.util.ScreenName
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class PlaceInfoViewModel @Inject constructor(
    @ApplicationContext context: Context
) : ViewModel()  {

    val carryOnApplication = context as CarryOnApplication

    private val _placeDetail = MutableStateFlow<PlaceList?>(null)
    val placeDetail: StateFlow<PlaceList?> = _placeDetail

    // Back 버튼 동작 메서드
    fun navigationBackIconOnClick() {
        carryOnApplication.navHostController.popBackStack(
            ScreenName.PLACE_INFO_SCREEN.name,
            inclusive = true
        )
//        carryOnApplication.navHostController.navigate(ScreenName.MAIN_SCREEN.name) {
//            launchSingleTop = true
//        }
    }

    // 장소 데이터 설정
    fun settingPlaceInfo(title: String, placeSearchViewModel: PlaceSearchViewModel) {
        val place = placeSearchViewModel._places.value
            .find { it.title == title }

        _placeDetail.value = place
    }


}