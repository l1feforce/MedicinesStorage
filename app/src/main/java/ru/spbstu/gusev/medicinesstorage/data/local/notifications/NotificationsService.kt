package ru.spbstu.gusev.medicinesstorage.data.local.notifications

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import kotlinx.coroutines.flow.Flow
import ru.spbstu.gusev.medicinesstorage.R
import ru.spbstu.gusev.medicinesstorage.data.local.medicines.MedicinesRepository
import ru.spbstu.gusev.medicinesstorage.data.local.reminders.RemindersRepository
import ru.spbstu.gusev.medicinesstorage.extensions.toIntOrZero
import ru.spbstu.gusev.medicinesstorage.models.Reminder
import ru.spbstu.gusev.medicinesstorage.models.Time
import ru.spbstu.gusev.medicinesstorage.models.TriggeredReminder
import ru.spbstu.gusev.medicinesstorage.models.toTime
import ru.spbstu.gusev.medicinesstorage.utils.DateUtil
import ru.spbstu.gusev.medicinesstorage.utils.DateUtil.Companion.toTime
import ru.spbstu.gusev.medicinesstorage.utils.NotificationsUtil.Companion.showNotificationMedicineIsOver
import ru.spbstu.gusev.medicinesstorage.utils.NotificationsUtil.Companion.showNotificationMedicineWillEndSoon
import java.util.*
import java.util.concurrent.TimeUnit

