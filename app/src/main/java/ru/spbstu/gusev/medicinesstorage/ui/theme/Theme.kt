package ru.spbstu.gusev.medicinesstorage.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController


private val LightColorPalette = lightColors(
    primary = Green500,
    primaryVariant = Green700,
    secondary = Pink200,
    secondaryVariant = Pink700,
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    error = Red700,
    onError = Color.White
)

@Composable
fun MedicinesStorageTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = LightColorPalette
    val systemUiController = rememberSystemUiController()

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )

    systemUiController.setStatusBarColor(Color.White, true)

}