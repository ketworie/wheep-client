package com.ketworie.wheep.client

import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.RoundedBitmapDrawable
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.ketworie.wheep.client.MainApplication.Companion.RESOURCE_BASE
import com.ketworie.wheep.client.hub.HubAdapter
import com.ketworie.wheep.client.hub.HubDao
import com.ketworie.wheep.client.user.UserDao
import dagger.android.AndroidInjection
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.add("Settings")?.apply {
            val bitmap = BitmapFactory.decodeResource(resources, R.raw.icon)
            val roundedBitmap = RoundedBitmapDrawableFactory.create(resources, bitmap)
            roundedBitmap.isCircular = true
            userDao.getMe().observe(this@MessageActivity) {
                val resourceUrl = it.image
                loadDrawable(resourceUrl, roundedBitmap)
            }
            icon = roundedBitmap
            setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
            setOnMenuItemClickListener {
                onSettings()
                return@setOnMenuItemClickListener true
            }
        }
        return super.onCreateOptionsMenu(menu)
    }

    private fun MenuItem.loadDrawable(
        resourceUrl: String,
        placeholder: RoundedBitmapDrawable
    ) {
        Glide.with(this@MessageActivity)
            .load(RESOURCE_BASE + resourceUrl)
            .circleCrop()
            .placeholder(placeholder)
            .into(object : CustomTarget<Drawable>() {
                override fun onLoadCleared(placeholder: Drawable?) {
                    icon = placeholder
                }

                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    icon = resource
                }

            })
    }

    private fun onSettings() {
        TODO("Not yet implemented")
    }

}
