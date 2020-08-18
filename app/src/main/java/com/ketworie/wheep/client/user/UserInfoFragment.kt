package com.ketworie.wheep.client.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.observe
import com.ketworie.wheep.client.R
import com.ketworie.wheep.client.ViewModelFactory
import com.ketworie.wheep.client.loadAvatar
import com.ketworie.wheep.client.network.GenericError
import com.ketworie.wheep.client.network.NetworkResponse
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_user_info.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
        viewModel.isContact(user.id).observe(this.viewLifecycleOwner) {
            transformContactButton(it)
        }
        name.text = user.name
        alias.text = user.alias
        this.context?.let { loadAvatar(it, avatar, user.image) }
        this.user = user
    }

    fun submitText(text: String) {
        avatar.setImageDrawable(null)
        name.text = text.capitalize()
        alias.text = ""
        addContact.visibility = View.INVISIBLE
        removeContact.visibility = View.INVISIBLE
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
            handleError(userService.addContact(user.id))
            requireActivity().runOnUiThread { addContact.isEnabled = true }
        }
    }

    private fun removeContact() {
        val user = user ?: return
        removeContact.isEnabled = false
        CoroutineScope(Dispatchers.IO).launch {
            handleError(userService.removeContact(user.id))
            requireActivity().runOnUiThread { removeContact.isEnabled = true }
        }
    }

    private fun handleError(error: GenericError<Unit>) {
        when (val response = error) {
            is NetworkResponse.ApiError -> toast(response.body.message)
            is NetworkResponse.NetworkError -> toast(resources.getString(R.string.network_error))
            is NetworkResponse.UnknownError -> toast(resources.getString(R.string.unknown_error))
        }
    }

    private fun toast(text: String) {
        requireActivity().runOnUiThread {
            Toast.makeText(this@UserInfoFragment.context, text.capitalize(), Toast.LENGTH_SHORT)
                .show()
        }
    }
}