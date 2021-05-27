package ru.spbstu.gusev.medicinesstorage.models

import android.os.Parcelable
import androidx.room.TypeConverter
import com.fasterxml.jackson.module.kotlin.jacksonMapperBuilder
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import kotlin.Exception

@Parcelize
data class Time(
    val hours: Int,
    val minutes: Int
) : Parcelable {
    override fun toString(): String {
        val format = SimpleDateFormat("HH:mm")
        val formattedTime = format.parse("$hours:$minutes")
        return format.format(formattedTime)
    }
}

class TimeConverter {
    @TypeConverter
    fun fromTimeList(timeList: List<Time>?): String? {
        return jacksonObjectMapper().writeValueAsString(timeList)
    }

    @TypeConverter
    fun toTimeList(string: String?): List<Time>? {
        return try {
            jacksonObjectMapper().readValue(string ?: "")
        } catch (e: Exception) {
            null
        }
    }
}

fun String?.toTime(): Time {
    return try {
        val parsedTime = "\\d{1,2}:\\d{2}".toRegex().find(this!!)?.value ?: ""
        val (hours, minutes) = parsedTime.split(":")
        Time(hours.toInt(), minutes.toInt())
    } catch (e: Exception) {
        e.printStackTrace()
        Time(0, 0)
    }
}