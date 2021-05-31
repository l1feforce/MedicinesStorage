package ru.spbstu.gusev.medicinesstorage.data.local.reminders

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.spbstu.gusev.medicinesstorage.models.Reminder

@Dao
interface RemindersDao {
    @Query("SELECT * FROM reminder")
    fun getAll(): Flow<List<Reminder>>

    @Query("SELECT * FROM reminder")
    fun getAllAsync(): List<Reminder>

    @Query("SELECT * FROM reminder WHERE id=:id")
    suspend fun getById(id: Int): Reminder

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg reminders: Reminder): List<Long>

    @Update
    suspend fun update(reminder: Reminder)

    @Delete
    suspend fun delete(reminder: Reminder)
}