package ru.spbstu.gusev.medicinesstorage.ui.animations

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Composable
fun CrossfadeVisibility(
    visible: Boolean,
    content: @Composable() AnimatedVisibilityScope.() -> Unit
) {
    val spring by remember { mutableStateOf(spring<Float>(stiffness = Spring.StiffnessMediumLow)) }
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(spring),
        exit = fadeOut(spring),
        content = content
    )
}