package ru.spbstu.gusev.medicinesstorage.ui.reminders.bindings

import android.widget.AutoCompleteTextView
import androidx.core.widget.doOnTextChanged
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener

@BindingAdapter("android:textInt")
fun AutoCompleteTextView.setInt(number: Int) {
    if (number.toString() != this.text.toString()) {
        setText(number.toString())
        setSelection(text?.length ?: 0)
    }
}

@InverseBindingAdapter(attribute = "android:textInt")
fun AutoCompleteTextView.getInt(): Int {
    return this.text.toString().toIntOrNull() ?: 0
}

@BindingAdapter("android:textIntAttrChanged")
fun AutoCompleteTextView.setTextIntListener(attrChange: InverseBindingListener) {
    this.doOnTextChanged { _, _, _, _ ->
        attrChange.onChange()
    }
}