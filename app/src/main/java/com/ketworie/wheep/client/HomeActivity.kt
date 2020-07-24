package com.ketworie.wheep.client

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
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.ketworie.wheep.client.MainApplication.Companion.RESOURCE_BASE
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_home.*
import javax.inject.Inject

class HomeActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var viewModel: HomeActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(messageToolbar)
        viewModel = ViewModelProvider(this, viewModelFactory).get()
        createAvatarImage()
        homePager.adapter = HomeCollectionAdapter(this)
        TabLayoutMediator(tabs, homePager) { tab, position ->
            tab.text = when (position) {
                0 -> resources.getString(R.string.hubs_tab); else -> resources.getString(R.string.contacts_tab)
            }
        }.attach()
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
        avatar.setImageDrawable(roundedBitmap)
        viewModel.getMe().observe(this) {
            val resourceUrl = it.image

            if (resourceUrl.isEmpty())
                return@observe

            Glide.with(this@HomeActivity)
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
