package com.lion.FinalProject_CarryOn_Anywhere.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.lion.FinalProject_CarryOn_Anywhere.R

val nanumSquareExtraBold = FontFamily(Font(R.font.nanumsquare_4))
val nanumSquareBold = FontFamily(Font(R.font.nanumsquare_3))
val nanumSquareRegular = FontFamily(Font(R.font.nanumsquare_2))
val nanumSquareLight = FontFamily(Font(R.font.nanumsquare_1))


val Typography = Typography(
    // 기본 텍스트
    bodyLarge = TextStyle(
        fontFamily = nanumSquareRegular,
        fontSize = 16.sp
    ),
    bodySmall = TextStyle(
        fontFamily = nanumSquareRegular,
        fontSize = 14.sp
    ),
    // 타이틀 텍스트
    titleLarge = TextStyle(
        fontFamily = nanumSquareBold,
        fontSize = 20.sp
    ),
    titleMedium = TextStyle(
        fontFamily = nanumSquareBold,
        fontSize = 18.sp
    ),
    // 버튼 및 라벨 텍스트
    labelLarge = TextStyle(
        fontFamily = nanumSquareBold,
        fontSize = 16.sp
    ),
    // Extrabold 글씨 - 헤드라인
    headlineMedium = TextStyle(
        fontFamily = nanumSquareExtraBold,
        fontSize = 20.sp
    ),
    headlineSmall  = TextStyle(
        fontFamily = nanumSquareExtraBold,
        fontSize = 16.sp
    )
)