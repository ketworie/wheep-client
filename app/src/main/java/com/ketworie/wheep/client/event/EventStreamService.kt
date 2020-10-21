package com.ketworie.wheep.client.event

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.IBinder
import android.os.SystemClock
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.fasterxml.jackson.databind.ObjectMapper
import com.ketworie.wheep.client.MainApplication.Companion.STOMP_SERVER
import com.ketworie.wheep.client.MainApplication.Companion.X_AUTH_TOKEN
import com.ketworie.wheep.client.network.NetworkResponse
import dagger.android.AndroidInjection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.delay
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.stomp.config.HeartBeat
import org.hildan.krossbow.stomp.config.StompConfig
import org.hildan.krossbow.stomp.conversions.subscribe
import org.hildan.krossbow.stomp.conversions.withJacksonConversions
import org.hildan.krossbow.websocket.okhttp.OkHttpWebSocketClient
import java.time.Duration
import javax.inject.Inject

class EventStreamService : Service() {

    val client: StompClient = StompClient(
        OkHttpWebSocketClient(),
        StompConfig().apply { heartBeat = HeartBeat(30000, 30000) })

    @Inject
    lateinit var objectMapper: ObjectMapper

    @Inject
    lateinit var eventService: EventService
    lateinit var connectivityManager: ConnectivityManager
    private var token = ""
    private var messageJob: Job? = null
    private val alarmMgr = getSystemService(Context.ALARM_SERVICE) as AlarmManager

    override fun onCreate() {
        AndroidInjection.inject(this)
        Log.d("MSG", "Service created")
        connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.registerDefaultNetworkCallback(NetworkListener())
        ProcessLifecycleOwner.get().lifecycle.addObserver(ProcessLifecycleListener())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        token = intent?.getStringExtra(X_AUTH_TOKEN) ?: ""
        if (token.isBlank()) {
            stopJob()
        }
        return START_REDELIVER_INTENT
    }

    private fun startJob() {
        if (messageJob == null || messageJob?.isActive == false)
            messageJob = CoroutineScope(Dispatchers.IO).launch { subscribe() }
    }

    private fun stopJob() {
        messageJob?.cancel()
    }

    private suspend fun subscribe() {
        if (token.isBlank())
            return
        val hasInternet =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) ?: false
        if (!hasInternet)
            return
        if (eventService.setupQueue() !is NetworkResponse.Success) {
            delay(Duration.ofMinutes(1))
            Log.d("MSR", "Retry after unsuccessful setup")
            subscribe()
            return
        }
        try {
            val session = client.connect(STOMP_SERVER, "biba", "4815162342", mapOf("host" to "/"))
                .withJacksonConversions(objectMapper)
            val subscribe = session.subscribe("/amq/queue/q-$token", Event::class)
            subscribe.collect {
                eventService.save(it)
                Log.d("MSG", "Received event ${it.date}")
            }
        } catch (e: Exception) {
            Log.d("MSG", "Exception during message receiving", e)
            delay(Duration.ofMinutes(1))
            subscribe()
            return
        }
    }

    private fun startReceiver() {
        val alarmIntent = Intent(this, EventBroadcastReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(this, 0, intent, 0)
        }
        alarmMgr.set(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + 1000,
            alarmIntent
        )
    }

    private fun stopReceiver() {
        val alarmIntent = Intent(this, EventBroadcastReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(this, 0, intent, 0)
        }
        alarmMgr.cancel(alarmIntent)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        Log.d("MST", "Service killed")
    }

    inner class NetworkListener : ConnectivityManager.NetworkCallback() {

        override fun onLosing(network: Network, maxMsToLive: Int) {
            Log.d("MSG", "About to lose network in $maxMsToLive ms")
            stopJob()
        }

        override fun onLost(network: Network) {
            Log.d("MSG", "Network lost")
            stopJob()
        }

        override fun onAvailable(network: Network) {
            Log.d("MSG", "Network available")
            stopJob()
        }

        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            val hasInternet =
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            Log.d("MSG", "Has internet: $hasInternet")
            if (!hasInternet)
                return
            startJob()
        }
    }

    inner class ProcessLifecycleListener : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        fun onStart() {
            Log.d("MSG", "App started")
            startJob()
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        fun onStop() {
            Log.d("MSG", "App stopped")
            stopJob()
        }
    }

}