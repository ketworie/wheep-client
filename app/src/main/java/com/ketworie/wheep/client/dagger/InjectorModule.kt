package com.ketworie.wheep.client.dagger

import com.ketworie.wheep.client.HomeActivity
import com.ketworie.wheep.client.SettingsActivity
import com.ketworie.wheep.client.SignInActivity
import com.ketworie.wheep.client.chat.ChatActivity
import com.ketworie.wheep.client.contact.ContactAddActivity
import com.ketworie.wheep.client.contact.ContactListFragment
import com.ketworie.wheep.client.event.EventBroadcastReceiver
import com.ketworie.wheep.client.event.EventStreamService
import com.ketworie.wheep.client.hub.HubAddActivity
import com.ketworie.wheep.client.hub.HubInfoActivity
import com.ketworie.wheep.client.hub.HubListFragment
import com.ketworie.wheep.client.user.UserInfoFragment
import com.ketworie.wheep.client.user.UserSelectorFragment
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

    @ContributesAndroidInjector
    abstract fun contributeUserInfoFragmentInjector(): UserInfoFragment?

    @ContributesAndroidInjector
    abstract fun contributeUserSelectorFragmentInjector(): UserSelectorFragment?

    @ContributesAndroidInjector
    abstract fun contributeAddContactActivityInjector(): ContactAddActivity?

    @ContributesAndroidInjector
    abstract fun contributeAddHubActivityInjector(): HubAddActivity?

    @ContributesAndroidInjector
    abstract fun contributeHubInfoActivityInjector(): HubInfoActivity?

    @ContributesAndroidInjector
    abstract fun contributeEventBroadcastReceiverInjector(): EventBroadcastReceiver?

    @ContributesAndroidInjector
    abstract fun contributeEventStreamServiceInjector(): EventStreamService?

}