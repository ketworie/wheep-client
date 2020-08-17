package com.ketworie.wheep.client.contact

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.observe
import com.ketworie.wheep.client.R
import com.ketworie.wheep.client.ViewModelFactory
import com.ketworie.wheep.client.network.NetworkResponse
import com.ketworie.wheep.client.user.UserInfoFragment
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_add_contact.*
import javax.inject.Inject

class AddContactActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var viewModel: AddContactActivityViewModel

    private val userInfoFragment: UserInfoFragment = UserInfoFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_contact)
        viewModel = ViewModelProvider(this, viewModelFactory).get()
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
        viewModel.search(alias).observe(this) {
            when (it) {
                is NetworkResponse.Success -> userInfoFragment
                    .submitUser(it.body)
                is NetworkResponse.ApiError -> userInfoFragment.submitText(it.body.message)
                else -> Toast.makeText(this, "Unknown error", Toast.LENGTH_SHORT).show()
            }
        }
    }
}