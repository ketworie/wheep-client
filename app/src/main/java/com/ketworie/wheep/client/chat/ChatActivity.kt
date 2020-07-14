package com.ketworie.wheep.client.chat

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.observe
import com.bumptech.glide.Glide
import com.ketworie.wheep.client.MainApplication.Companion.HUB_ID
import com.ketworie.wheep.client.MainApplication.Companion.RESOURCE_BASE
import com.ketworie.wheep.client.R
import com.ketworie.wheep.client.ViewModelFactory
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_chat.*
import javax.inject.Inject

class ChatActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: ChatActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        viewModel = ViewModelProvider(this, viewModelFactory).get()
        intent.extras?.getString(HUB_ID)?.let { hubId ->
            avatar.transitionName = "avatar_$hubId"
            name.transitionName = "text_$hubId"
            viewModel.getHub(hubId).observe(this) {
                name.text = it.name
                Glide.with(this)
                    .asBitmap()
                    .circleCrop()
                    .load(RESOURCE_BASE + it.image)
                    .into(avatar)
            }
        }
    }
}
