package com.ketworie.wheep.client

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.observe
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.ketworie.wheep.client.MainApplication.Companion.PREFERENCES
import com.ketworie.wheep.client.MainApplication.Companion.RESOURCE_BASE
import com.ketworie.wheep.client.MainApplication.Companion.X_AUTH_TOKEN
import com.ketworie.wheep.client.user.UserService
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_settings.*
import javax.inject.Inject

class SettingsActivity : AppCompatActivity() {

    @Inject
    lateinit var userService: UserService

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        userService.getMe().observe(this)
        { user ->
            userName.text = user.name
            userAlias.text = user.alias
            loadAvatar(user.image)
        }
        logOut.setOnClickListener {
            logOut()
        }
    }

    private fun logOut() {
        getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE).edit().remove(X_AUTH_TOKEN).apply()
        startActivity(
            Intent(this, SignInActivity::class.java)
            , ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle()
        )
    }

    private fun loadAvatar(it: String) {
        supportPostponeEnterTransition()
        Glide.with(this)
            .asBitmap()
            .circleCrop()
            .listener(object : RequestListener<Bitmap> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    isFirstResource: Boolean
                ): Boolean {
                    supportStartPostponedEnterTransition()
                    return false
                }

                override fun onResourceReady(
                    resource: Bitmap?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    supportStartPostponedEnterTransition()
                    return false
                }
            })
            .load(RESOURCE_BASE + it)
            .into(avatar)
    }
}
