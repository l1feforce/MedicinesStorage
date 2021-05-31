package ru.spbstu.gusev.medicinesstorage.ui.reminders

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ru.spbstu.gusev.medicinesstorage.data.local.notifications.NotificationsRepository
import ru.spbstu.gusev.medicinesstorage.data.local.reminders.RemindersRepository
import ru.spbstu.gusev.medicinesstorage.models.Reminder
import ru.spbstu.gusev.medicinesstorage.models.TriggeredReminder
import ru.spbstu.gusev.medicinesstorage.utils.livedata.Event


class RemindersViewModel(
    remindersRepository: RemindersRepository,
    private val notificationsRepository: NotificationsRepository
) : ViewModel() {

    val remindersList =
        remindersRepository.getAllReminders().asLiveData()
    val filteredRemindersList =
        MutableLiveData(remindersRepository.getAllReminders().asLiveData().value)
    val isEmptyRecyclerView =
        Transformations.map(remindersList) { it.isEmpty() }

    val haveToTakeRemindersList =
        notificationsRepository.getTriggeredReminders().asLiveData()
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
            notificationsRepository.startReminder(reminder.copy(isStarted = true))
        }
    }

    fun removeNotifications(reminder: Reminder) {
        viewModelScope.launch {
            notificationsRepository.stopReminder(reminder.copy(isStarted = false))
        }
    }

    fun skipNotification(reminder: TriggeredReminder) {
        viewModelScope.launch { notificationsRepository.reminderSkip(reminder) }
    }

    fun completeNotification(reminder: TriggeredReminder) {
        viewModelScope.launch { notificationsRepository.reminderComplete(reminder) }
    }
}