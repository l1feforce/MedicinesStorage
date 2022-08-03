package ru.spbstu.gusev.medicinesstorage.utils

import android.content.Context
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import ru.spbstu.gusev.medicinesstorage.ui.theme.MedicinesStorageTheme

fun composeContent(context: Context, content: @Composable () -> Unit): View =
    ComposeView(context).apply {
        setContent { MedicinesStorageTheme { content() } }
    }

inline fun showIf(condition: Boolean, action: () -> Unit) {
    if (condition) action()
}
