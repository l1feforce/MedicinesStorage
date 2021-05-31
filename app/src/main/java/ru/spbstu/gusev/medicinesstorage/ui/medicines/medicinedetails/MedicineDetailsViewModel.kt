package ru.spbstu.gusev.medicinesstorage.ui.medicines.medicinedetails

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.spbstu.gusev.medicinesstorage.data.local.medicines.MedicinesRepository
import ru.spbstu.gusev.medicinesstorage.models.Medicine
import ru.spbstu.gusev.medicinesstorage.ui.medicines.models.ResidueDetails
import ru.spbstu.gusev.medicinesstorage.utils.livedata.Event

class MedicineDetailsViewModel(private val medicinesRepository: MedicinesRepository) : ViewModel() {

    val medicineDetails = MutableLiveData<Medicine>()

    val residueDetails = MutableLiveData<ResidueDetails>()
    val editResidueEvent: MutableLiveData<Event<Unit>> = MutableLiveData()
    fun editResidue() {
        editResidueEvent.value = Event(Unit)
    }

    val editUseUntilEvent: MutableLiveData<Event<Unit>> = MutableLiveData()
    fun editUseUntil() {
        editUseUntilEvent.value = Event(Unit)
    }

    val openInstructionEvent = MutableLiveData(Event(""))
    fun openInstruction(url: String) {
        openInstructionEvent.value = Event(url)
    }

    val onCancelEvent = MutableLiveData<Event<Unit>>()
    fun onCancel() {
        onCancelEvent.value = Event(Unit)
    }

    val onSaveEvent = MutableLiveData<Event<Unit>>()
    fun onSave() {
        medicineDetails.value?.let { medicine ->
            viewModelScope.launch {
                Log.d("test", "onSave: id: ${medicine.uid}")
                if (medicine.uid == 0) medicinesRepository.insertMedicines(medicine)
                else medicinesRepository.updateMedicine(medicine)
            }
        }
        onSaveEvent.value = Event(Unit)
    }

    fun deleteMedicine() {
        viewModelScope.launch {
            medicineDetails.value?.let { medicinesRepository.deleteMedicine(it) }
        }
    }

}