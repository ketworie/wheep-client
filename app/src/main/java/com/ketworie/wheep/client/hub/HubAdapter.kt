package com.ketworie.wheep.client.hub

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ketworie.wheep.client.MainApplication.Companion.RESOURCE_BASE
import com.ketworie.wheep.client.R
import kotlinx.android.synthetic.main.hub_list_item.view.*

class HubAdapter() :
    PagedListAdapter<HubMessage, HubAdapter.HubViewHolder>(
        HubDefaultDiff
    ) {

    var onItemClick: ((View, Hub) -> Unit)? = null

    companion object HubDefaultDiff : DiffUtil.ItemCallback<HubMessage>() {
        override fun areItemsTheSame(oldItem: HubMessage, newItem: HubMessage): Boolean {
            return newItem.hub.id == oldItem.hub.id
        }

        override fun areContentsTheSame(oldItem: HubMessage, newItem: HubMessage): Boolean {
            return oldItem.hub.lastModified == newItem.hub.lastModified && oldItem.message?.date == newItem.message?.date
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HubViewHolder {
        val messageView =
            LayoutInflater.from(parent.context).inflate(R.layout.hub_list_item, parent, false)
                ?: throw RuntimeException("Error inflating message item")
        return HubViewHolder(messageView)
    }

    override fun onBindViewHolder(holder: HubViewHolder, position: Int) {
        getItem(position)?.apply {
            holder.header.text = hub.name
            // TODO: Show last message
            holder.lastMessage.text = message?.text ?: "No messages"
            Glide
                .with(holder.itemView)
                .asBitmap()
                .circleCrop()
                .load(RESOURCE_BASE + hub.image)
                .into(holder.avatar)
            holder.avatar.transitionName = "avatar_${hub.id}"
            holder.header.transitionName = "text_${hub.id}"
        }
    }

    inner class HubViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val avatar: ImageView = view.avatar
        val header: TextView = view.header
        val lastMessage: TextView = view.lastMessage

        init {
            view.setOnClickListener {
                getItem(adapterPosition)?.let { onItemClick?.invoke(view, it.hub) }
            }
        }
    }
}