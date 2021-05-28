package ru.spbstu.gusev.medicinesstorage.ui.medicines.bindings

import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.annotation.ArrayRes
import androidx.databinding.BindingAdapter
import ru.spbstu.gusev.medicinesstorage.R

@BindingAdapter("android:adapterItems")
fun AutoCompleteTextView.setAdapterItems(@ArrayRes stringArrayRes: Int) {
    Log.d("test", "setItems: inside")
    val adapter = ArrayAdapter(
        this.context,
        R.layout.support_simple_spinner_dropdown_item,
        resources.getStringArray(stringArrayRes)
    )
    setAdapter(adapter)
}