package ru.spbstu.gusev.medicinesstorage.models

import androidx.lifecycle.MutableLiveData

data class ReminderObservable(
    val id: Int = 0,
    val medicineId: Int = 0,
    val tradeName: String = "",
    val imageRes: Int = 0,
    val dosageForm: MutableLiveData<String> = MutableLiveData(""),
    var dose: Int = 0,
    val intakesAmount: MutableLiveData<Int> = MutableLiveData(0),
    val intakes: MutableLiveData<List<Time>> = MutableLiveData(listOf()),
    var duration: Int = 0,
    val isStarted: Boolean = false
) {
    override fun toString(): String {
        return "ReminderObservable(id=$id, medicineId=$medicineId, tradeName='$tradeName', imageRes=$imageRes, dosageForm=${dosageForm.value}, dose=$dose, intakesAmount=${intakesAmount.value}, intakes=${intakes.value}, duration=$duration, isStarted=$isStarted)"
    }

    fun toReminder(): Reminder =
        Reminder(
            id, medicineId, tradeName, imageRes, dosageForm.value ?: "",
            dose, intakesAmount.value ?: 0, intakes.value ?: listOf(),
            duration, isStarted
        )


}