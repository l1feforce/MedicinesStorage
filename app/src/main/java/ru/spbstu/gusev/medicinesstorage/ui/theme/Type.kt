package ru.spbstu.gusev.medicinesstorage.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.font.FontWeight

// Set of Material typography styles to start with

val defaultTypography = Typography()

val Typography = Typography(
    h6 = defaultTypography.h6.copy(fontWeight = FontWeight.Medium)

)