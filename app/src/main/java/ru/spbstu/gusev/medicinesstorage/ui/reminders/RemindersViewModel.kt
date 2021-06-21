package ru.spbstu.gusev.medicinesstorage.ui.reminders

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ru.spbstu.gusev.medicinesstorage.data.local.notifications.NotificationsService
import ru.spbstu.gusev.medicinesstorage.data.local.reminders.RemindersRepository
import ru.spbstu.gusev.medicinesstorage.models.Reminder
import ru.spbstu.gusev.medicinesstorage.models.TriggeredReminder
import ru.spbstu.gusev.medicinesstorage.utils.livedata.Event


class RemindersViewModel(
    private val remindersRepository: RemindersRepository,
    private val notificationsService: NotificationsService
) : ViewModel() {

    val remindersList =
        remindersRepository.getAllReminders().asLiveData()
    val filteredRemindersList =
        MutableLiveData(remindersRepository.getAllReminders().asLiveData().value)
    val isEmptyRecyclerView =
        Transformations.map(remindersList) { it.isEmpty() }

    val triggeredRemindersList =
        notificationsService.getTriggeredReminders().asLiveData()
    val isEmptyTriggeredRemindersList =
        Transformations.map(triggeredRemindersList) { it?.isEmpty() ?: true }

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
            notificationsService.startReminder(reminder.copy(isStarted = true))
        }
    }

    fun removeNotifications(reminder: Reminder) {
        viewModelScope.launch {
            notificationsService.stopReminder(reminder.copy(isStarted = false))
        }
    }

    fun skipNotification(reminder: TriggeredReminder) {
        viewModelScope.launch { notificationsService.reminderSkip(reminder) }
    }

    fun completeNotification(reminder: TriggeredReminder) {
        viewModelScope.launch { notificationsService.reminderComplete(reminder) }
    }
}