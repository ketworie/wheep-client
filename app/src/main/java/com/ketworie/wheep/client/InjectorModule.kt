package com.ketworie.wheep.client

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class InjectorModule {
    @ContributesAndroidInjector
    abstract fun contributeSignInActivityInjector(): SignInActivity?

    @ContributesAndroidInjector
    abstract fun contributeMessageActivityInjector(): MessageActivity?

    @ContributesAndroidInjector
    abstract fun contributeSettingsActivityInjector(): SettingsActivity?

}