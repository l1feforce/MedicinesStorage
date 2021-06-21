package ru.spbstu.gusev.medicinesstorage.ui.medicines.bindings

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import ru.spbstu.gusev.medicinesstorage.R
import ru.spbstu.gusev.medicinesstorage.models.Medicine
import ru.spbstu.gusev.medicinesstorage.ui.medicines.adapters.ItemClickInterface
import ru.spbstu.gusev.medicinesstorage.ui.medicines.adapters.OnClickListenerAdapter
import ru.spbstu.gusev.medicinesstorage.utils.DateUtil
import kotlin.math.abs
import kotlin.math.round

@BindingAdapter(value = ["setAdapter", "onItemClickListener"])
fun RecyclerView.setAdapterAndOnItemClickListener(
    adapter: RecyclerView.Adapter<*>,
    onClickListener: ItemClickInterface
) {
    this.apply {
        this.adapter = adapter
        (this.adapter as OnClickListenerAdapter).setOnItemClickListener(onClickListener)
    }
}

@BindingAdapter(value = ["src"])
fun ImageView.bindImageByUrl(url: String) {
    if (url.isBlank()) this.setImageResource(R.drawable.ic_medication_package)
    else Glide.with(this).load(url).into(this)
}

@BindingAdapter(value = ["date"])
fun TextView.setDaysAndText(date: Long) {
    val formattedString = StringBuilder()
    formattedString.append(DateUtil.formatAsEuDate(date))
    val days = DateUtil.calculateDaysDistance(DateUtil.currentDate(), date)
    when (days) {
        in -3650..-365 -> {
            formattedString.append(
                " ${
                    this.resources.getQuantityString(
                        R.plurals.medicine_details_use_until_expired_plurals_years,
                        round(abs(days) / 365.0).toInt(),
                        round(abs(days) / 365.0).toInt()
                    )
                }"
            )
        }
        in -364..-32 -> {
            formattedString.append(
                " ${
                    this.resources.getQuantityString(
                        R.plurals.medicine_details_use_until_expired_plurals_months,
                        abs(days) / 30,
                        abs(days) / 30
                    )
                }"
            )
        }
        in -31..0 -> {
            formattedString.append(
                " ${
                    this.resources.getQuantityString(
                        R.plurals.medicine_details_use_until_expired_plurals_days,
                        abs(days),
                        abs(days)
                    )
                }"
            )
        }
        in 0..31 -> {
            formattedString.append(
                " ${
                    this.resources.getQuantityString(
                        R.plurals.medicine_details_use_until_plurals_days,
                        days,
                        days
                    )
                }"
            )
        }
        in 32..364 -> {
            formattedString.append(
                " ${
                    this.resources.getQuantityString(
                        R.plurals.medicine_details_use_until_plurals_months,
                        days / 30,
                        days / 30
                    )
                }"
            )
        }
        else -> {
            formattedString.append(
                " ${
                    this.resources.getQuantityString(
                        R.plurals.medicine_details_use_until_plurals_years,
                        round(days / 365.0).toInt(),
                        round(days / 365.0).toInt()
                    )
                }"
            )
        }
    }
    this.text = formattedString
}

@BindingAdapter(value = ["days"])
fun TextView.setStoreDays(days: Int) {
    if (days < 1) this.text = " - "
    else this.text = this.resources.getQuantityString(
        R.plurals.medicine_details_store_after_open_plurals,
        days,
        days
    )
}

@BindingAdapter(value = ["volume"])
fun TextView.setVolume(medicine: Medicine) {
    this.text = "${medicine.residue}/${medicine.volume}"
    if (medicine.residue.toDouble() / medicine.volume <= 0.3) this.setTextColor(
        this.resources.getColor(
            R.color.red_error
        )
    )
}

@BindingAdapter("android:text")
fun TextInputEditText.setInt(number: Int) {
    if (number == 0) setText("")
    else setText(number.toString())
}

@InverseBindingAdapter(attribute = "android:text")
fun TextInputEditText.getInt(): Int {
    return this.text.toString().toIntOrNull() ?: 0
}
