package com.myradio.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.myradio.R
import com.myradio.utils.Constants.CHANNEL_ID
import com.myradio.utils.Constants.CHANNEL_NAME
import com.myradio.utils.Constants.NOTIFICATION_TITLE
import com.myradio.utils.Constants.VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION

private const val  TAG = "NotificationHelper"
object NotificationHelper {

    fun makeStatusNotification(message: String,pendingIntent: PendingIntent, context: Context):NotificationCompat.Builder {
        // Make a channel if necessary
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            val name = CHANNEL_NAME
            val description = VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = description

            // Add the channel
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(channel)
        }

        // Create the notification
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.music)
            .setContentTitle(NOTIFICATION_TITLE)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVibrate(LongArray(0))

        pendingIntent?.let {
            builder.setContentIntent(pendingIntent)
        }

        return builder;

        // Show the notification
        //NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, builder.build())
    }

}
