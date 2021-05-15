package ru.spbstu.gusev.medicinesstorage.ui.medicines.camera

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.spbstu.gusev.medicinesstorage.utils.Event

class ScannerViewModel : ViewModel() {

    val barcode: MutableLiveData<Event<String>> = MutableLiveData()
}