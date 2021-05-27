package ru.spbstu.gusev.medicinesstorage.ui.reminders.bindings

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("srcRes")
fun ImageView.setPhoto(id: Int) {
    setImageResource(id)//Glide.with(this).load(id).into(this)
}