package ru.spbstu.gusev.medicinesstorage.data.local.remiders

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import ru.spbstu.gusev.medicinesstorage.utils.NotificationsUtil.Companion.showNotification

class NotifyWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {
    companion object {
        const val TITLE_KEY = "title"
        const val BODY_KEY = "body"
        const val ID_KEY = "id"
        const val TRIGGERED_REMINDER_ID_KEY = "triggered_reminder_id"
    }

    override suspend fun doWork(): Result {
        val title = inputData.getString(TITLE_KEY) ?: "Title"
        val body = inputData.getString(BODY_KEY) ?: "Body"
        val id = inputData.getInt(ID_KEY, 0)
        val triggeredReminderId = inputData.getInt(TRIGGERED_REMINDER_ID_KEY, 0)
        Log.d("test", "doWork: triggeredReminderId: $triggeredReminderId")
        applicationContext.showNotification(title, body, id, triggeredReminderId)

        return Result.success()
    }

}