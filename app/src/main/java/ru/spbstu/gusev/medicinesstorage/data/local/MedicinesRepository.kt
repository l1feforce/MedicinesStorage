package ru.spbstu.gusev.medicinesstorage.data.local

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import ru.spbstu.gusev.medicinesstorage.data.local.medicines.MedicinesDatabase
import ru.spbstu.gusev.medicinesstorage.data.local.medicines.model.Medicine
import ru.spbstu.gusev.medicinesstorage.models.Reminder
import ru.spbstu.gusev.medicinesstorage.models.TriggeredReminder

class MedicinesRepository(private val medicinesDatabase: MedicinesDatabase) {

    fun getAllMedicines(): Flow<List<Medicine>> {
        return medicinesDatabase.medicinesDao().getAll()
    }

    suspend fun insertMedicines(vararg medicines: Medicine) {
        medicinesDatabase.medicinesDao().insert(*medicines)
    }

    suspend fun getMedicineById(medicineId: Int): Medicine {
        return medicinesDatabase.medicinesDao().getById(medicineId)
    }

    suspend fun updateMedicine(medicine: Medicine) {
        medicinesDatabase.medicinesDao().insert(medicine)
    }

    //TODO("Other medicines operations")

    fun getAllReminders(): Flow<List<Reminder>> {
        return medicinesDatabase.remindersDao().getAll()
    }

    suspend fun getReminderById(id: Int): Reminder {
        return medicinesDatabase.remindersDao().getById(id)
    }

    suspend fun updateReminder(reminder: Reminder) {
        medicinesDatabase.remindersDao().update(reminder)
    }

    suspend fun insertReminders(vararg reminder: Reminder): List<Long> {
        return medicinesDatabase.remindersDao().insert(*reminder)
    }

    suspend fun deleteReminder(reminder: Reminder) {
        medicinesDatabase.remindersDao().delete(reminder)
    }

    fun getAllTriggeredReminders(): Flow<List<TriggeredReminder>> {
        return medicinesDatabase.triggeredRemindersDao().getAll()
    }

    suspend fun insertTriggeredReminders(vararg reminder: TriggeredReminder): List<Long> {
        return medicinesDatabase.triggeredRemindersDao().insert(*reminder)
    }

    suspend fun deleteTriggeredRemindersByReminderId(id: Int) {
        medicinesDatabase.triggeredRemindersDao().deleteByReminderId(id)
    }

    suspend fun deleteTriggeredReminderById(id: Int) {
        medicinesDatabase.triggeredRemindersDao().deleteById(id)
    }

    suspend fun getTriggeredReminderById(id: Int): TriggeredReminder {
        return medicinesDatabase.triggeredRemindersDao().getById(id)
    }
}