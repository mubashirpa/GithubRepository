package com.evaluation.githubrepository.presentation.core

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import com.evaluation.githubrepository.R
import com.evaluation.githubrepository.presentation.main.MainActivity

class NotificationBuilder(
    private val context: Context,
) {
    fun showNotification(
        title: String?,
        body: String,
    ) {
        val intent =
            Intent(context, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            }

        val pendingIntent =
            PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE,
            )

        val channelId = context.getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val builder =
            NotificationCompat
                .Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)

        title?.let { builder.setContentTitle(it) }

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(0, builder.build())
    }
}
