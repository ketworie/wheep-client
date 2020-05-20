package com.ketworie.wheep.client.hub

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.ketworie.wheep.client.R

class HubAdapter : ListAdapter<Hub, HubViewHolder>(UserDefaultDiff) {

    companion object UserDefaultDiff : DiffUtil.ItemCallback<Hub>() {
        override fun areItemsTheSame(oldItem: Hub, newItem: Hub): Boolean {
            return newItem.id == oldItem.id
        }

        override fun areContentsTheSame(oldItem: Hub, newItem: Hub): Boolean {
            return oldItem.name == newItem.name && oldItem.lastMessage?.id == newItem.lastMessage?.id
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HubViewHolder {
        val messageView =
            LayoutInflater.from(parent.context).inflate(R.layout.message_item, parent, false)
                ?: throw RuntimeException("Error inflating message item")
        return HubViewHolder(messageView)
    }

    override fun onBindViewHolder(holder: HubViewHolder, position: Int) {
        val (id, name, lastMessage) = getItem(position)
        holder.header.text = name
        holder.lastMessage.text = lastMessage?.text ?: "No messages"
    }
}