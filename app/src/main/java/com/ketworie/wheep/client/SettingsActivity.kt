package com.ketworie.wheep.client

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.observe
import com.ketworie.wheep.client.MainApplication.Companion.PREFERENCES
import com.ketworie.wheep.client.MainApplication.Companion.X_AUTH_TOKEN
import com.ketworie.wheep.client.hub.HubService
import com.ketworie.wheep.client.user.UserService
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsActivity : AppCompatActivity() {

    @Inject
    lateinit var userService: UserService

    @Inject
    lateinit var hubService: HubService

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        supportPostponeEnterTransition()
        setContentView(R.layout.activity_settings)
        userService.getMe().observe(this)
        { user ->
            name.text = user.name
            alias.text = user.alias
            loadAvatar(user.image)
        }
        logOut.setOnClickListener {
            logOut()
        }
    }

    private fun logOut() {
        getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE).edit().remove(X_AUTH_TOKEN).apply()
        userService.resetUserId()
        CoroutineScope(Dispatchers.IO).launch {
            hubService.deleteAll()
            runOnUiThread {
                startActivity(
                    Intent(this@SettingsActivity, SignInActivity::class.java)
                    ,
                    ActivityOptionsCompat.makeSceneTransitionAnimation(this@SettingsActivity)
                        .toBundle()
                )
            }
        }
    }

    private fun loadAvatar(it: String) {
        loadAvatar(
            applicationContext,
            avatar,
            it
        ) { supportStartPostponedEnterTransition() }
    }
}
