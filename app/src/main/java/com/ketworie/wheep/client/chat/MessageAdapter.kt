package com.ketworie.wheep.client.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ketworie.wheep.client.R
import kotlinx.android.synthetic.main.incoming_message.view.*

class MessageAdapter(private val userId: String) :
    PagedListAdapter<Message, MessageAdapter.MessageViewHolder>(MessageDefaultDiff) {

    var isScrollNeeded = false

    companion object MessageDefaultDiff : DiffUtil.ItemCallback<Message>() {
        override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem.text == newItem.text
        }
    }

    fun setScroller(scroller: () -> Unit) {
        registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                if (isScrollNeeded) {
                    isScrollNeeded = false
                    scroller.invoke()
                }
            }
        })
    }

    override fun getItemViewType(position: Int): Int {
        return (if (getItem(position)?.userId == userId) R.layout.outgoing_message else R.layout.incoming_message)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(viewType, parent, false)
                ?: throw RuntimeException("Error inflating message item")
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        getItem(position)?.apply {
            holder.name?.text = userId
            holder.text.text = text
        }
    }

    inner class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val avatar = view.avatar
        val name = view.name
        val text = view.text
    }
}