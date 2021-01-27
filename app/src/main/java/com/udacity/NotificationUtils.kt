package com.udacity

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat




fun NotificationManager.sendNotification(
    messageBody: String,
    applicationContext: Context,
    pendingIntent: PendingIntent
) {

    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.channelId)
    )
        .setSmallIcon(R.drawable.ic_launcher_background)
     //   .setLargeIcon(BitmapFactory.decodeResource(applicationContext.resources,R.drawable.ic_launcher_background))
        .setContentTitle(
            applicationContext
                .getString(R.string.notification_title)
        )
        .setContentText(messageBody)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)
        .addAction(R.drawable.ic_assistant_black_24dp, "Check the status", pendingIntent)

    notify(1234, builder.build())
    Log.d("NotUtil","OK")
}