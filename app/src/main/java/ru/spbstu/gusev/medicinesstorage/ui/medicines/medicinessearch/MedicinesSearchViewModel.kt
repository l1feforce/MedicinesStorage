package ru.spbstu.gusev.medicinesstorage.ui.medicines.medicinessearch

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.spbstu.gusev.medicinesstorage.data.network.grls.GrlsServiceRepository
import ru.spbstu.gusev.medicinesstorage.data.network.helpers.ResultWrapper
import ru.spbstu.gusev.medicinesstorage.models.Medicine
import ru.spbstu.gusev.medicinesstorage.utils.livedata.Event

class MedicinesSearchViewModel(private val grlsServiceRepository: GrlsServiceRepository) : ViewModel() {
    val searchQuery = MutableLiveData("")
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
                val searchResultList = grlsServiceRepository.getMedicinesSearchResult(query)
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

    val onBarcodeClickEvent = MutableLiveData<Event<Unit>>()
    fun onBarcodeClick() {
        onBarcodeClickEvent.value = Event(Unit)
    }

    fun performBarcodeSearch(barcode: String) {
        isLoading.value = true
        viewModelScope.launch {
            val searchResultList = grlsServiceRepository.getMedicinesByBarcode(barcode)
            if (searchResultList is ResultWrapper.Success) medicinesList.postValue(
                searchResultList.value
            )
            isLoading.postValue(false)
        }
    }
}