package com.lion.FinalProject_CarryOn_Anywhere.component

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
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
    markerTitle: List<String> = listOf(),
    markerSnippet: List<String> = listOf(),
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
            selectedPlaces.forEachIndexed  { index, location ->
                Marker(
                    state = MarkerState(position = location),
                    title = markerTitle.getOrNull(index) ?: "장소 ${index + 1}",
                    snippet = markerSnippet.getOrNull(index) ?: "여행 장소",
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