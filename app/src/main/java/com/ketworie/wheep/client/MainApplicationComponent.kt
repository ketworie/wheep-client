package com.ketworie.wheep.client

import com.ketworie.wheep.client.chat.RetrofitModule
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidInjectionModule::class, InjectorModule::class, RetrofitModule::class])
interface MainApplicationComponent : AndroidInjector<MainApplication> {
}