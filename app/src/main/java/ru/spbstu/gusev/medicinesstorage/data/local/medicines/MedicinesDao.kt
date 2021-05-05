package ru.spbstu.gusev.medicinesstorage.data.local.medicines

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import ru.spbstu.gusev.medicinesstorage.data.local.medicines.model.Medicine

@Dao
interface MedicinesDao {
    @Query("SELECT * FROM medicine")
    fun getAll(): List<Medicine>

    @Insert
    fun insert(vararg medicines: Medicine)

    @Delete
    fun delete(medicine: Medicine)
}