package com.ketworie.wheep.client

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import com.ketworie.wheep.client.MainApplication.Companion.IMAGE_PATH
import com.ketworie.wheep.client.MainApplication.Companion.PREFERENCES
import com.ketworie.wheep.client.MainApplication.Companion.REQUEST_AVATAR
import com.ketworie.wheep.client.MainApplication.Companion.USER_ID
import com.ketworie.wheep.client.MainApplication.Companion.X_AUTH_TOKEN
import com.ketworie.wheep.client.hub.HubService
import com.ketworie.wheep.client.image.ImageCropperActivity
import com.ketworie.wheep.client.image.uploadImage
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
        userService.getMe().observeOnce(this)
        { user ->
            name.text = user.name
            alias.text = user.alias
            loadAvatar(user.image)
        }
        logOut.setOnClickListener {
            logOut()
        }
        avatar.setOnClickListener { pickAvatar() }
    }

    private fun logOut() {
        getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE).edit().remove(X_AUTH_TOKEN).apply()
        getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE).edit().remove(USER_ID).apply()
        Intent(this, MessageStreamService::class.java).apply {
            putExtra(X_AUTH_TOKEN, "")
            startService(this)
        }
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
        com.ketworie.wheep.client.image.loadAvatar(
            applicationContext,
            avatar,
            it
        ) { supportStartPostponedEnterTransition() }
    }

    private fun pickAvatar() {
        startActivityForResult(
            Intent(applicationContext, ImageCropperActivity::class.java),
            REQUEST_AVATAR
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK)
            return
        if (requestCode == REQUEST_AVATAR && data != null) {
            data.extras?.get(IMAGE_PATH)?.let { updateAvatar(it as Uri) }
        }
    }

    private fun updateAvatar(image: Uri) {
        CoroutineScope(Dispatchers.IO).launch {
            uploadImage(
                this@SettingsActivity,
                avatar,
                image
            ) { userService.updateAvatar(userService.userId, it) }
        }
    }

}
