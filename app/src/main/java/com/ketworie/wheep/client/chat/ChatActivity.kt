package com.ketworie.wheep.client.chat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.ketworie.wheep.client.MainApplication.Companion.HUB_ID
import com.ketworie.wheep.client.R
import com.ketworie.wheep.client.ViewModelFactory
import com.ketworie.wheep.client.hub.HubInfoActivity
import com.ketworie.wheep.client.image.loadAvatar
import com.ketworie.wheep.client.user.UserService
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChatActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var userService: UserService

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: ChatActivityViewModel
    lateinit var hubId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        viewModel = ViewModelProvider(this, viewModelFactory).get()
        hubId = intent.extras?.getString(HUB_ID) ?: return
        viewModel.getHub(hubId).observe(this) {
            name.text = it.hub.name
            userCount.text = resources.getString(R.string.user_count, it.users.size)
            loadAvatar(
                this,
                avatar,
                it.hub.image
            )
            val usersById = it.users.map { user -> Pair(user.id, user) }.toMap()
            val messageAdapter = MessageAdapter(userService.userId, usersById)
            messageAdapter.setScroller { messageList.smoothScrollToPosition(0) }
            messageList.apply {
                adapter = messageAdapter
                layoutManager =
                    LinearLayoutManager(this@ChatActivity).apply { reverseLayout = true }
            }
            viewModel.getMessages(hubId).observe(this) {
                messageAdapter.submitList(it)
            }
        }
        sendButton.setOnClickListener {
            sendMessage()
        }
        hubInfo.setOnClickListener(this::onHubInfo)
    }

    private fun onHubInfo(v: View?) {
        val intent = Intent(this, HubInfoActivity::class.java).putExtra(HUB_ID, hubId)
        startActivity(intent)
    }

    private fun sendMessage() {
        val text = messageInput.text.toString().trim()
        if (text.isEmpty())
            return
        sendButton.isEnabled = false
        CoroutineScope(Dispatchers.IO).launch {
            try {
                viewModel.sendMessage(MessageSend(hubId, text))
                messageInput.text?.clear()
                (messageList.adapter as MessageAdapter).isScrollNeeded = true
            } catch (e: Exception) {
                Log.e("CHAT", "Error during message send", e)
            }
            sendButton.isEnabled = true
        }
    }
}
