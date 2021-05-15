package ru.spbstu.gusev.medicinesstorage.extensions

import android.app.Activity
import android.content.Context
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment

fun Fragment.getColorFromTheme(id: Int): Int {
    val typedValue = TypedValue()
    val theme = this.requireContext().theme
    theme.resolveAttribute(id, typedValue, true)
    return typedValue.data
}

fun Fragment.hideKeyboard() {
    val view: View? = this.activity?.findViewById(android.R.id.content)
    if (view != null) {
        val imm: InputMethodManager =
            this.activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}