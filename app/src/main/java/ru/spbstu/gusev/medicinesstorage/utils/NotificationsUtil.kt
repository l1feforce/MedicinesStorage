package ru.spbstu.gusev.medicinesstorage.utils

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.os.bundleOf
import androidx.navigation.NavDeepLinkBuilder
import ru.spbstu.gusev.medicinesstorage.MainActivity
import ru.spbstu.gusev.medicinesstorage.R
import ru.spbstu.gusev.medicinesstorage.broadcastreciever.NotificationActionsHandleReceiver
import ru.spbstu.gusev.medicinesstorage.ui.reminders.notificationactions.ActionIntakeCompleteFragment
import kotlin.random.Random

class NotificationsUtil {
    companion object {
        const val channelId = "reminders_channel_id"
        const val channelName = "Reminders"

        @SuppressLint("NewApi")
        private fun Context.createNotificationChannel() {
            val serviceChannel =
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    NotificationChannel(
                        channelId,
                        channelName, NotificationManager.IMPORTANCE_HIGH
                    )
                } else null
            val manager = getSystemService(
                NotificationManager::class.java
            )
            serviceChannel?.let {
                manager.createNotificationChannel(serviceChannel)
            }
        }

        fun Context.showNotificationMedicineWillEndSoon(title: String, body: String) {
            createNotificationChannel()

            val builder = getBaseNotification(title, body)

            NotificationManagerCompat.from(this).notify(Random(124).nextInt(), builder.build())
        }

        fun Context.showNotificationMedicineIsOver(title: String) {
            createNotificationChannel()

            val builder = getBaseNotification(title = title)

            NotificationManagerCompat.from(this).notify(Random(124).nextInt(), builder.build())
        }

        fun Context.showNotification(
            title: String,
            body: String,
            id: Int,
            triggeredReminderId: Int
        ) {
            createNotificationChannel()

            val actionIntakeCompleteIntent =
                Intent(this, NotificationActionsHandleReceiver::class.java).apply {
                    putExtra(NotificationActionsHandleReceiver.EXTRA_BUNDLE_KEY, bundleOf(
                        NotificationActionsHandleReceiver.IS_COMPLETE to true,
                        NotificationActionsHandleReceiver.REMINDER_ID to id,
                        NotificationActionsHandleReceiver.TRIGGERED_REMINDER_ID to triggeredReminderId)
                    )
                }
            val actionIntakeCompletePendingIntent =
                PendingIntent.getBroadcast(this, id, actionIntakeCompleteIntent, 0)

            val actionIntakeSkipIntent = Intent(this, NotificationActionsHandleReceiver::class.java).apply {
                putExtra(NotificationActionsHandleReceiver.EXTRA_BUNDLE_KEY, bundleOf(
                    NotificationActionsHandleReceiver.IS_COMPLETE to false,
                    NotificationActionsHandleReceiver.REMINDER_ID to id,
                    NotificationActionsHandleReceiver.TRIGGERED_REMINDER_ID to triggeredReminderId
                ))
            }
            val actionIntakeSkipPendingIntent = PendingIntent.getBroadcast(this, id, actionIntakeSkipIntent, 0)

            val builder = getBaseNotification(title, body)
                .addAction(
                    R.drawable.ic_medication_package,
                    resources.getString(R.string.notifications_action_intake_skip),
                    actionIntakeSkipPendingIntent
                )
                .addAction(
                    R.drawable.ic_medication_package,
                    resources.getString(R.string.notifications_action_intake_complete),
                    actionIntakeCompletePendingIntent
                )

            NotificationManagerCompat.from(this).notify(id, builder.build())
        }

        private fun Context.getBaseNotification(
            title: String = "",
            body: String = ""
        ): NotificationCompat.Builder {

            val contentPendingIntent = NavDeepLinkBuilder(this)
                .setComponentName(MainActivity::class.java)
                .setGraph(R.navigation.mobile_navigation).setDestination(R.id.navigation_reminders)
                .createPendingIntent()

            val builder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(contentPendingIntent)
                .setAutoCancel(true)

            if (title.isNotBlank()) builder.setContentTitle(title)
            if (body.isNotBlank()) builder.setContentText(body)

            return builder
        }
    }
}