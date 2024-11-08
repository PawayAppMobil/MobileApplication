// In a new file, e.g., Typography.kt
package com.paway.mobileapplication.user.presentation

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.paway.mobileapplication.R

val Baskerville = FontFamily(
    Font(R.font.baskerville_regular, FontWeight.Normal),
    Font(R.font.baskerville_italic, FontWeight.Normal),
    Font(R.font.baskerville_bold, FontWeight.Bold)
)

val CustomTypography = Typography(
    titleMedium = TextStyle(
        fontFamily = Baskerville,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = Baskerville,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp
    )
    // Add other text styles as needed
)