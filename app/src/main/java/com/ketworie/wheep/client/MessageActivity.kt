package com.ketworie.wheep.client

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ketworie.wheep.client.MainApplication.Companion.IMAGE_URL_KEY
import com.ketworie.wheep.client.MainApplication.Companion.RESOURCE_BASE
import com.ketworie.wheep.client.hub.HubAdapter
import com.ketworie.wheep.client.hub.HubDao
import com.ketworie.wheep.client.user.UserDao
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_message.*
import javax.inject.Inject

class MessageActivity : AppCompatActivity() {

    @Inject
    lateinit var hubDao: HubDao

    @Inject
    lateinit var userDao: UserDao
    private var avatarImageResource: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)
        setSupportActionBar(messageToolbar)
        createAvatarImage()
        val hubAdapter = HubAdapter()
        findViewById<RecyclerView>(R.id.hubList).apply {
            layoutManager = LinearLayoutManager(this@MessageActivity)
            adapter = hubAdapter
        }
        hubDao.myHubs.observe(this, onChanged = { list -> hubAdapter.submitList(list) })
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }

    private fun createAvatarImage() {
        val bitmap = BitmapFactory.decodeResource(resources, R.raw.icon)
        val roundedBitmap = RoundedBitmapDrawableFactory.create(resources, bitmap)
        roundedBitmap.isCircular = true
        userDao.me.observe(this@MessageActivity) {
            val resourceUrl = it.image
            avatarImageResource = resourceUrl
            Glide.with(this)
                .asBitmap()
                .placeholder(roundedBitmap)
                .load(RESOURCE_BASE + resourceUrl)
                .circleCrop()
                .into(avatarImageView)
        }
    }


    fun onSettings(view: View) {
        val intent = Intent(this, SettingsActivity::class.java)
        if (avatarImageResource != null)
            intent.putExtra(IMAGE_URL_KEY, avatarImageResource)
        startActivity(
            intent,
            ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                avatarImageView,
                resources.getString(R.string.user_avatar_transition_key)
            )
                .toBundle()
        )
    }

}
