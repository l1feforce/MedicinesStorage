package ru.spbstu.gusev.medicinesstorage.data.local.remiders

import android.content.Context
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import kotlinx.coroutines.flow.Flow
import ru.spbstu.gusev.medicinesstorage.R
import ru.spbstu.gusev.medicinesstorage.data.local.MedicinesRepository
import ru.spbstu.gusev.medicinesstorage.extensions.toIntOrZero
import ru.spbstu.gusev.medicinesstorage.models.Reminder
import ru.spbstu.gusev.medicinesstorage.models.Time
import ru.spbstu.gusev.medicinesstorage.models.TriggeredReminder
import ru.spbstu.gusev.medicinesstorage.utils.DateUtil
import ru.spbstu.gusev.medicinesstorage.utils.DateUtil.Companion.toTime
import ru.spbstu.gusev.medicinesstorage.utils.NotificationsUtil.Companion.showNotificationMedicineIsOver
import ru.spbstu.gusev.medicinesstorage.utils.NotificationsUtil.Companion.showNotificationMedicineWillEndSoon
import java.util.*
import java.util.concurrent.TimeUnit

class RemindersRepository(
    private val medicinesRepository: MedicinesRepository,
    private val workManager: WorkManager,
    private val context: Context
) {

    suspend fun reminderComplete(id: Int, triggeredReminderId: Int) {
        Log.d("test", "reminderComplete: id: $id, triggeredReminderId: $triggeredReminderId")
        try {
            val reminder = medicinesRepository.getReminderById(id)
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
            medicinesRepository.deleteTriggeredReminderById(triggeredReminderId)
            NotificationManagerCompat.from(context).cancel(id)
        } catch (e: Exception) {
        }
    }

    suspend fun reminderComplete(reminder: TriggeredReminder) {
        reminderComplete(reminder.reminderId, reminder.id)
    }

    suspend fun reminderSkip(id: Int, triggeredReminderId: Int) {
        try {
            val reminder = medicinesRepository.getReminderById(id)
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
            medicinesRepository.deleteTriggeredReminderById(triggeredReminderId)
            NotificationManagerCompat.from(context).cancel(id)
        } catch (e: Exception) {
        }
    }

    suspend fun reminderSkip(reminder: TriggeredReminder) {
        reminderSkip(reminder.reminderId, reminder.id)
    }

    fun getTriggeredReminders(): Flow<List<TriggeredReminder>> {
        return medicinesRepository.getAllTriggeredReminders()
    }

    suspend fun startReminder(reminder: Reminder) {
        val id = medicinesRepository.insertReminders(reminder).firstOrNull()
        addNotifications(reminder.copy(id = id?.toInt() ?: 0))
    }

    private suspend fun addNotifications(reminder: Reminder) {
        val workerTag = StringBuilder(reminder.id.toString())
        val intakes = reminder.intakes.sortedWith(compareBy({ it.hours }, { it.minutes }))

        var startIntakeIndex = DateUtil.calculateNearestIntakeIndex(intakes)
        Log.d("test", "addNotifications: startIntakeIndex: $startIntakeIndex")
        val startDayDelta = if (startIntakeIndex == null) {
            startIntakeIndex = 0
            1
        } else 0
        val startDate = Calendar.getInstance()
        Log.d(
            "test",
            "addNotifications: startDayDelta: $startDayDelta, duration: ${reminder.duration}, intakesAmount: ${reminder.intakesAmount}"
        )

        for (i in 0 until reminder.duration * reminder.intakesAmount) {
            val currentIntake = intakes[(startIntakeIndex + i) % reminder.intakesAmount]
            val daysDelta = startDayDelta + (startIntakeIndex + i) / reminder.intakesAmount
            Log.d("test", "addNotifications: currentIntake: $currentIntake, daysDelta: $daysDelta")
            val millisDelta =
                DateUtil.calculateDelayInMillis(startDate.toTime(), currentIntake, daysDelta)

            val triggeredReminderId = medicinesRepository.insertTriggeredReminders(
                TriggeredReminder(
                    reminderId = reminder.id,
                    medicineId = reminder.medicineId,
                    tradeName = reminder.tradeName + " ($currentIntake)",
                    imageRes = reminder.imageRes,
                    triggerTime = System.currentTimeMillis() + millisDelta
                )
            )
            Log.d(
                "test",
                "addNotifications: triggeredReminderId: $triggeredReminderId \n reminderId: ${reminder.id}"
            )
            val inputData = getInputData(
                reminder,
                currentIntake,
                triggeredReminderId.firstOrNull()?.toInt() ?: 0,
                i == reminder.duration * reminder.intakesAmount - 1
            )
            Log.d("test", "addNotifications: time: ${millisDelta / 1000 / 60 / 60}")
            val notificationWork =
                OneTimeWorkRequestBuilder<NotifyWorker>()
                    .setInputData(inputData)
                    .setInitialDelay(millisDelta, TimeUnit.MILLISECONDS)
                    .addTag(workerTag.toString())
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
        medicinesRepository.updateReminder(reminder)
        removeNotifications(reminder)
    }

    private suspend fun removeNotifications(reminder: Reminder) {
        workManager.cancelAllWorkByTag(reminder.id.toString())
        medicinesRepository.deleteTriggeredRemindersByReminderId(reminder.id)
    }

    suspend fun removeReminder(reminder: Reminder) {
        medicinesRepository.deleteReminder(reminder)
        stopReminder(reminder)
    }

}