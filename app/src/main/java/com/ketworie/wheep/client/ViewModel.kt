package com.ketworie.wheep.client

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ketworie.wheep.client.chat.ChatActivityViewModel
import com.ketworie.wheep.client.contact.ContactAddActivityViewModel
import com.ketworie.wheep.client.contact.ContactListFragmentViewModel
import com.ketworie.wheep.client.hub.HubListFragmentViewModel
import com.ketworie.wheep.client.user.UserInfoFragmentViewModel
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton
import kotlin.reflect.KClass


@Singleton
class ViewModelFactory @Inject constructor(private val viewModels: MutableMap<Class<out ViewModel>, Provider<ViewModel>>) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        viewModels[modelClass]?.get() as T
}

@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@MapKey
internal annotation class ViewModelKey(val value: KClass<out ViewModel>)

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(HomeActivityViewModel::class)
    internal abstract fun postListViewModel(viewModel: HomeActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChatActivityViewModel::class)
    internal abstract fun postChatViewModel(viewModel: ChatActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HubListFragmentViewModel::class)
    internal abstract fun postHubListFragmentViewModel(viewModel: HubListFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ContactListFragmentViewModel::class)
    internal abstract fun postContactListFragmentViewModel(viewModel: ContactListFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UserInfoFragmentViewModel::class)
    internal abstract fun postUserInfoFragmentViewModel(viewModel: UserInfoFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ContactAddActivityViewModel::class)
    internal abstract fun postAddContactActivityViewModel(viewModelAdd: ContactAddActivityViewModel): ViewModel


}