package ru.spbstu.gusev.medicinesstorage.data.local.medicines.model

import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Medicine(
    @PrimaryKey(autoGenerate = true) var uid: Int,
    @ColumnInfo val imageUrl: String,
    @ColumnInfo val name: String,
    @ColumnInfo val description: String,
    @ColumnInfo val volume: Float,
    @ColumnInfo val residue: Float,
    @ColumnInfo val unitsOfMeasure: String,
    @ColumnInfo val useUntil: Long, //UNIX seconds
    @ColumnInfo val storeAfterOpen: Int, //days
    @ColumnInfo val activeIngredient: String,
    @ColumnInfo val pharmacologicalGroup: String,
    @ColumnInfo val instructionUrl: String
): Parcelable {
    companion object {
        const val SECONDS_IN_DAY = 86400

        fun generateRandom(amount: Int): List<Medicine> {
            val result = mutableListOf<Medicine>()
            for (i in 0 until amount) {
                result.add(
                    Medicine(
                        0,
                        imageUrl = "https://funpick.ru/wp-content/uploads/2017/10/Malenkie-1.jpeg",
                        name = "Some title $i",
                        description = "Some description $i",
                        volume = 100f,
                        residue = 50f - i,
                        unitsOfMeasure = "мл",
                        useUntil = getCurrentDate() - amount/2 * SECONDS_IN_DAY + i * SECONDS_IN_DAY,
                        storeAfterOpen = 5,
                        activeIngredient = "Нифуроксидаз",
                        pharmacologicalGroup = "Противополитеховское",
                        instructionUrl = "https://www.youtube.com/watch?v=dQw4w9WgXcQ"
                    )
                )
            }
            return result.toList()
        }

        private fun getCurrentDate(): Long = System.currentTimeMillis() / 1000
    }
}