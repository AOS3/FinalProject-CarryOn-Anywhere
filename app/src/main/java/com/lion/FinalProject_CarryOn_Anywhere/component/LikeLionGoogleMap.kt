package com.lion.FinalProject_CarryOn_Anywhere.component

import android.location.Location
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline

@Composable
fun LikeLionGoogleMap(
    cameraPositionState: CameraPositionState,
    selectedPlaces: List<LatLng>, // ✅ 선택된 장소 리스트
    modifier: Modifier = Modifier,
    onMapClick: () -> Unit = {},
    isAddTripPlan: Boolean = false
) {
    Column(modifier = modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.weight(1f),
            cameraPositionState = cameraPositionState,
            onMapClick = {
                if (isAddTripPlan) {
                    onMapClick()
                }
            }
        ) {
            // ✅ 선택된 장소마다 마커 추가
            selectedPlaces.forEach { location ->
                Marker(
                    state = MarkerState(position = location),
                    title = "여행 장소",
                    snippet = "선택한 장소"
                )
            }

            // ✅ 마커들을 연결하는 선 (Polyline)
            if (selectedPlaces.size > 1) {
                Polyline(
                    points = selectedPlaces, // ✅ 추가된 장소를 따라 선을 그림
                    color = Color.Blue, // ✅ 선 색깔 설정
                    width = 5f // ✅ 선 두께 설정
                )
            }
        }
    }
}