package com.ketworie.wheep.client

import android.app.Application
import com.ketworie.wheep.client.dagger.DaggerMainApplicationComponent
import com.ketworie.wheep.client.dagger.DataModule
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject


class MainApplication : Application(), HasAndroidInjector {
    companion object {
        const val X_AUTH_TOKEN = "X-Auth-Token"
        const val USER_ID = "userId"
        const val PREFERENCES = "preferences"
        const val SERVER_BASE = "http://10.0.2.2:8080"
        const val RESOURCE_BASE = "$SERVER_BASE/wayne/"
        const val IS_NEW_SESSION = "isNewSession"
        const val HUB_ID = "hubId"
    }

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>


    override fun onCreate() {
        super.onCreate()
        DaggerMainApplicationComponent
            .builder()
            .dataModule(DataModule(this))
            .build()
            .inject(this)
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return dispatchingAndroidInjector
    }
}