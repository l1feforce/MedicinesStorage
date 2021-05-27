package ru.spbstu.gusev.medicinesstorage.ui.reminders.bindings

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.databinding.BindingAdapter
import ru.spbstu.gusev.medicinesstorage.R
import ru.spbstu.gusev.medicinesstorage.models.Reminder

@SuppressLint("SetTextI18n")
@BindingAdapter("nextDose")
fun TextView.nextDose(reminder: Reminder) {
    val dosesPerDay = this.resources.getQuantityString(
        R.plurals.reminders_plurals_per_day,
        reminder.intakesAmount,
        reminder.intakesAmount
    )
    val days = this.resources.getQuantityString(
        R.plurals.reminders_plurals_days,
        reminder.duration,
        reminder.duration
    )
    text = "$dosesPerDay $days"
}