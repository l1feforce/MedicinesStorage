package ru.spbstu.gusev.medicinesstorage.utils

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.os.bundleOf
import androidx.navigation.NavDeepLinkBuilder
import ru.spbstu.gusev.medicinesstorage.MainActivity
import ru.spbstu.gusev.medicinesstorage.R
import ru.spbstu.gusev.medicinesstorage.broadcastreciever.NotificationActionsHandleReceiver
import ru.spbstu.gusev.medicinesstorage.broadcastreciever.NotificationActionsHandleReceiver.Companion.EXTRA_BUNDLE_KEY
import ru.spbstu.gusev.medicinesstorage.broadcastreciever.NotificationActionsHandleReceiver.Companion.IS_COMPLETE
import ru.spbstu.gusev.medicinesstorage.broadcastreciever.NotificationActionsHandleReceiver.Companion.IS_DELAYED
import ru.spbstu.gusev.medicinesstorage.broadcastreciever.NotificationActionsHandleReceiver.Companion.REMINDER_ID
import ru.spbstu.gusev.medicinesstorage.broadcastreciever.NotificationActionsHandleReceiver.Companion.TRIGGERED_REMINDER_ID
import kotlin.random.Random

class NotificationsUtil {
    companion object {
        const val channelId = "reminders_channel_id"
        const val channelName = R.string.notifications_channel_name

        @SuppressLint("NewApi")
        private fun Context.createNotificationChannel() {
            val serviceChannel =
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    NotificationChannel(
                        channelId,
                        this.getString(channelName), NotificationManager.IMPORTANCE_HIGH
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

            NotificationManagerCompat.from(this).notify(Random.Default.nextInt(), builder.build())
        }

        fun Context.showNotificationMedicineIsOver(title: String) {
            createNotificationChannel()

            val builder = getBaseNotification(title = title)

            NotificationManagerCompat.from(this).notify(Random.Default.nextInt(), builder.build())
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
                    putExtra(
                        EXTRA_BUNDLE_KEY, bundleOf(
                            IS_COMPLETE to true,
                            REMINDER_ID to id,
                            TRIGGERED_REMINDER_ID to triggeredReminderId
                        )
                    )
                }
            val actionIntakeCompletePendingIntent =
                PendingIntent.getBroadcast(
                    this,
                    Random.Default.nextInt(),
                    actionIntakeCompleteIntent,
                    0
                )
            Log.d(
                "test", "showNotification: actionIntakeComplete: ${
                    actionIntakeCompleteIntent.getBundleExtra(
                        EXTRA_BUNDLE_KEY
                    )
                }"
            )

            val actionIntakePostpone =
                Intent(this, NotificationActionsHandleReceiver::class.java).apply {
                    putExtra(
                        EXTRA_BUNDLE_KEY, bundleOf(
                            IS_COMPLETE to false,
                            REMINDER_ID to id,
                            TRIGGERED_REMINDER_ID to triggeredReminderId,
                            IS_DELAYED to true
                        )
                    )
                }
            val actionIntakePostponePendingIntent =
                PendingIntent.getBroadcast(this, Random.Default.nextInt(), actionIntakePostpone, 0)
            Log.d(
                "test", "showNotification: actionIntakePostpone: ${
                    actionIntakePostpone.getBundleExtra(
                        EXTRA_BUNDLE_KEY
                    )
                }"
            )

            val actionIntakeSkipIntent =
                Intent(this, NotificationActionsHandleReceiver::class.java).apply {
                    putExtra(
                        EXTRA_BUNDLE_KEY, bundleOf(
                            IS_COMPLETE to false,
                            REMINDER_ID to id,
                            TRIGGERED_REMINDER_ID to triggeredReminderId
                        )
                    )
                }
            val actionIntakeSkipPendingIntent =
                PendingIntent.getBroadcast(
                    this,
                    Random.Default.nextInt(),
                    actionIntakeSkipIntent,
                    0
                )
            Log.d(
                "test", "showNotification: actionIntakeSkip: ${
                    actionIntakeSkipIntent.getBundleExtra(
                        EXTRA_BUNDLE_KEY
                    )
                }"
            )

            val builder = getBaseNotification(title, body)
                .addAction(
                    R.drawable.ic_medication_package,
                    resources.getString(R.string.notifications_action_intake_skip),
                    actionIntakeSkipPendingIntent
                )
                .addAction(
                    R.drawable.ic_medication_package,
                    resources.getString(R.string.notifications_action_intake_postpone),
                    actionIntakePostponePendingIntent
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
                .setSmallIcon(R.drawable.ic_date_range_24px)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(contentPendingIntent)

            if (title.isNotBlank()) builder.setContentTitle(title)
            if (body.isNotBlank()) builder.setContentText(body)

            return builder
        }
    }
}