package com.ketworie.wheep.client.user

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ketworie.wheep.client.MainApplication.Companion.USER_ID
import com.ketworie.wheep.client.R

class UserInfoActivity : AppCompatActivity() {

    private val userInfoFragment = UserInfoFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)
        supportFragmentManager.beginTransaction().add(R.id.fragment, userInfoFragment).runOnCommit {
            intent.getStringExtra(USER_ID)?.let {
                userInfoFragment.submitUserId(it)
            }
        }.commit()
    }


}