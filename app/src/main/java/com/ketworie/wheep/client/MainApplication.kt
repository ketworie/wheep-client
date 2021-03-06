package com.ketworie.wheep.client

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import com.ketworie.wheep.client.dagger.DaggerMainApplicationComponent
import com.ketworie.wheep.client.dagger.DataModule
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication


class MainApplication : DaggerApplication() {
    companion object {
        const val X_AUTH_TOKEN = "X-Auth-Token"
        const val USER_ID = "userId"
        const val PREFERENCES = "preferences"
        const val SERVER_BASE = "http://10.0.2.2"
        const val STOMP_SERVER = "http://185.162.10.137:8335/ws"
        const val WEB_SERVER = "$SERVER_BASE:8080"
        const val RESOURCE_BASE = "$WEB_SERVER/wayne/"
        const val HUB_ID = "hubId"
        const val IMAGE_TYPE = "imageType"
        const val IMAGE_PATH = "imagePath"
        const val USER_IDS = "userIds"
        const val REQUEST_AVATAR = 101
        const val CHANNEL_ID = "WHP"
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerMainApplicationComponent
            .builder()
            .dataModule(DataModule(this))
            .build()
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        val name = getString(R.string.channel_name)
        val descriptionText = getString(R.string.channel_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        // Register the channel with the system
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}