package com.civilcam.ext_features.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.civilcam.ext_features.R


data class CCTypography internal constructor(
    val notification: TextStyle = TextStyle(
        fontSize = 12.sp,
        lineHeight = 14.sp,
        fontFamily = FontFamily(Font(R.font.roboto_regular)),
        fontWeight = FontWeight.W400,
        color = Color(0xFFFFFFFF),
        letterSpacing = (-0.5).sp,
    ),

    val common_text_regular_12: TextStyle = TextStyle(
        fontSize = 12.sp,
        lineHeight = 14.sp,
        fontFamily = FontFamily(Font(R.font.roboto_regular)),
        fontWeight = FontWeight.W400,
        color = Color(0xFFFFFFFF),
        letterSpacing = (-0.2).sp,
    ),

    val common_error_text_regular: TextStyle = TextStyle(
        fontSize = 12.sp,
        lineHeight = 14.sp,
        fontFamily = FontFamily(Font(R.font.roboto_regular)),
        fontWeight = FontWeight.W400,
        color = Color(0xFFCF3919),
        letterSpacing = (-0.01).sp,
    ),

    val common_text_small_regular: TextStyle = TextStyle(
        fontSize = 13.sp,
        lineHeight = 15.sp,
        fontFamily = FontFamily(Font(R.font.roboto_regular)),
        fontWeight = FontWeight.W400,
        letterSpacing = (-0.01).sp,
    ),
    val common_text_small_regular_spacing: TextStyle = TextStyle(
        fontSize = 13.sp,
        lineHeight = 15.sp,
        fontFamily = FontFamily(Font(R.font.roboto_regular)),
        fontWeight = FontWeight.W400,
        letterSpacing = (-0.5).sp,
    ),

    val common_text_small_medium: TextStyle = TextStyle(
        fontSize = 13.sp,
        lineHeight = 15.sp,
        fontFamily = FontFamily(Font(R.font.roboto_medium)),
        fontWeight = FontWeight.W500,
        letterSpacing = (-0.01).sp,
    ),

    val common_text_regular: TextStyle = TextStyle(
        fontSize = 15.sp,
        lineHeight = 18.sp,
        fontFamily = FontFamily(Font(R.font.roboto_regular)),
        fontWeight = FontWeight.W400,
        letterSpacing = (-0.01).sp,
        color = Color(0xFF111111)
    ),

    val common_text_regular_error: TextStyle = TextStyle(
        fontSize = 15.sp,
        lineHeight = 18.sp,
//        fontFamily = FontFamily(Font(R.font.roboto_regular)),
        fontWeight = FontWeight.W400,
        letterSpacing = (-0.01).sp,
        color = Color(0xFFCF3919)
    ),

    val common_text_medium: TextStyle = TextStyle(
        fontSize = 15.sp,
        lineHeight = 18.sp,
        fontFamily = FontFamily(Font(R.font.roboto_medium)),
        fontWeight = FontWeight.W500,
        letterSpacing = (-0.01).sp,
        color = Color(0xFF111111)
    ),

    val button_text: TextStyle = TextStyle(
        fontSize = 17.sp,
        lineHeight = 20.sp,
        fontFamily = FontFamily(Font(R.font.roboto_bold)),
        fontWeight = FontWeight.W600,
        letterSpacing = (-0.01).sp,
    ),

    val common_medium_text_regular: TextStyle = TextStyle(
        fontSize = 17.sp,
        lineHeight = 20.sp,
        fontFamily = FontFamily(Font(R.font.roboto_regular)),
        fontWeight = FontWeight.W400,
        letterSpacing = (-0.01).sp,
    ),

    val common_medium_text_bold: TextStyle = TextStyle(
        fontSize = 17.sp,
        lineHeight = 20.sp,
        fontFamily = FontFamily(Font(R.font.roboto_bold)),
        fontWeight = FontWeight.W600,
        letterSpacing = (-0.01).sp,
    ),

    val emergency_button: TextStyle = TextStyle(
        fontSize = 24.sp,
        lineHeight = 28.sp,
        fontFamily = FontFamily(Font(R.font.roboto_bold)),
        fontWeight = FontWeight.W700,
        letterSpacing = (-0.01).sp,
    ),

    val big_title: TextStyle = TextStyle(
        fontSize = 28.sp,
        lineHeight = 34.sp,
//        fontFamily = FontFamily(Font(R.font.roboto_regular)),
        fontWeight = FontWeight.W700,
        letterSpacing = (-0.01).sp,
    ),
)