package com.ketworie.wheep.client.contact

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.observe
import com.ketworie.wheep.client.R
import com.ketworie.wheep.client.ViewModelFactory
import com.ketworie.wheep.client.hideKeyboard
import com.ketworie.wheep.client.network.NetworkResponse
import com.ketworie.wheep.client.user.UserInfoFragment
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_contact_add.*
import javax.inject.Inject

class ContactAddActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var viewModelAdd: ContactAddActivityViewModel

    private val userInfoFragment: UserInfoFragment = UserInfoFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_add)
        viewModelAdd = ViewModelProvider(this, viewModelFactory).get()
        alias.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchUser()
                true
            } else
                false
        }
        search.setOnClickListener { searchUser() }
        supportFragmentManager.beginTransaction().add(R.id.userInfo, userInfoFragment).commit()
    }

    private fun searchUser() {
        val alias = alias.text.toString()
        if (alias.isBlank())
            return
        viewModelAdd.search(alias).observe(this) {
            when (it) {
                is NetworkResponse.Success -> {
                    userInfoFragment.submitUser(it.body)
                    hideKeyboard()
                }
                is NetworkResponse.ApiError -> userInfoFragment.submitText(it.body.message)
                else -> Toast.makeText(this, "Unknown error", Toast.LENGTH_SHORT).show()
            }
        }
    }
}