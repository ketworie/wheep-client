package com.ketworie.wheep.client.dagger

import com.ketworie.wheep.client.SettingsActivity
import com.ketworie.wheep.client.SignInActivity
import com.ketworie.wheep.client.chat.ChatActivity
import com.ketworie.wheep.client.hub.activity.HubListActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class InjectorModule {
    @ContributesAndroidInjector
    abstract fun contributeSignInActivityInjector(): SignInActivity?

    @ContributesAndroidInjector
    abstract fun contributeMessageActivityInjector(): HubListActivity?

    @ContributesAndroidInjector
    abstract fun contributeSettingsActivityInjector(): SettingsActivity?

    @ContributesAndroidInjector
    abstract fun contributeChatActivityInjector(): ChatActivity?

}