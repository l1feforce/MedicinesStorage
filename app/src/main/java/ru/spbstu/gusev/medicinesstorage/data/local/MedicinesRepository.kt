package ru.spbstu.gusev.medicinesstorage.data.local

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import ru.spbstu.gusev.medicinesstorage.data.local.medicines.MedicinesDatabase
import ru.spbstu.gusev.medicinesstorage.data.local.medicines.model.Medicine
import ru.spbstu.gusev.medicinesstorage.data.network.medicinesfirebase.MedicinesRemoteDataSource
import ru.spbstu.gusev.medicinesstorage.models.Reminder
import ru.spbstu.gusev.medicinesstorage.models.Statistics
import ru.spbstu.gusev.medicinesstorage.models.TriggeredReminder

class MedicinesRepository(
    private val medicinesDatabase: MedicinesDatabase,
    private val medicinesRemoteDataSource: MedicinesRemoteDataSource
) {
    private fun isAuthorized(): Boolean = Firebase.auth.currentUser != null

    fun getAllMedicines(): Flow<List<Medicine>> {
        return if (isAuthorized()) {
            medicinesRemoteDataSource.getAll()!!
        } else medicinesDatabase.medicinesDao().getAll()
    }

    fun getAllLocalMedicines(): Flow<List<Medicine>> = medicinesDatabase.medicinesDao().getAll()

    suspend fun insertMedicines(vararg medicines: Medicine) {
        if (isAuthorized()) medicinesRemoteDataSource.insert(*medicines)
        else medicinesDatabase.medicinesDao().insert(*medicines)
    }

    suspend fun getMedicineById(medicineId: Int): Medicine {
        return if (isAuthorized()) medicinesRemoteDataSource.getById(medicineId)!!
        else medicinesDatabase.medicinesDao()
            .getById(medicineId)
    }

    suspend fun updateMedicine(medicine: Medicine) {
        if (isAuthorized()) medicinesRemoteDataSource.update(medicine)
        else medicinesDatabase.medicinesDao().insert(medicine)
    }

    suspend fun deleteMedicine(medicine: Medicine) {
        if (isAuthorized()) medicinesRemoteDataSource.delete(medicine)
        else medicinesDatabase.medicinesDao().delete(medicine)
    }

    suspend fun insertMedicinesToRemote(vararg medicines: Medicine) {
        medicinesRemoteDataSource.insert(*medicines)
    }

    suspend fun deleteAllLocalMedicines() {
        medicinesDatabase.medicinesDao().deleteAll()
    }

    suspend fun getStatistics(): Statistics {
        val medicinesAmount =
            medicinesRemoteDataSource.getAllAsync()?.size ?: 0
        val remindersAmount =
            medicinesDatabase.remindersDao().getAllAsync().size
        return Statistics(medicinesAmount, remindersAmount)
    }


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