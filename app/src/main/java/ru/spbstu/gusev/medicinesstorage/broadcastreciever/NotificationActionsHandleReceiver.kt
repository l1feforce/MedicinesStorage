package ru.spbstu.gusev.medicinesstorage.broadcastreciever

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject
import ru.spbstu.gusev.medicinesstorage.data.local.notifications.NotificationsRepository

class NotificationActionsHandleReceiver : BroadcastReceiver() {
    companion object {
        const val EXTRA_BUNDLE_KEY = "extra_bundle_key"
        const val REMINDER_ID = "reminder_id_key"
        const val IS_COMPLETE = "is_complete_key"
        const val TRIGGERED_REMINDER_ID = "triggered_reminder_id_key"
    }

    private var reminderId: Int? = null
    private var isComplete: Boolean? = null
    private var triggeredReminderId: Int? = null
    val notificationsRepository: NotificationsRepository by inject(NotificationsRepository::class.java)


    override fun onReceive(context: Context, intent: Intent) {
        intent.getBundleExtra(EXTRA_BUNDLE_KEY)?.let {
            reminderId = it.getInt(REMINDER_ID)
            isComplete = it.getBoolean(IS_COMPLETE)
            triggeredReminderId = it.getInt(TRIGGERED_REMINDER_ID)
        }

        when {
            isComplete == true && reminderId != null -> {
                GlobalScope.launch {
                    notificationsRepository.reminderComplete(reminderId!!, triggeredReminderId!!)
                }
            }
            isComplete == false && reminderId != null -> {
                GlobalScope.launch {
                    notificationsRepository.reminderSkip(reminderId!!, triggeredReminderId!!)
                }
            }
        }
    }
}