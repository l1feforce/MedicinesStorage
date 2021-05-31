package ru.spbstu.gusev.medicinesstorage.data.local.medicines

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.spbstu.gusev.medicinesstorage.models.Medicine

const val MEDICINES_DATABASE_NAME = "medicines_db"

@Database(entities = [Medicine::class], version = 1)
abstract class MedicinesDatabase : RoomDatabase() {
    abstract fun medicinesDao(): MedicinesDao
}