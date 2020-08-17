package com.ketworie.wheep.client.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.observe
import com.bumptech.glide.Glide
import com.ketworie.wheep.client.MainApplication.Companion.RESOURCE_BASE
import com.ketworie.wheep.client.R
import com.ketworie.wheep.client.ViewModelFactory
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_user_info.*
import javax.inject.Inject

class UserInfoFragment() : Fragment() {

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
    }

    fun submitUser(user: User) {
        viewModel.isContact(user.id).observe(this.viewLifecycleOwner) {
            transformContactButton(it)
        }
        name.text = user.name
        alias.text = user.alias
        Glide.with(this)
            .asBitmap()
            .circleCrop()
            .load(RESOURCE_BASE + user.image)
            .into(avatar)
        this.user = user
    }

    fun submitText(text: String) {
        avatar.setImageDrawable(null)
        name.text = text.capitalize()
        alias.text = ""
        contactManagement.visibility = View.INVISIBLE
    }

    private fun transformContactButton(isContact: Boolean) {
        contactManagement.visibility = View.VISIBLE
        if (isContact) {
            contactManagement.text = resources.getString(R.string.remove_from_contacts)
            contactManagement.setTextColor(resources.getColor(R.color.red, activity?.theme))
            contactManagement.background =
                resources.getDrawable(R.drawable.white_border, activity?.theme)
        } else {
            contactManagement.text = resources.getString(R.string.add_to_contacts)
            contactManagement.setTextColor(resources.getColor(R.color.white, activity?.theme))
            contactManagement.background =
                resources.getDrawable(R.drawable.white_border, activity?.theme)
        }
    }
}