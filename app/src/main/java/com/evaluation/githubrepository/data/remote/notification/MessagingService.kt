package com.evaluation.githubrepository.data.remote.notification

import android.util.Log
import com.evaluation.githubrepository.presentation.core.NotificationBuilder
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.koin.android.ext.android.inject

class MessagingService : FirebaseMessagingService() {
    private val notificationBuilder: NotificationBuilder by inject()

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "Refreshed token: $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        message.notification?.let { notification ->
            val title = notification.title
            val body = notification.body

            body?.let {
                notificationBuilder.showNotification(title, body)
            }
        }
    }

    companion object {
        val TAG = MessagingService::class.simpleName
    }
}
