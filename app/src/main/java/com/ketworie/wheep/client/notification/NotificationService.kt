package com.ketworie.wheep.client.notification

import android.app.Notification
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.ketworie.wheep.client.MainApplication
import com.ketworie.wheep.client.R
import com.ketworie.wheep.client.chat.Message
import com.ketworie.wheep.client.event.Event
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationService @Inject constructor() {

    fun notify(context: Context, e: Event) {
        val builder = NotificationCompat.Builder(context, MainApplication.CHANNEL_ID)
        val notification = when (e.body) {
            is Message -> notify(builder, e.body)
            else -> null
        }
        notification?.let { NotificationManagerCompat.from(context).notify(1, it) }
    }

    private fun notify(
        builder: NotificationCompat.Builder,
        m: Message
    ): Notification {
        val notification = builder
            .setSmallIcon(R.drawable.accent_circle)
            .setContentTitle(m.userId)
            .setContentText(m.text)
            .build()
        return notification
    }
}