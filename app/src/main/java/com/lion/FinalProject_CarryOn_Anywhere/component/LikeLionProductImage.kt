package com.lion.FinalProject_CarryOn_Anywhere.component

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.lion.FinalProject_CarryOn_Anywhere.R

@Composable
fun LikeLionProductImage(
    imgUrl: String?,
    size: Dp?,
    fixedImage: Int = R.drawable.test1, // ✅ 기본 이미지 변경
    contentScale: ContentScale = ContentScale.Crop,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    // ✅ Glide를 통한 비동기 이미지 로드
    LaunchedEffect(imgUrl) {
        imgUrl?.let {
            Glide.with(context)
                .asBitmap()
                .load(it)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        bitmap = resource
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        bitmap = null
                    }
                })
        }
    }

    if (bitmap != null) {
        Image(
            bitmap = bitmap!!.asImageBitmap(),
            contentDescription = "Loaded Image",
            modifier = modifier
                .size(size ?: 100.dp)
                .clip(shape = androidx.compose.foundation.shape.RoundedCornerShape(10.dp)),
            contentScale = contentScale
        )
    } else {
        // ✅ 기본 이미지 설정
        Image(
            painter = painterResource(id = fixedImage),
            contentDescription = "Default Image",
            modifier = modifier
                .size(size ?: 100.dp)
                .clip(shape = androidx.compose.foundation.shape.RoundedCornerShape(10.dp)),
            contentScale = contentScale
        )
    }
}

@Composable
fun LikeLionProductListView(
    randomImg: List<String?>,
    size: Dp? = 100.dp,
    modifier: Modifier = Modifier
) {
    LazyRow(contentPadding = PaddingValues(horizontal = 20.dp)) {
        items(randomImg) { imgUrl ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(end = 15.dp)
            ) {
                LikeLionProductImage(
                    imgUrl = imgUrl,
                    size = size,
                    modifier = modifier
                )
            }
        }
    }
}
