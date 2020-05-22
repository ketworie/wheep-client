package com.ketworie.wheep.client.dagger

import com.ketworie.wheep.client.MainApplication
import com.ketworie.wheep.client.ViewModelModule
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidInjectionModule::class, InjectorModule::class, DataModule::class, ViewModelModule::class])
interface MainApplicationComponent : AndroidInjector<MainApplication> {
}