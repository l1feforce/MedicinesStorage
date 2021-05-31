package ru.spbstu.gusev.medicinesstorage.data.network.medicinesfirebase

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.Flow
import ru.spbstu.gusev.medicinesstorage.data.local.medicines.model.Medicine

interface RemoteDataSource {

    fun getAll(): Flow<List<Medicine>>?
    suspend fun getById(id: Int): Medicine?
    suspend fun insert(vararg medicines: Medicine)
    suspend fun update(medicine: Medicine)
    suspend fun delete(medicine: Medicine)
    suspend fun getAllAsync(): List<Medicine>?
}
