package ru.spbstu.gusev.medicinesstorage.ui.reminders.addingnewsearch

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import ru.spbstu.gusev.medicinesstorage.data.local.MedicinesRepository
import ru.spbstu.gusev.medicinesstorage.data.local.medicines.model.Medicine
import ru.spbstu.gusev.medicinesstorage.utils.livedata.Event

class AddingNewSearchViewModel(private val medicinesRepository: MedicinesRepository) : ViewModel() {
    val searchQuery = MutableLiveData("")

    val medicinesList =
        medicinesRepository.getAllMedicines().asLiveData()
    val filteredMedicinesList = MutableLiveData(medicinesRepository.getAllMedicines().asLiveData().value)
    val isEmptyRecyclerView =
        Transformations.map(filteredMedicinesList) { it?.isEmpty() ?: true }

    fun filter(query: String?) {
        val filteredList = medicinesList.value?.filter { medicine ->
            medicine.name.contains(
                query ?: "",
                ignoreCase = true
            )
        }
        filteredMedicinesList.value = filteredList
    }

    val openMedicineEvent = MutableLiveData<Event<Medicine>>()

    fun openMedicine(medicine: Medicine) {
        openMedicineEvent.value = Event(medicine)
    }
}