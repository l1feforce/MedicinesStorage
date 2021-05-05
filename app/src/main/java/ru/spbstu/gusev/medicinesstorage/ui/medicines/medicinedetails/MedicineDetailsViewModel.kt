package ru.spbstu.gusev.medicinesstorage.ui.medicines.medicinedetails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.spbstu.gusev.medicinesstorage.data.local.medicines.model.Medicine
import ru.spbstu.gusev.medicinesstorage.utils.DateUtil
import java.lang.Math.abs
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*

class MedicineDetailsViewModel : ViewModel() {

    val medicineDetails = MutableLiveData<Medicine>()

    fun editResidue() {
        //TODO()
    }

    fun editUseUntil() {
        //TODO()
    }

    fun openInstruction(url: String) {
        //TODO()
    }

    fun onCancel() {
        //TODO()
    }

    fun onSave() {
        //TODO()
    }

}