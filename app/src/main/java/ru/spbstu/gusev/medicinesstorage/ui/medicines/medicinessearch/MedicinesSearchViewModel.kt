package ru.spbstu.gusev.medicinesstorage.ui.medicines.medicinessearch

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import ru.spbstu.gusev.gpstracker.data.network.wrapper.ResultWrapper
import ru.spbstu.gusev.medicinesstorage.data.local.medicines.model.Medicine
import ru.spbstu.gusev.medicinesstorage.data.network.medicinesdatabase.MedicinesNetworkRepository
import ru.spbstu.gusev.medicinesstorage.utils.Event

class MedicinesSearchViewModel(
    val searchMedicinesRepository: MedicinesNetworkRepository) : ViewModel() {
    val searchQuery = MutableLiveData<String>("")
    var oldQuery = ""
    val barcode = MutableLiveData<String>()

    val medicinesList =
        MutableLiveData<List<Medicine>>().apply { value = listOf() }
    val isEmptyRecyclerView =
        Transformations.map(medicinesList) { it.isEmpty() }

    private lateinit var searchJob: Job
    val isLoading = MutableLiveData(false)

    fun performMedicinesSearch(query: String?) {
        isLoading.value = true
        if (::searchJob.isInitialized) searchJob.cancel()
        searchJob = viewModelScope.launch {
            delay(1000)
            Log.d("TAG", "performMedicinesSearch: inside")
            if (!query.isNullOrBlank()) {
                val searchResultList = searchMedicinesRepository.getMedicinesSearchResult(query)
                if (searchResultList is ResultWrapper.Success) medicinesList.postValue(
                    searchResultList.value
                )
                isLoading.postValue(false)
            }
        }
    }

    val openMedicineEvent = MutableLiveData<Event<Medicine>>()

    fun openMedicine(medicine: Medicine) {
        openMedicineEvent.value = Event(medicine)
    }

    fun performBarcodeSearch(barcode: String) {
        isLoading.value = true
        viewModelScope.launch {
            val searchResultList = searchMedicinesRepository.getMedicinesByBarcode(barcode)
            if (searchResultList is ResultWrapper.Success) medicinesList.postValue(
                searchResultList.value
            )
            isLoading.postValue(false)
        }
    }
}