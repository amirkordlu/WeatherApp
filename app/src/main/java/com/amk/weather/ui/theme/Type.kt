package com.amk.weather.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.amk.weather.R

// Set of Material typography styles to start with
val Typography = Typography(
    body1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )
    /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
)

val interRegular = FontFamily(Font(R.font.inter_regular))
val interBold = FontFamily(Font(R.font.inter_bold))
val interLight = FontFamily(Font(R.font.inter_light))
val interMedium = FontFamily(Font(R.font.inter_medium))
val interSemiBold = FontFamily(Font(R.font.inter_semibold))
val nahidFont = FontFamily(Font(R.font.nahid))
val todayStyle = TextStyle(
    fontFamily = FontFamily(Font(R.font.inter_bold)),
    fontSize = 14.sp,
    color = Color(49, 51, 65)
)
val tomorrowStyle = TextStyle(
    fontFamily = FontFamily(Font(R.font.inter_regular)),
    fontSize = 14.sp,
    color = Color(214, 153, 107)
)