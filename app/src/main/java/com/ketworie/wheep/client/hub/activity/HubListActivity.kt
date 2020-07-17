package com.ketworie.wheep.client.hub.activity

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.core.util.Pair
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.ketworie.wheep.client.MainApplication.Companion.HUB_ID
import com.ketworie.wheep.client.MainApplication.Companion.IS_NEW_SESSION
import com.ketworie.wheep.client.MainApplication.Companion.RESOURCE_BASE
import com.ketworie.wheep.client.R
import com.ketworie.wheep.client.SettingsActivity
import com.ketworie.wheep.client.ViewModelFactory
import com.ketworie.wheep.client.chat.ChatActivity
import com.ketworie.wheep.client.hub.Hub
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_hub_list.*
import kotlinx.android.synthetic.main.hub_list_item.view.*
import javax.inject.Inject

class HubListActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: HubListActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hub_list)
        setSupportActionBar(messageToolbar)
        viewModel = ViewModelProvider(this, viewModelFactory).get()
        createAvatarImage()
        val hubAdapter = HubAdapter()
        hubAdapter.onItemClick = this::startChat
        hubList.apply {
            layoutManager = LinearLayoutManager(this@HubListActivity)
            adapter = hubAdapter
        }
        viewModel.hubs.observe(this) {
            hubAdapter.submitList(it)
        }
        if (intent.extras?.getBoolean(IS_NEW_SESSION) == true)
            viewModel.refreshHubs()
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }

    private fun startChat(view: View, hub: Hub) {
        val avatarElement: Pair<View, String> = Pair(view.avatar, "avatar_${hub.id}")
        val headerElement: Pair<View, String> = Pair(view.header, "text_${hub.id}")
        startActivity(
            Intent(this, ChatActivity::class.java).putExtra(HUB_ID, hub.id),
            ActivityOptionsCompat.makeSceneTransitionAnimation(this, avatarElement, headerElement)
                .toBundle()
        )
    }

    private fun createAvatarImage() {
        val bitmap = BitmapFactory.decodeResource(
            resources,
            R.raw.icon
        )
        val roundedBitmap = RoundedBitmapDrawableFactory.create(resources, bitmap)
        roundedBitmap.isCircular = true
        avatar.setImageDrawable(roundedBitmap)
        viewModel.me.observe(this) {
            val resourceUrl = it.image

            if (resourceUrl.isEmpty())
                return@observe

            Glide.with(this@HubListActivity)
                .asBitmap()
                .circleCrop()
                .load(RESOURCE_BASE + resourceUrl)
                .into(avatar)
        }
    }


    fun onSettings(view: View) {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(
            intent,
            ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                avatar,
                resources.getString(R.string.user_avatar_transition_key)
            )
                .toBundle()
        )
    }

}
