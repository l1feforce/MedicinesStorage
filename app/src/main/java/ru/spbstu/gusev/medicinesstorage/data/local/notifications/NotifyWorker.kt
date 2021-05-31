package ru.spbstu.gusev.medicinesstorage.data.local.notifications

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import org.koin.java.KoinJavaComponent.inject
import ru.spbstu.gusev.medicinesstorage.data.local.reminders.RemindersRepository
import ru.spbstu.gusev.medicinesstorage.utils.NotificationsUtil.Companion.showNotification

class NotifyWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {
    companion object {
        const val TITLE_KEY = "title"
        const val BODY_KEY = "body"
        const val ID_KEY = "id"
        const val TRIGGERED_REMINDER_ID_KEY = "triggered_reminder_id"
        const val IS_LAST = "is_last_key"
    }

    override suspend fun doWork(): Result {
        val title = inputData.getString(TITLE_KEY) ?: "Title"
        val body = inputData.getString(BODY_KEY) ?: "Body"
        val id = inputData.getInt(ID_KEY, 0)
        val triggeredReminderId = inputData.getInt(TRIGGERED_REMINDER_ID_KEY, 0)
        val isLast = inputData.getBoolean(IS_LAST, false)
        Log.d("test", "doWork: triggeredReminderId: $triggeredReminderId")
        applicationContext.showNotification(title, body, id, triggeredReminderId)
        if (isLast) {
            changeIsStartedFlag(id)
        }
        return Result.success()
    }

    private suspend fun changeIsStartedFlag(reminderId: Int) {
        val remindersRepository by inject(RemindersRepository::class.java)
        val oldReminder = remindersRepository.getReminderById(reminderId)
        remindersRepository.updateReminder(oldReminder.copy(isStarted = false))
    }

}