package ru.spbstu.gusev.medicinesstorage.data.local.statistics

import ru.spbstu.gusev.medicinesstorage.data.local.reminders.RemindersDatabase
import ru.spbstu.gusev.medicinesstorage.data.network.medicines.MedicinesRemoteDataSource
import ru.spbstu.gusev.medicinesstorage.models.Statistics

class StatisticsRepository(
    private val remindersDatabase: RemindersDatabase,
    private val medicinesRemoteDataSource: MedicinesRemoteDataSource
) {

    suspend fun getStatistics(): Statistics {
        val medicinesAmount =
            medicinesRemoteDataSource.getAllAsync()?.size ?: 0
        val remindersAmount =
            remindersDatabase.remindersDao().getAllAsync().size
        return Statistics(medicinesAmount, remindersAmount)
    }
}