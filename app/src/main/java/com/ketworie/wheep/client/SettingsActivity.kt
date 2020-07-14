package com.ketworie.wheep.client

import android.graphics.Bitmap
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.ketworie.wheep.client.MainApplication.Companion.IMAGE_URL_KEY
import com.ketworie.wheep.client.MainApplication.Companion.RESOURCE_BASE
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
        val avatarImageUrl = intent.extras?.getString(IMAGE_URL_KEY)
        avatarImageUrl?.also { loadAvatar(it) }
        userService.getMe().observe(this)
        { user ->
            userName.text = user.name
            userAlias.text = user.alias
        }
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