class NotificationsService(
    private val medicinesRepository: MedicinesRepository,
    private val remindersRepository: RemindersRepository,
    private val workManager: WorkManager,
    private val context: Context
) {

    suspend fun reminderComplete(id: Int, triggeredReminderId: Int) {
        try {
            val reminder = remindersRepository.getReminderById(id)
            val medicine = medicinesRepository.getMedicineById(reminder.medicineId)
            val newResidue = medicine.residue - reminder.dose
            if (newResidue > 0) {
                if (newResidue.toDouble() / medicine.volume <= 0.25) context.showNotificationMedicineWillEndSoon(
                    context.resources.getString(
                        R.string.notifications_title_medicine_will_end_soon,
                        medicine.name
                    ),
                    context.resources.getString(
                        R.string.notifications_body_medicine_will_end_soon,
                        newResidue,
                        medicine.unitsOfMeasure
                    )
                )
            } else context.showNotificationMedicineIsOver(
                context.resources.getString(
                    R.string.notifications_title_no_medicines,
                    medicine.name
                )
            )
            medicinesRepository.updateMedicine(medicine.copy(residue = newResidue.toIntOrZero()))
            remindersRepository.deleteTriggeredReminderById(triggeredReminderId)
        } catch (e: Exception) {
        }
    }

    suspend fun reminderComplete(reminder: TriggeredReminder) {
        reminderComplete(reminder.reminderId, reminder.id)
    }

    suspend fun reminderSkip(id: Int, triggeredReminderId: Int) {
        try {
            val reminder = remindersRepository.getReminderById(id)
            val medicine = medicinesRepository.getMedicineById(reminder.medicineId)
            val newResidue = medicine.residue - reminder.dose
            if (newResidue > 0) {
                if (newResidue.toDouble() / medicine.volume <= 0.25) context.showNotificationMedicineWillEndSoon(
                    context.resources.getString(
                        R.string.notifications_title_medicine_will_end_soon,
                        medicine.name
                    ),
                    context.resources.getString(
                        R.string.notifications_body_medicine_will_end_soon,
                        newResidue,
                        medicine.unitsOfMeasure
                    )
                )
            } else context.showNotificationMedicineIsOver(
                context.resources.getString(
                    R.string.notifications_title_no_medicines,
                    medicine.name
                )
            )
            remindersRepository.deleteTriggeredReminderById(triggeredReminderId)
        } catch (e: Exception) {
        }
    }

    suspend fun reminderSkip(reminder: TriggeredReminder) {
        reminderSkip(reminder.reminderId, reminder.id)
    }

    fun getTriggeredReminders(): Flow<List<TriggeredReminder>> {
        return remindersRepository.getAllTriggeredReminders()
    }

    suspend fun startReminder(reminder: Reminder) {
        val id = remindersRepository.insertReminders(reminder).firstOrNull()
        addNotifications(reminder.copy(id = id.toIntOrZero()))
    }

    private suspend fun addNotifications(reminder: Reminder) {
        val workerTag = reminder.id.toString()
        val intakes = reminder.intakes.sortedWith(compareBy({ it.hours }, { it.minutes }))

        val initialIntakeData = DateUtil.calculateNearestIntakeIndex(intakes)
        val startIntakeIndex = initialIntakeData.initialIntakeIndex
        val startDayDelta = initialIntakeData.initialDayDelta
        val startDate = Calendar.getInstance()

        for (i in 0 until reminder.duration * reminder.intakesAmount) {
            val currentIntake = intakes[(startIntakeIndex + i) % reminder.intakesAmount]
            val daysDelta = startDayDelta + (startIntakeIndex + i) / reminder.intakesAmount
            val millisDelta =
                DateUtil.calculateDelayInMillis(startDate.toTime(), currentIntake, daysDelta)

            val triggeredReminderId = remindersRepository.insertTriggeredReminders(
                TriggeredReminder(
                    reminderId = reminder.id,
                    medicineId = reminder.medicineId,
                    tradeName = reminder.tradeName + " ($currentIntake)",
                    imageRes = reminder.imageRes,
                    triggerTime = System.currentTimeMillis() + millisDelta
                )
            )
            val inputData = getInputData(
                reminder,
                currentIntake,
                triggeredReminderId.firstOrNull().toIntOrZero(),
                i == reminder.duration * reminder.intakesAmount - 1
            )
            val notificationWork =
                OneTimeWorkRequestBuilder<NotifyWorker>()
                    .setInputData(inputData)
                    .setInitialDelay(millisDelta, TimeUnit.MILLISECONDS)
                    .addTag(workerTag)
                    .build()
            workManager.enqueue(notificationWork)
        }
    }

    private fun getInputData(
        reminder: Reminder,
        time: Time,
        triggeredReminderId: Int,
        isLast: Boolean
    ): Data =
        Data.Builder()
            .putString(
                NotifyWorker.TITLE_KEY, context.resources.getString(
                    R.string.notifications_title_new_intake,
                    reminder.dose,
                    reminder.dosageForm,
                    reminder.tradeName
                )
            )
            .putInt(NotifyWorker.ID_KEY, reminder.id)
            .putString(NotifyWorker.BODY_KEY, time.toString())
            .putInt(NotifyWorker.TRIGGERED_REMINDER_ID_KEY, triggeredReminderId)
            .putBoolean(NotifyWorker.IS_LAST, isLast).build()

    suspend fun stopReminder(reminder: Reminder) {
        remindersRepository.updateReminder(reminder)
        removeNotifications(reminder)
    }

    private suspend fun removeNotifications(reminder: Reminder) {
        workManager.cancelAllWorkByTag(reminder.id.toString())
        remindersRepository.deleteTriggeredRemindersByReminderId(reminder.id)
    }

    suspend fun removeReminder(reminder: Reminder) {
        remindersRepository.deleteReminder(reminder)
        stopReminder(reminder)
    }

    suspend fun postponeNotification(reminderId: Int, triggeredReminderId: Int) {
        val reminder = remindersRepository.getReminderById(reminderId)
        val triggeredReminder = remindersRepository.getTriggeredReminderById(triggeredReminderId)
        val time = triggeredReminder.tradeName.split(" ").lastOrNull().toTime()
        val inputData = getInputData(reminder, time, triggeredReminderId, false)

        val notificationWork =
            OneTimeWorkRequestBuilder<NotifyWorker>()
                .setInputData(inputData)
                .setInitialDelay(5, TimeUnit.MINUTES)
                .addTag(reminderId.toString())
                .build()
        workManager.enqueue(notificationWork)
    }

}