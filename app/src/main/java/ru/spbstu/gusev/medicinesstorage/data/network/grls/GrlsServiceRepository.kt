package ru.spbstu.gusev.medicinesstorage.data.network.grls

import ru.spbstu.gusev.medicinesstorage.data.network.helpers.ResultWrapper
import ru.spbstu.gusev.medicinesstorage.data.network.helpers.safeApiCall
import ru.spbstu.gusev.medicinesstorage.models.Medicine

class GrlsServiceRepository(val grlsServiceApi: GrlsServiceApi) {

    suspend fun getMedicinesSearchResult(query: String): ResultWrapper<List<Medicine>> =
        safeApiCall { grlsServiceApi.getMedicinesSearchResult(query) }

    suspend fun getMedicinesByBarcode(query: String): ResultWrapper<List<Medicine>> =
        safeApiCall { grlsServiceApi.getMedicinesByBarcode(query) }

    suspend fun wakeUpServer() {
        safeApiCall { grlsServiceApi.wakeUpServer() }
    }
}