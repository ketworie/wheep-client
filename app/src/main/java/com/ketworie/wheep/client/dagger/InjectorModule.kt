package com.ketworie.wheep.client.dagger

import com.ketworie.wheep.client.HomeActivity
import com.ketworie.wheep.client.SettingsActivity
import com.ketworie.wheep.client.SignInActivity
import com.ketworie.wheep.client.chat.ChatActivity
import com.ketworie.wheep.client.hub.HubListFragment
import com.ketworie.wheep.client.notebook.ContactListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class InjectorModule {
    @ContributesAndroidInjector
    abstract fun contributeSignInActivityInjector(): SignInActivity?

    @ContributesAndroidInjector
    abstract fun contributeMessageActivityInjector(): HomeActivity?

    @ContributesAndroidInjector
    abstract fun contributeSettingsActivityInjector(): SettingsActivity?

    @ContributesAndroidInjector
    abstract fun contributeChatActivityInjector(): ChatActivity?

    @ContributesAndroidInjector
    abstract fun contributeHubListFragmentInjector(): HubListFragment?

    @ContributesAndroidInjector
    abstract fun contributeContactListFragmentInjector(): ContactListFragment?

}