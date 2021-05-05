package ru.spbstu.gusev.medicinesstorage.ui.medicines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import ru.spbstu.gusev.medicinesstorage.data.local.medicines.model.Medicine
import ru.spbstu.gusev.medicinesstorage.utils.Event

class MedicinesViewModel : ViewModel() {

    private val currentTimeSeconds = System.currentTimeMillis() / 1000

    val medicinesList =
        MutableLiveData<List<Medicine>>().apply { value = Medicine.generateRandom(11) }
    val isEmptyRecyclerView =
        Transformations.map(medicinesList) { it.isEmpty() }

    val expiredMedicinesList = Transformations.map(medicinesList) { medicinesList ->
        medicinesList.filter { medicine ->
            currentTimeSeconds >= medicine.useUntil
        }
    }
    val isEmptyExpiredMedicinesList =
        Transformations.map(expiredMedicinesList) { it.isEmpty() }

    val freshMedicinesList = Transformations.map(medicinesList) { medicinesList ->
        medicinesList.filter { medicine ->
            currentTimeSeconds < medicine.useUntil
        }
    }
    val isEmptyFreshMedicinesList =
        Transformations.map(freshMedicinesList) { it.isEmpty() }


    val openMedicineEvent = MutableLiveData<Event<Medicine>>()

    fun openMedicine(medicine: Medicine) {
        openMedicineEvent.value = Event(medicine)
    }
}