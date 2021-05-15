package ru.spbstu.gusev.medicinesstorage.data.network.medicinesdatabase

import retrofit2.http.*
import ru.spbstu.gusev.medicinesstorage.data.local.medicines.model.Medicine

interface MedicinesDatabaseApi {

    @Headers("Authorization: Basic bDFmZWZvcmNlOjg5MTA2Njg5NzEwQWE=")
    @GET("medicines/{query}")
    suspend fun getMedicinesSearchResult(@Path("query") query: String): List<Medicine>

    @Headers("Authorization: Basic bDFmZWZvcmNlOjg5MTA2Njg5NzEwQWE=")
    @GET("barcodeSearch/{query}")
    suspend fun getMedicinesByBarcode(@Path("query") query: String): List<Medicine>


}
