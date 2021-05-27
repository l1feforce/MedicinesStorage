package ru.spbstu.gusev.medicinesstorage.models

import android.os.Parcelable
import androidx.lifecycle.MutableLiveData
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import ru.spbstu.gusev.medicinesstorage.R

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
        fun generateMock(): List<Reminder> {
            val k = 10
            val result = mutableListOf<Reminder>()
            for (i in 1..k) {
                result.add(
                    Reminder(
                        id = 0,
                        medicineId = 0,
                        tradeName = "Dummy name $i",
                        imageRes = R.drawable.ic_medication_package,
                        dosageForm = "Dummy dosage $i",
                        dose = 10,
                        intakes = listOf(),
                        duration = 5,
                        isStarted = i % 2 == 0,
                        intakesAmount = 3
                    )
                )
            }
            return result
        }
    }

}