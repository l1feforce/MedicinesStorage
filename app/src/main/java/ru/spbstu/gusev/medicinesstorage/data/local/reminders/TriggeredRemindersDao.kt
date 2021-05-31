package ru.spbstu.gusev.medicinesstorage.data.local.reminders

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.spbstu.gusev.medicinesstorage.models.TriggeredReminder

@Dao
interface TriggeredRemindersDao {
    @Query("SELECT * FROM triggeredreminder WHERE triggerTime < strftime('%s','now') * 1000")
    fun getAll(): Flow<List<TriggeredReminder>>

    @Query("SELECT * FROM triggeredreminder WHERE id=:id")
    suspend fun getById(id: Int): TriggeredReminder

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg reminders: TriggeredReminder): List<Long>

    @Update
    suspend fun update(reminder: TriggeredReminder)

    @Delete
    suspend fun delete(reminder: TriggeredReminder)

    @Query("DELETE FROM triggeredreminder WHERE reminderId=:id")
    suspend fun deleteByReminderId(id: Int)

    @Query("DELETE FROM triggeredreminder WHERE id=:id")
    suspend fun deleteById(id: Int)

}