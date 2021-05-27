package ru.spbstu.gusev.medicinesstorage.ui.reminders.reminderdetails

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.spbstu.gusev.medicinesstorage.data.local.MedicinesRepository
import ru.spbstu.gusev.medicinesstorage.data.local.remiders.RemindersRepository
import ru.spbstu.gusev.medicinesstorage.models.ReminderObservable
import ru.spbstu.gusev.medicinesstorage.utils.livedata.Event

class ReminderDetailsViewModel(
    private val medicinesRepository: MedicinesRepository,
    private val remindersRepository: RemindersRepository
) : ViewModel() {
    val reminderDetails = MutableLiveData<ReminderObservable>()

    val onSaveEvent = MutableLiveData<Event<Unit>>()
    fun onSave() {
        GlobalScope.launch {
            reminderDetails.value?.toReminder()?.let { reminder ->
                Log.d("test", "onSave: reminder: $reminder")
                remindersRepository.stopReminder(reminder)
                remindersRepository.startReminder(reminder.copy(isStarted = true))
            }
        }
        onSaveEvent.value = Event(Unit)
    }

    val onCancelEvent = MutableLiveData<Event<Unit>>()
    fun onCancel() {
        onCancelEvent.value = Event(Unit)
    }

    fun deleteReminder() {
        viewModelScope.launch {
            reminderDetails.value?.toReminder()?.let { reminder ->
                remindersRepository.removeReminder(reminder)
            }
        }
    }
}