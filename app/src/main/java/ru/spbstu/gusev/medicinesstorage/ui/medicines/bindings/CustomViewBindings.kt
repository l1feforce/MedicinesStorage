package ru.spbstu.gusev.medicinesstorage.ui.medicines.bindings

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.spbstu.gusev.medicinesstorage.R
import ru.spbstu.gusev.medicinesstorage.data.local.medicines.model.Medicine
import ru.spbstu.gusev.medicinesstorage.ui.medicines.adapters.MedicinesAdapter
import ru.spbstu.gusev.medicinesstorage.utils.DateUtil
import java.lang.StringBuilder

@BindingAdapter(value = ["setAdapter", "onItemClickListener"])
fun RecyclerView.setAdapterAndOnItemClickListener(adapter: RecyclerView.Adapter<*>, onClickListener: ItemClickInterface) {
    this.apply {
        setHasFixedSize(true)
        this.adapter = adapter
        (this.adapter as MedicinesAdapter).setOnItemClickListener(onClickListener)
    }
}

@BindingAdapter(value = ["src"])
fun ImageView.bindImageByUrl(url: String) {
    Glide.with(this).load(url).into(this)
}

@BindingAdapter(value = ["date"])
fun TextView.setDaysAndText(date: Long) {
    val formattedString = StringBuilder()
    formattedString.append(DateUtil.formatAsEuDate(date))
    val days = DateUtil.calculateDaysDistance(DateUtil.currentDate(), date)
    formattedString.append(" ${this.resources.getQuantityString(R.plurals.medicine_details_use_until_plurals, days, days)}")
    this.text = formattedString
}

@BindingAdapter(value = ["days"])
fun TextView.setStoreDays(days: Int) {
    this.text = this.resources.getQuantityString(R.plurals.medicine_details_store_after_open_plurals, days, days)
}

interface ItemClickInterface {
    fun onItemClickListener(medicine: Medicine)
}