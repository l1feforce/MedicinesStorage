package ru.spbstu.gusev.medicinesstorage.ui.reminders

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ru.spbstu.gusev.medicinesstorage.data.local.MedicinesRepository
import ru.spbstu.gusev.medicinesstorage.data.local.remiders.RemindersRepository
import ru.spbstu.gusev.medicinesstorage.models.Reminder
import ru.spbstu.gusev.medicinesstorage.models.TriggeredReminder
import ru.spbstu.gusev.medicinesstorage.utils.livedata.Event


class RemindersViewModel(
    private val medicinesRepository: MedicinesRepository,
    private val remindersRepository: RemindersRepository
) : ViewModel() {

    val remindersList =
        medicinesRepository.getAllReminders().asLiveData()
    val filteredRemindersList =
        MutableLiveData(medicinesRepository.getAllReminders().asLiveData().value)
    val isEmptyRecyclerView =
        Transformations.map(remindersList) { it.isEmpty() }

    val haveToTakeRemindersList =
        remindersRepository.getTriggeredReminders().asLiveData()
    val isEmptyHaveToTakeRemindersList =
        Transformations.map(haveToTakeRemindersList) { it?.isEmpty() ?: true }


    fun filter(query: String?) {
        val filteredList = remindersList.value?.filter { reminder ->
            reminder.tradeName.contains(
                query ?: "",
                ignoreCase = true
            )
        }
        filteredRemindersList.value = filteredList
    }

    val addNewReminderEvent = MutableLiveData<Event<Unit>>()

    fun addNewReminder() {
        addNewReminderEvent.value = Event(Unit)
    }

    fun addNotifications(reminder: Reminder) {
        viewModelScope.launch {
            remindersRepository.startReminder(reminder.copy(isStarted = true))
        }
    }

    fun removeNotifications(reminder: Reminder) {
        viewModelScope.launch {
            remindersRepository.stopReminder(reminder.copy(isStarted = false))
        }
    }

    fun skipNotification(reminder: TriggeredReminder) {
        viewModelScope.launch { remindersRepository.reminderSkip(reminder) }
    }

    fun completeNotification(reminder: TriggeredReminder) {
        viewModelScope.launch { remindersRepository.reminderComplete(reminder) }
    }
}