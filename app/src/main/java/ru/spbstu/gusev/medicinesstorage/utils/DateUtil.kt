package ru.spbstu.gusev.medicinesstorage.utils

import java.text.SimpleDateFormat
import java.util.*

class DateUtil {
    companion object {
        fun currentDate(): Long = System.currentTimeMillis() / 1000

        fun formatAsEuDate(date: Long): String {
            val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            return dateFormat.format(Date(date*1000))
        }

        //Date in UNIX seconds
        fun calculateDaysDistance(firstDate: Long, secondDate: Long): Int =
            (kotlin.math.abs(firstDate - secondDate) / 60 / 60 / 24).toInt()
    }
}