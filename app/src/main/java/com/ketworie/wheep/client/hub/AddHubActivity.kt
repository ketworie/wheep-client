package com.ketworie.wheep.client.hub

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ketworie.wheep.client.MainApplication.Companion.USER_IDS
import com.ketworie.wheep.client.R
import com.ketworie.wheep.client.UserSelectorFragment
import com.ketworie.wheep.client.image.loadAvatar
import kotlinx.android.synthetic.main.activity_home.*

class AddHubActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_hub)
        intent?.getStringArrayListExtra(USER_IDS)?.let {
            supportFragmentManager.beginTransaction().add(R.id.fragment, UserSelectorFragment(it))
                .commit()
        }
        loadAvatar(applicationContext, avatar, "")
    }
}