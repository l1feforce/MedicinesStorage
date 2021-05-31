package ru.spbstu.gusev.medicinesstorage.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Medicine(
    @PrimaryKey(autoGenerate = true) var uid: Int = 0,
    @ColumnInfo val imageUrl: String = "",
    @ColumnInfo val name: String = "",
    @ColumnInfo val packageDescription: String = "",
    @ColumnInfo val activeIngredientConcentration: String = "",
    @ColumnInfo val volume: Int = 0,
    @ColumnInfo val residue: Int = 0,
    @ColumnInfo val unitsOfMeasure: String = "",
    @ColumnInfo val useUntil: Long = 0L, //UNIX seconds
    @ColumnInfo val storeAfterOpen: Int = 0, //days
    @ColumnInfo val activeIngredient: String = "",
    @ColumnInfo val pharmacologicalGroup: String = "",
    @ColumnInfo val instructionUrl: String = "",
    @ColumnInfo val medicineDescription: String = ""
) : Parcelable {
    companion object {
        const val SECONDS_IN_DAY = 86400

        private fun getCurrentDate(): Long = System.currentTimeMillis() / 1000
    }
}