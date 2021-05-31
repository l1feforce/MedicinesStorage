package ru.spbstu.gusev.medicinesstorage.data.network.grls

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import ru.spbstu.gusev.medicinesstorage.models.Medicine

interface GrlsServiceApi {

    @Headers("Authorization: Basic bDFmZWZvcmNlOjg5MTA2Njg5NzEwQWE=")
    @GET("medicines/{query}")
    suspend fun getMedicinesSearchResult(@Path("query") query: String): List<Medicine>

    @Headers("Authorization: Basic bDFmZWZvcmNlOjg5MTA2Njg5NzEwQWE=")
    @GET("barcodeSearch/{query}")
    suspend fun getMedicinesByBarcode(@Path("query") query: String): List<Medicine>

    @GET(".")
    suspend fun wakeUpServer()

}
