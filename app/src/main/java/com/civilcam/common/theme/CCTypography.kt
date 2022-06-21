package com.civilcam.common.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp


data class CCTypography internal constructor(


    val big_title: TextStyle = TextStyle(
        fontSize = 28.sp,
        lineHeight = 34.sp,
//        fontFamily = FontFamily(Font(R.font.roboto_regular)),
        fontWeight = FontWeight.W700,
        letterSpacing = (-0.01).sp,
    ),

    val common_text_regular: TextStyle = TextStyle(
        fontSize = 15.sp,
        lineHeight = 18.sp,
//        fontFamily = FontFamily(Font(R.font.roboto_regular)),
        fontWeight = FontWeight.W400,
        letterSpacing = (-0.01).sp,
        color = Color(0xFF111111)
    ),

    val common_text_medium: TextStyle = TextStyle(
        fontSize = 15.sp,
        lineHeight = 18.sp,
//        fontFamily = FontFamily(Font(R.font.roboto_regular)),
        fontWeight = FontWeight.W500,
        letterSpacing = (-0.01).sp,
        color = Color(0xFF111111)
    ),

    val button_text: TextStyle = TextStyle(
        fontSize = 17.sp,
        lineHeight = 20.sp,
//        fontFamily = FontFamily(Font(R.font.roboto_regular)),
        fontWeight = FontWeight.W600,
        letterSpacing = (-0.01).sp,
    ),

    val common_text_small_regular: TextStyle = TextStyle(
        fontSize = 13.sp,
        lineHeight = 15.sp,
//        fontFamily = FontFamily(Font(R.font.roboto_regular)),
        fontWeight = FontWeight.W400,
        letterSpacing = (-0.01).sp,
    ),

    val common_text_small_medium: TextStyle = TextStyle(
        fontSize = 13.sp,
        lineHeight = 15.sp,
//        fontFamily = FontFamily(Font(R.font.roboto_regular)),
        fontWeight = FontWeight.W500,
        letterSpacing = (-0.01).sp,
    ),
)