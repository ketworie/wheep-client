package com.ketworie.wheep.client.chat

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.ketworie.wheep.client.MainApplication.Companion.HUB_ID
import com.ketworie.wheep.client.R
import com.ketworie.wheep.client.ViewModelFactory
import com.ketworie.wheep.client.loadAvatar
import com.ketworie.wheep.client.user.UserService
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChatActivity : AppCompatActivity() {

    @Inject
    lateinit var userService: UserService

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: ChatActivityViewModel
    lateinit var hubId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        supportPostponeEnterTransition()
        setContentView(R.layout.activity_chat)
        viewModel = ViewModelProvider(this, viewModelFactory).get()
        hubId = intent.extras?.getString(HUB_ID) ?: return
        avatar.transitionName = "avatar_$hubId"
        name.transitionName = "text_$hubId"
        viewModel.getHub(hubId).observe(this) {
            name.text = it.name
            loadAvatar(this, avatar, it.image) { supportStartPostponedEnterTransition() }
        }
        userService.getMe().observe(this) { user ->
            val messageAdapter = MessageAdapter(user.id)
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

    }

    private fun sendMessage() {
        val text = messageInput.text.toString().trim()
        if (text.isEmpty())
            return
        sendButton.isEnabled = false
        CoroutineScope(Dispatchers.IO).launch {
            try {
                viewModel.sendMessage(MessageSend(hubId, text))
                messageInput.text.clear()
                (messageList.adapter as MessageAdapter).isScrollNeeded = true
            } catch (e: Exception) {
                Log.e("CHAT", "Error during message send", e)
            }
            sendButton.isEnabled = true
        }
    }
}
