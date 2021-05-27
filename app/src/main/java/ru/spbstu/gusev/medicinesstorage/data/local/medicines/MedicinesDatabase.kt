package ru.spbstu.gusev.medicinesstorage.data.local.medicines

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.spbstu.gusev.medicinesstorage.data.local.medicines.model.Medicine
import ru.spbstu.gusev.medicinesstorage.data.local.remiders.RemindersDao
import ru.spbstu.gusev.medicinesstorage.data.local.remiders.TriggeredRemindersDao
import ru.spbstu.gusev.medicinesstorage.models.Reminder
import ru.spbstu.gusev.medicinesstorage.models.TimeConverter
import ru.spbstu.gusev.medicinesstorage.models.TriggeredReminder

const val MEDICINES_DATABASE_NAME = "medicines_db"

@Database(entities = [Medicine::class, Reminder::class, TriggeredReminder::class], version = 1)
@TypeConverters(TimeConverter::class)
abstract class MedicinesDatabase: RoomDatabase() {
    abstract fun medicinesDao(): MedicinesDao
    abstract fun remindersDao(): RemindersDao
    abstract fun triggeredRemindersDao(): TriggeredRemindersDao
}