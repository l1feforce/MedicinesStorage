package ru.spbstu.gusev.medicinesstorage.extensions

import android.view.View
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import ru.spbstu.gusev.medicinesstorage.R
import java.util.*

fun showSnackbar(anchorView: View, @StringRes text: Int) = Snackbar.make(
    anchorView,
    text,
    Snackbar.LENGTH_LONG
).apply {
    setAnchorView(anchorView)
}.show()

fun showErrorSnackbar(view: View, @StringRes text: Int) = Snackbar.make(
    view,
    text,
    Snackbar.LENGTH_LONG
).apply {
    setTextColor(view.context.getColorFromTheme(R.attr.colorOnError))
    setBackgroundTint(view.context.getColorFromTheme(R.attr.colorError))
}.show()

fun showErrorSnackbar(view: View, text: String) = Snackbar.make(
    view,
    text,
    Snackbar.LENGTH_LONG
).apply {
    setTextColor(view.context.getColorFromTheme(R.attr.colorOnError))
    setBackgroundTint(view.context.getColorFromTheme(R.attr.colorError))
}.show()

fun generateUid(): Int =
    UUID.randomUUID().toString().hashCode()