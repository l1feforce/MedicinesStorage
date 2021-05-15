package ru.spbstu.gusev.medicinesstorage.ui.medicines.medicinedetails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.spbstu.gusev.medicinesstorage.data.local.medicines.MedicinesDatabase
import ru.spbstu.gusev.medicinesstorage.data.local.medicines.model.Medicine
import ru.spbstu.gusev.medicinesstorage.ui.medicines.models.ResidueDetails
import ru.spbstu.gusev.medicinesstorage.utils.Event

class MedicineDetailsViewModel(val userMedicinesRepository: MedicinesDatabase) : ViewModel() {

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
                userMedicinesRepository.medicinesDao().insert(medicine)
            }
        }
        onSaveEvent.value = Event(Unit)
    }

    fun deleteMedicine() {
        viewModelScope.launch {
            medicineDetails.value?.let { userMedicinesRepository.medicinesDao().delete(it) }
        }
    }

}