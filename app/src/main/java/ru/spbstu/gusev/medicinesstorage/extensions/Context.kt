package ru.spbstu.gusev.medicinesstorage.extensions

import android.content.Context
import android.util.TypedValue
import androidx.fragment.app.Fragment

fun Context.getColorFromTheme(id: Int): Int {
    val typedValue = TypedValue()
    val theme = this.theme
    theme.resolveAttribute(id, typedValue, true)
    return typedValue.data
}