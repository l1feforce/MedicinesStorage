package ru.spbstu.gusev.medicinesstorage.ui.reminders.bindings

import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("setEndIconClickListener")
fun TextInputLayout.setEndIconClickListener(listener: () -> Unit) {
    setEndIconOnClickListener { listener.invoke() }
}