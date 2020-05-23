package com.ketworie.wheep.client.hub

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.ketworie.wheep.client.MainApplication.Companion.RESOURCE_BASE
import com.ketworie.wheep.client.R

class HubAdapter : PagedListAdapter<Hub, HubViewHolder>(UserDefaultDiff) {

    companion object UserDefaultDiff : DiffUtil.ItemCallback<Hub>() {
        override fun areItemsTheSame(oldItem: Hub, newItem: Hub): Boolean {
            return newItem.id == oldItem.id
        }

        override fun areContentsTheSame(oldItem: Hub, newItem: Hub): Boolean {
            return oldItem.name == newItem.name && oldItem.lastMessage?.date == newItem.lastMessage?.date
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HubViewHolder {
        val messageView =
            LayoutInflater.from(parent.context).inflate(R.layout.message_item, parent, false)
                ?: throw RuntimeException("Error inflating message item")
        return HubViewHolder(messageView)
    }

    override fun onBindViewHolder(holder: HubViewHolder, position: Int) {
        getItem(position)?.let {
            val (_, name, image, _, _, lastMessage) = it
            holder.header.text = name
            holder.lastMessage.text = lastMessage?.text ?: "No messages"
            Glide
                .with(holder.itemView)
                .asBitmap()
                .circleCrop()
                .load(RESOURCE_BASE + image)
                .into(holder.avatar)
        }
    }
}