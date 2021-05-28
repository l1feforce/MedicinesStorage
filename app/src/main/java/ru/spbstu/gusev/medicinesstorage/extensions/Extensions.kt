package ru.spbstu.gusev.medicinesstorage.extensions

import android.view.View
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar

fun showSnackbar(anchorView: View, @StringRes text: Int) = Snackbar.make(
    anchorView,
    text,
    Snackbar.LENGTH_LONG
).apply {
    setAnchorView(anchorView)
}.show()