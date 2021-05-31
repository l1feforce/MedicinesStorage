package ru.spbstu.gusev.medicinesstorage.ui.medicines

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import ru.spbstu.gusev.medicinesstorage.data.local.MedicinesRepository
import ru.spbstu.gusev.medicinesstorage.data.local.medicines.model.Medicine
import ru.spbstu.gusev.medicinesstorage.utils.livedata.Event

class MedicinesViewModel(medicinesRepository: MedicinesRepository) : ViewModel() {

    private val currentTimeSeconds = System.currentTimeMillis() / 1000

    val medicinesList =
        medicinesRepository.getAllMedicines().asLiveData()
    val filteredMedicinesList = MutableLiveData(medicinesRepository.getAllMedicines().asLiveData().value)
    val isEmptyRecyclerView =
        Transformations.map(medicinesList) { it.isEmpty() }

    val expiredMedicinesList = Transformations.map(filteredMedicinesList) { medicinesList ->
        medicinesList?.filter { medicine ->
            currentTimeSeconds >= medicine.useUntil
        } ?: listOf()
    }
    val isEmptyExpiredMedicinesList =
        Transformations.map(expiredMedicinesList) { it.isEmpty() }

    val freshMedicinesList = Transformations.map(filteredMedicinesList) { medicinesList ->
        medicinesList?.filter { medicine ->
            currentTimeSeconds < medicine.useUntil
        } ?: listOf()
    }
    val isEmptyFreshMedicinesList =
        Transformations.map(freshMedicinesList) { it.isEmpty() }

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

    val addNewMedicineEvent = MutableLiveData<Event<Unit>>()

    fun addNewMedicine() {
        addNewMedicineEvent.value = Event(Unit)
    }
}