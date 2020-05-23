package com.ketworie.wheep.client.hub.activity

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ketworie.wheep.client.MainApplication.Companion.IMAGE_URL_KEY
import com.ketworie.wheep.client.MainApplication.Companion.RESOURCE_BASE
import com.ketworie.wheep.client.R
import com.ketworie.wheep.client.SettingsActivity
import com.ketworie.wheep.client.ViewModelFactory
import com.ketworie.wheep.client.hub.HubAdapter
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_hub_list.*
import javax.inject.Inject

class HubListActivity : AppCompatActivity() {

    private var avatarImageResource: String? = null

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
        findViewById<RecyclerView>(R.id.hubList).apply {
            layoutManager = LinearLayoutManager(this@HubListActivity)
            adapter = hubAdapter
        }
        viewModel.hubs.observe(this) {
            hubAdapter.submitList(it)
        }
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }

    private fun createAvatarImage() {
        val bitmap = BitmapFactory.decodeResource(
            resources,
            R.raw.icon
        )
        val roundedBitmap = RoundedBitmapDrawableFactory.create(resources, bitmap)
        roundedBitmap.isCircular = true
        avatarImageView.setImageDrawable(roundedBitmap)
        viewModel.me.observe(this) {
            val resourceUrl = it.image
            avatarImageResource = resourceUrl
            Glide.with(this@HubListActivity)
                .asBitmap()
//                .placeholder(roundedBitmap)
                .circleCrop()
                .load(RESOURCE_BASE + resourceUrl)
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
