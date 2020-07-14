package com.ketworie.wheep.client.hub.activity

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
import com.ketworie.wheep.client.hub.Hub
import kotlinx.android.synthetic.main.hub_list_item.view.*

class HubAdapter : PagedListAdapter<Hub, HubAdapter.HubViewHolder>(
    UserDefaultDiff
) {

    var onItemClick: ((View, Hub) -> Unit)? = null

    companion object UserDefaultDiff : DiffUtil.ItemCallback<Hub>() {
        override fun areItemsTheSame(oldItem: Hub, newItem: Hub): Boolean {
            return newItem.id == oldItem.id
        }

        override fun areContentsTheSame(oldItem: Hub, newItem: Hub): Boolean {
            return oldItem.name == newItem.name && oldItem.lastUpdate == newItem.lastUpdate
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HubViewHolder {
        val messageView =
            LayoutInflater.from(parent.context).inflate(R.layout.hub_list_item, parent, false)
                ?: throw RuntimeException("Error inflating message item")
        return HubViewHolder(messageView)
    }

    override fun onBindViewHolder(holder: HubViewHolder, position: Int) {
        getItem(position)?.let {
            val (id, name, image, _, _, _) = it
            holder.header.text = name
            // TODO: Show last message
            holder.lastMessage.text = "No messages"
            Glide
                .with(holder.itemView)
                .asBitmap()
                .circleCrop()
                .load(RESOURCE_BASE + image)
                .into(holder.avatar)
            holder.avatar.transitionName = "avatar_$id"
            holder.header.transitionName = "text_$id"
        }

    }

    inner class HubViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val avatar: ImageView = view.avatar
        val header: TextView = view.header
        val lastMessage: TextView = view.lastMessage

        init {
            view.setOnClickListener {
                getItem(adapterPosition)?.let { onItemClick?.invoke(view, it) }
            }
        }
    }
}