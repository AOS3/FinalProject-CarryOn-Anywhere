package com.lion.FinalProject_CarryOn_Anywhere.component

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.lion.FinalProject_CarryOn_Anywhere.R


@SuppressLint("UnrememberedMutableState")
@Composable
fun LikeLionProductImage(
    imgUrl: String,
    size:Dp?,
    // 로고 이미지 -> 추후 수정
    fixedImage : Int = R.drawable.ic_launcher_background,
    contentScale: ContentScale = ContentScale.Inside,
    modifier: Modifier = Modifier
) {
    // 이미지 비트맵

    //val bitmap : MutableState<Bitmap?> = remember {mutableStateOf(null)}
    val bitmap : MutableState<Bitmap?> = mutableStateOf(null)
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
    if (bitmap.value == null){
        Image(bitmap = ImageBitmap.imageResource(fixedImage)
            , contentScale = contentScale
            , contentDescription = null
            , modifier = if(size != null) modifier
                .size(size).clip(RectangleShape)
            else
                modifier.clip(RectangleShape)
        )
    }
    else{

        // bitmap에 데이터가 있다면? -> 이미지를 다운 받았다면
        bitmap.value?.asImageBitmap()?.let {
            Image(bitmap = it
                , contentScale = contentScale
                , contentDescription = null
                , modifier = if(size != null) modifier
                    .size(size).clip(RectangleShape)
                else
                    modifier.clip(RectangleShape)
            )
        } ?: Image(bitmap = ImageBitmap.imageResource(fixedImage)
            , contentScale = contentScale
            , contentDescription = null
            , modifier = if(size != null) modifier
                .size(size).clip(RectangleShape)
            else
                modifier.clip(RectangleShape)
        )
    }

}

@Composable
fun LikeLionProductListView(
    randomImg: List<String>,
    size:Dp?,
    modifier: Modifier
){
    // LazyColumn은 RecyclerView와 유사하다.
    LazyRow(
        contentPadding = PaddingValues(start = 25.dp, end = 20.dp)
    ){
        items(randomImg.size) { idx ->
            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                LikeLionProductImage(
                    randomImg[idx],
                    size = size,
                    modifier = modifier
                )
                Spacer(Modifier.width(15.dp))
            }

        }
    }
}