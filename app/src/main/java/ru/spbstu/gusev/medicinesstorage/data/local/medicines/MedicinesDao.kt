package ru.spbstu.gusev.medicinesstorage.data.local.medicines

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.spbstu.gusev.medicinesstorage.models.Medicine

@Dao
interface MedicinesDao {
    @Query("SELECT * FROM medicine")
    fun getAll(): Flow<List<Medicine>>

    @Query("SELECT * FROM medicine WHERE uid=:id")
    suspend fun getById(id: Int): Medicine

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg medicines: Medicine)

    @Delete
    suspend fun delete(medicine: Medicine)

    @Query("DELETE FROM medicine")
    suspend fun deleteAll()
}