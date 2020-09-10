package com.ketworie.wheep.client.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.observe
import androidx.transition.TransitionManager
import com.ketworie.wheep.client.R
import com.ketworie.wheep.client.ViewModelFactory
import com.ketworie.wheep.client.image.loadAvatar
import com.ketworie.wheep.client.network.toastError
import com.ketworie.wheep.client.observeOnce
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_user_info.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserInfoFragment() : Fragment() {

    @Inject
    lateinit var userService: UserService

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var viewModel: UserInfoFragmentViewModel
    private var user: User? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get()
        addContact.setOnClickListener { addContact() }
        removeContact.setOnClickListener { removeContact() }
    }

    fun submitUserId(id: String) {
        userService.getUser(id).observe(viewLifecycleOwner) {
            submitUser(it)
        }
    }

    fun submitUser(user: User) {
        name.text = user.name
        alias.text = user.alias
        this.context?.let {
            loadAvatar(
                it,
                avatar,
                user.image
            )
        }
        viewModel.isContact(user.id).observeOnce(this.viewLifecycleOwner) {
            transformContactButton(it)
        }
        this.user = user
    }

    fun submitText(text: String) {
        avatar.setImageDrawable(null)
        name.text = text.capitalize()
        alias.text = ""
        addContact.visibility = View.INVISIBLE
        removeContact.visibility = View.INVISIBLE
    }

    private fun animateContactButton(isContact: Boolean) {
        TransitionManager.beginDelayedTransition(root)
        transformContactButton(isContact)
    }

    private fun transformContactButton(isContact: Boolean) {
        if (isContact) {
            addContact.visibility = View.INVISIBLE
            removeContact.visibility = View.VISIBLE
        } else {
            addContact.visibility = View.VISIBLE
            removeContact.visibility = View.INVISIBLE
        }
    }

    private fun addContact() {
        val user = user ?: return
        addContact.isEnabled = false
        CoroutineScope(Dispatchers.IO).launch {
            val hasError = userService.addContact(user).toastError(requireActivity())
            withContext(Dispatchers.Main) {
                addContact.isEnabled = true
                animateContactButton(!hasError)
            }
        }
    }

    private fun removeContact() {
        val user = user ?: return
        removeContact.isEnabled = false
        CoroutineScope(Dispatchers.IO).launch {
            val hasError = userService.removeContact(user).toastError(requireActivity())
            withContext(Dispatchers.Main) {
                removeContact.isEnabled = true
                animateContactButton(hasError)
            }
        }
    }
}