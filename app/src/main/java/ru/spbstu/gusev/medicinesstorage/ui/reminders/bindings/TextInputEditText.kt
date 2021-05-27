package ru.spbstu.gusev.medicinesstorage.ui.reminders.bindings

import android.util.Log
import android.widget.LinearLayout
import androidx.core.widget.doOnTextChanged
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.google.android.material.textfield.TextInputEditText

@BindingAdapter("android:textInt")
fun TextInputEditText.setInt(number: Int) {
    if (number.toString() != this.text.toString()) {
        setText(number.toString())
        setSelection(text?.length ?: 0)
    }
}

@InverseBindingAdapter(attribute = "android:textInt")
fun TextInputEditText.getInt(): Int {
    return this.text.toString().toIntOrNull() ?: 0
}

@BindingAdapter("android:textIntAttrChanged")
fun TextInputEditText.setTextIntListener(attrChange: InverseBindingListener) {
    this.doOnTextChanged { _, _, _, _ ->
        attrChange.onChange()
    }
}