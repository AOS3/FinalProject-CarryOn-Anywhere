package com.lion.FinalProject_CarryOn_Anywhere.ui.screen.trip

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState
import com.lion.FinalProject_CarryOn_Anywhere.R
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionGoogleMap
import com.lion.FinalProject_CarryOn_Anywhere.component.LikeLionTopAppBar
import com.lion.FinalProject_CarryOn_Anywhere.ui.viewmodel.trip.TripInfoViewModel

@Composable
fun ShowTripMapScreen(
    tripInfoViewModel: TripInfoViewModel = hiltViewModel(),
    tripDocumentId: String
) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(tripInfoViewModel.selectedPlaceLocation.value, 8f)
    }

    Scaffold(
        topBar = {
            LikeLionTopAppBar(
                title = "지도 상세 보기",
                navigationIconImage = ImageVector.vectorResource(R.drawable.arrow_back_24px),
                navigationIconOnClick = {
                    tripInfoViewModel.showMapNavigationOnClick(tripDocumentId)
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(it)
        ) {
            LikeLionGoogleMap(
                cameraPositionState,
                selectedPlaces = tripInfoViewModel.selectedPlaces
            )
        }
    }
}