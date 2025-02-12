package com.lion.FinalProject_CarryOn_Anywhere.component

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.AudioProfile
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.lion.FinalProject_CarryOn_Anywhere.R


@SuppressLint("UnrememberedMutableState")
@Composable
fun LikeLionProfileImg(
    imgUrl: String = "",
    modifier: Modifier = Modifier,
    iconTint: Color,
    profileBack: Color,
    profileSize:Dp = 50.dp,
    contentScale: ContentScale = ContentScale.Fit,
    offsetX: Dp? = null,
    offsetY: Dp? = null
){
    // 이미지 비트맵
    val bitmap : MutableState<Bitmap?> = mutableStateOf(null)


    var imageModifier = modifier
        .size(profileSize)
//        .clip(RoundedCornerShape(10.dp))  // 사각형에 라운드 주는거
        .clip(CircleShape)  // 원형으로 만드는거
    var circleModifier = modifier

    if (offsetX != null || offsetY != null){
        circleModifier = circleModifier.offset(offsetX?: 0.dp,offsetY?: 0.dp)
    }


    Glide.with(LocalContext.current)
        .asBitmap() // 뭘로 변활 할 것?
        .load(imgUrl) // 어디서 가지고 올 것?
        .into(object : CustomTarget<Bitmap>(){  // 어디에 넣을 것?
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                // resource가 다운 받은 이미지
                bitmap.value = resource
            }

            override fun onLoadCleared(placeholder: Drawable?) {

            }
        })


    Column(
        modifier = circleModifier
            .size(profileSize + 10.dp).clip(CircleShape).background(profileBack),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {

        // bitmap에 데이터가 있다면? -> 이미지를 다운 받았다면
        bitmap.value?.asImageBitmap()?.let {
            Image(bitmap = it,
                contentScale = ContentScale.Crop,
                contentDescription = null,
                modifier = imageModifier
                    .fillMaxSize()
            )
        } ?: Image(
            painter = painterResource(id = R.drawable.ic_empty_person_24), // 다운 받은 이미지가 없는 경우
            contentScale = contentScale,
            contentDescription = null,
            modifier = imageModifier,
            colorFilter = ColorFilter.tint(iconTint)
        )
    }
}