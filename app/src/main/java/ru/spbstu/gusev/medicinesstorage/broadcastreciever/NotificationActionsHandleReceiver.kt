package ru.spbstu.gusev.medicinesstorage.broadcastreciever

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject
import ru.spbstu.gusev.medicinesstorage.data.local.notifications.NotificationsService

class NotificationActionsHandleReceiver : BroadcastReceiver() {
    companion object {
        const val EXTRA_BUNDLE_KEY = "extra_bundle_key"
        const val REMINDER_ID = "reminder_id_key"
        const val IS_COMPLETE = "is_complete_key"
        const val TRIGGERED_REMINDER_ID = "triggered_reminder_id_key"
        const val IS_DELAYED = "is_delayed_key"
    }

    var reminderId: Int? = null
    var isComplete: Boolean = false
    var triggeredReminderId: Int? = null
    var isDelayed: Boolean = false

    override fun onReceive(context: Context, intent: Intent) {

        intent.getBundleExtra(EXTRA_BUNDLE_KEY)?.let {
            reminderId = it.getInt(REMINDER_ID)
            isComplete = it.getBoolean(IS_COMPLETE, false)
            triggeredReminderId = it.getInt(TRIGGERED_REMINDER_ID)
            isDelayed = it.getBoolean(IS_DELAYED, false)
        }
        NotificationManagerCompat.from(context).cancel(reminderId ?: 0)
        val notificationsRepository by inject(NotificationsService::class.java)

        GlobalScope.launch(Dispatchers.IO) {
            when {
                isDelayed -> {
                    Log.d("test", "onReceive: isDelayed")
                    notificationsRepository.postponeNotification(
                        reminderId!!,
                        triggeredReminderId!!
                    )
                }
                isComplete -> {
                    Log.d("test", "onReceive: isComplete")
                    notificationsRepository.reminderComplete(reminderId!!, triggeredReminderId!!)
                }
                !isComplete -> {
                    Log.d("test", "onReceive: isSkip")
                    notificationsRepository.reminderSkip(reminderId!!, triggeredReminderId!!)
                }
            }
        }
    }
}