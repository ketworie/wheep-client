package com.ketworie.wheep.client.event

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.SystemClock
import com.ketworie.wheep.client.notification.NotificationService
import dagger.android.DaggerBroadcastReceiver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import javax.inject.Inject

class EventBroadcastReceiver : DaggerBroadcastReceiver() {

    @Inject
    lateinit var eventService: EventService

    @Inject
    lateinit var notificationService: NotificationService
    lateinit var connectivityManager: ConnectivityManager
    private val defaultRate = 5000
    private var minRate = 12 * 60 * 60 * 1000

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        connectivityManager =
            context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var dateTime = intent?.getSerializableExtra("date") as ZonedDateTime? ?: ZonedDateTime.now()
        val rate = intent?.getIntExtra("rate", defaultRate)!!
        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            val hasInternet =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                    ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) ?: false
            if (!hasInternet) {
                reschedule(context, rate, dateTime)
                pendingResult.finish()
                return@launch
            }
            val lastEvent = eventService.checkLast(dateTime)
            if (lastEvent != null) {
                notificationService.notify(context, lastEvent)
                dateTime = lastEvent.date
            }
//            rate = if (lastEvent == null && rate < minRate) rate * 2 else defaultRate
            reschedule(context, rate, dateTime)
            pendingResult.finish()
        }
    }

    private fun reschedule(context: Context, rate: Int, dateTime: ZonedDateTime?) {
        val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(context, EventBroadcastReceiver::class.java).let { intent ->
            intent.putExtra("rate", rate)
            intent.putExtra("date", dateTime)
            PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }
        alarmMgr.set(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + rate,
            alarmIntent
        )

    }
}