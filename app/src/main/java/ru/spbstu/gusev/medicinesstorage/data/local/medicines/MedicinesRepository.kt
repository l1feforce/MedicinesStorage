package ru.spbstu.gusev.medicinesstorage.data.local.medicines

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import ru.spbstu.gusev.medicinesstorage.data.network.medicines.MedicinesRemoteDataSource
import ru.spbstu.gusev.medicinesstorage.models.Medicine

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
}