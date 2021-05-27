package ru.spbstu.gusev.medicinesstorage.ui.medicines.scanner

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.spbstu.gusev.medicinesstorage.utils.livedata.Event

class ScannerViewModel : ViewModel() {

    val barcode: MutableLiveData<Event<String>> = MutableLiveData()
}