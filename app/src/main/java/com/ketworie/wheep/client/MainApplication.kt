package com.ketworie.wheep.client

import android.app.Application
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject


class MainApplication : Application(), HasAndroidInjector {
    companion object {
        const val X_AUTH_TOKEN = "X-Auth-Token"
        const val SERVER_BASE = "http://10.0.2.2:8080"
        const val RESOURCE_BASE = "$SERVER_BASE/wayne/"
    }

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>


    override fun onCreate() {
        super.onCreate()
        DaggerMainApplicationComponent.create().inject(this)
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return dispatchingAndroidInjector
    }
}