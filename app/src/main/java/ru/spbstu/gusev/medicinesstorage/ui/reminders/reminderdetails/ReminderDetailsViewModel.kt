package ru.spbstu.gusev.medicinesstorage.ui.reminders.reminderdetails

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.spbstu.gusev.medicinesstorage.data.local.notifications.NotificationsRepository
import ru.spbstu.gusev.medicinesstorage.models.ReminderObservable
import ru.spbstu.gusev.medicinesstorage.utils.livedata.Event

class ReminderDetailsViewModel(
    private val notificationsRepository: NotificationsRepository
) : ViewModel() {
    val reminderDetails = MutableLiveData<ReminderObservable>()

    val onSaveEvent = MutableLiveData<Event<Unit>>()
    fun onSave() {
        onSaveEvent.value = Event(Unit)
    }

    fun performSave() {
        GlobalScope.launch {
            reminderDetails.value?.toReminder()?.let { reminder ->
                Log.d("test", "onSave: reminder: $reminder")
                notificationsRepository.stopReminder(reminder)
                notificationsRepository.startReminder(reminder.copy(isStarted = true))
            }
        }
    }

    val onCancelEvent = MutableLiveData<Event<Unit>>()
    fun onCancel() {
        onCancelEvent.value = Event(Unit)
    }

    fun deleteReminder() {
        viewModelScope.launch {
            reminderDetails.value?.toReminder()?.let { reminder ->
                notificationsRepository.removeReminder(reminder)
            }
        }
    }

    val onDoseHelpClickedEvent = MutableLiveData<Event<Unit>>()
    fun onDoseHelpClicked() {
        onDoseHelpClickedEvent.value = Event(Unit)
    }
}