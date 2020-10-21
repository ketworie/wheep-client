package com.ketworie.wheep.client.event

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.ketworie.wheep.client.MainApplication.Companion.CHANNEL_ID
import com.ketworie.wheep.client.R
import com.ketworie.wheep.client.chat.Message
import dagger.android.DaggerBroadcastReceiver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import javax.inject.Inject

class EventBroadcastReceiver : DaggerBroadcastReceiver() {

    @Inject
    lateinit var eventService: EventService

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        val dateTime = intent?.getSerializableExtra("date") as ZonedDateTime? ?: ZonedDateTime.now()
        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            val lastEvent = eventService.checkLast(dateTime)
            if (lastEvent != null) {
                val builder = NotificationCompat.Builder(context!!, CHANNEL_ID)
                    .setSmallIcon(R.drawable.accent_circle)
                    .setContentTitle("New message")
                    .setContentText((lastEvent.body as Message).text)
                NotificationManagerCompat.from(context).notify(1, builder.build())
            }
            reschedule(context!!)
            pendingResult.finish()
        }
    }

    private fun reschedule(context: Context) {
        val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(context, EventBroadcastReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(context, 0, intent, 0)
        }
        alarmMgr.set(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + 1000,
            alarmIntent
        )

    }
}