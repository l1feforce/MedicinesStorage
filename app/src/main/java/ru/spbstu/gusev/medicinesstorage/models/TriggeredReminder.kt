package ru.spbstu.gusev.medicinesstorage.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.spbstu.gusev.medicinesstorage.R

@Entity
data class TriggeredReminder(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo val reminderId: Int = 0,
    @ColumnInfo val medicineId: Int = 0,
    @ColumnInfo val tradeName: String = "",
    @ColumnInfo val imageRes: Int = R.drawable.ic_medication_package,
    @ColumnInfo val triggerTime: Long = 0L
)