package com.ketworie.wheep.client

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
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

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)
        setSupportActionBar(toolbar)
        createAvatarImage()
        val hubAdapter = HubAdapter()
        findViewById<RecyclerView>(R.id.messageList).apply {
            layoutManager = LinearLayoutManager(this@MessageActivity)
            adapter = hubAdapter
        }
        hubDao.getMyHubs().observe(this, onChanged = { list -> hubAdapter.submitList(list) })
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }

    fun createAvatarImage() {
        val bitmap = BitmapFactory.decodeResource(resources, R.raw.icon)
        val roundedBitmap = RoundedBitmapDrawableFactory.create(resources, bitmap)
        roundedBitmap.isCircular = true
        userDao.getMe().observe(this@MessageActivity) {
            val resourceUrl = it.image
            Glide.with(this)
                .asBitmap()
                .placeholder(roundedBitmap)
                .load(RESOURCE_BASE + resourceUrl)
                .circleCrop()
                .into(avatarImageView)
        }
    }

    private fun onSettings(view: View) {
        TODO("Not yet implemented")
    }

}
