package ru.spbstu.gusev.medicinesstorage.ui.medicines.models

import androidx.lifecycle.MutableLiveData

data class ResidueDetails(
    val unitsOfMeasure: MutableLiveData<String> = MutableLiveData(""),
    var volume: Int = 0,
    var residue: Int = 0
)