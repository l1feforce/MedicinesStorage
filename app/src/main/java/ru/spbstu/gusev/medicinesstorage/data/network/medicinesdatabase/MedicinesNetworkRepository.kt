package ru.spbstu.gusev.medicinesstorage.data.network.medicinesdatabase

import ru.spbstu.gusev.gpstracker.data.network.wrapper.ResultWrapper
import ru.spbstu.gusev.medicinesstorage.data.local.medicines.model.Medicine
import ru.spbstu.gusev.medicinesstorage.data.network.helpers.safeApiCall
import ru.spbstu.gusev.medicinesstorage.data.network.medicinesdatabase.MedicinesDatabaseApi

class MedicinesNetworkRepository(val medicinesDatabaseApi: MedicinesDatabaseApi) {

    suspend fun getMedicinesSearchResult(query: String): ResultWrapper<List<Medicine>> =
        safeApiCall { medicinesDatabaseApi.getMedicinesSearchResult(query) }

    suspend fun getMedicinesByBarcode(query: String): ResultWrapper<List<Medicine>> =
        safeApiCall { medicinesDatabaseApi.getMedicinesByBarcode(query) }
}