package ru.spbstu.gusev.medicinesstorage.models

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.lifecycle.MutableLiveData
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import ru.spbstu.gusev.medicinesstorage.R

@Keep
@Entity
@Parcelize
data class Reminder(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo val medicineId: Int = 0,
    @ColumnInfo val tradeName: String = "",
    @ColumnInfo val imageRes: Int = R.drawable.ic_medication_package,
    @ColumnInfo var dosageForm: String = "",
    @ColumnInfo var dose: Int = 0,
    @ColumnInfo var intakesAmount: Int = 0,
    @ColumnInfo var intakes: List<Time> = listOf(),
    @ColumnInfo var duration: Int = 0,
    @ColumnInfo val isStarted: Boolean = false
) : Parcelable {

    fun toObservable(): ReminderObservable {
        return ReminderObservable(
            id,
            medicineId,
            tradeName,
            imageRes,
            MutableLiveData(dosageForm),
            dose,
            MutableLiveData(intakesAmount),
            MutableLiveData(intakes),
            duration,
            isStarted
        )
    }

    companion object {
    }

}