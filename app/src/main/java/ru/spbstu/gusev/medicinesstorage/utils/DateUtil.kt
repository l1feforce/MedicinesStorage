package ru.spbstu.gusev.medicinesstorage.utils

import ru.spbstu.gusev.medicinesstorage.models.IntakeInitialData
import ru.spbstu.gusev.medicinesstorage.models.Time
import java.text.SimpleDateFormat
import java.util.*

class DateUtil {
    companion object {
        fun currentDate(): Long = System.currentTimeMillis() / 1000

        fun formatAsEuDate(date: Long): String {
            val dateFormat = SimpleDateFormat("MM.yyyy", Locale.getDefault())
            return dateFormat.format(Date(date * 1000))
        }

        //Date in UNIX seconds
        fun calculateDaysDistance(currentDate: Long, secondDate: Long): Int =
            ((secondDate - currentDate) / 60 / 60 / 24).toInt()

        fun calculateNearestIntakeIndex(intakes: List<Time>): IntakeInitialData {
            val currentDateCalendar = Calendar.getInstance()
            intakes.forEachIndexed { index, time ->
                currentDateCalendar.apply {
                    set(Calendar.HOUR_OF_DAY, time.hours)
                    set(Calendar.MINUTE, time.minutes)
                }
                if (currentDateCalendar.timeInMillis - System.currentTimeMillis() >= 0) return IntakeInitialData(index, 0)
            }
            return IntakeInitialData(0, 1)
        }

        fun calculateDelayInMillis(firstTime: Time, secondTime: Time, daysDelta: Int): Long {
            val format = SimpleDateFormat("HH:mm dd")
            val date1 = format.parse("$firstTime 01")
            val date2 = format.parse("$secondTime ${daysDelta+1}")
            return date2.time - date1.time
        }

        fun Calendar.toTime(): Time {
            return Time(this.get(Calendar.HOUR_OF_DAY), this.get(Calendar.MINUTE))
        }
    }
}

typealias Index = Int