package com.ketworie.wheep.client.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ketworie.wheep.client.R
import com.ketworie.wheep.client.image.loadAvatar
import com.ketworie.wheep.client.user.User
import kotlinx.android.synthetic.main.item_incoming_message.view.*

class MessageAdapter(private val userId: String, var usersById: Map<String, User>) :
    PagedListAdapter<Message, MessageAdapter.MessageViewHolder>(MessageDefaultDiff) {

    var isScrollNeeded = false

    companion object MessageDefaultDiff : DiffUtil.ItemCallback<Message>() {
        override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem == newItem
        }
    }

    fun setScroller(scroller: () -> Unit) {
        registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                if (isScrollNeeded) {
                    isScrollNeeded = false
                    scroller()
                }
            }
        })
    }

    override fun getItemViewType(position: Int): Int {
        return (if (getItem(position)?.userId == userId) R.layout.item_outgoing_message else R.layout.item_incoming_message)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(viewType, parent, false)
                ?: throw RuntimeException("Error inflating message item")
        if (viewType == R.layout.item_outgoing_message)
            return MessageViewHolder(view)

        return IncomingMessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        getItem(position)?.apply {
            holder.text.text = text
            if (holder !is IncomingMessageViewHolder)
                return
            val user = usersById[userId]
            holder.name.text = user?.name ?: "Undefined"
            loadAvatar(holder.itemView.context, holder.avatar, user?.image ?: "")
        }
    }

    open inner class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val text: AppCompatTextView = view.text
    }

    inner class IncomingMessageViewHolder(view: View) : MessageViewHolder(view) {
        val avatar: AppCompatImageView = view.avatar
        val name: AppCompatTextView = view.name
    }
}