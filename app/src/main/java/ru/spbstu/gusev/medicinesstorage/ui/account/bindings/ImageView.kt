package ru.spbstu.gusev.medicinesstorage.ui.account.bindings

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("imageUrl")
fun ImageView.setImageByUrl(url: String) {
    if (url.isNotBlank())
        Glide.with(this).load(url).into(this)

}