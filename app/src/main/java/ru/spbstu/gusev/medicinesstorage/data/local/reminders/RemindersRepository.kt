package ru.spbstu.gusev.medicinesstorage.data.local.reminders

import kotlinx.coroutines.flow.Flow
import ru.spbstu.gusev.medicinesstorage.models.Reminder
import ru.spbstu.gusev.medicinesstorage.models.TriggeredReminder

class RemindersRepository(private val remindersDatabase: RemindersDatabase) {
    fun getAllReminders(): Flow<List<Reminder>> {
        return remindersDatabase.remindersDao().getAll()
    }

    suspend fun getReminderById(id: Int): Reminder {
        return remindersDatabase.remindersDao().getById(id)
    }

    suspend fun updateReminder(reminder: Reminder) {
        remindersDatabase.remindersDao().update(reminder)
    }

    suspend fun insertReminders(vararg reminder: Reminder): List<Long> {
        return remindersDatabase.remindersDao().insert(*reminder)
    }

    suspend fun deleteReminder(reminder: Reminder) {
        remindersDatabase.remindersDao().delete(reminder)
    }

    fun getAllTriggeredReminders(): Flow<List<TriggeredReminder>> {
        return remindersDatabase.triggeredRemindersDao().getAll()
    }

    suspend fun insertTriggeredReminders(vararg reminder: TriggeredReminder): List<Long> {
        return remindersDatabase.triggeredRemindersDao().insert(*reminder)
    }

    suspend fun deleteTriggeredRemindersByReminderId(id: Int) {
        remindersDatabase.triggeredRemindersDao().deleteByReminderId(id)
    }

    suspend fun deleteTriggeredReminderById(id: Int) {
        remindersDatabase.triggeredRemindersDao().deleteById(id)
    }

    suspend fun getTriggeredReminderById(id: Int): TriggeredReminder {
        return remindersDatabase.triggeredRemindersDao().getById(id)
    }
}