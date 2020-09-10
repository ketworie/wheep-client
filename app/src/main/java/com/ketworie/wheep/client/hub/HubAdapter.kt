package com.ketworie.wheep.client.hub

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ketworie.wheep.client.R
import com.ketworie.wheep.client.image.loadAvatar
import kotlinx.android.synthetic.main.item_list_entry.view.*

class HubAdapter : PagedListAdapter<HubMessage, HubAdapter.HubViewHolder>(HubDefaultDiff) {

    var onItemClick: ((View, Hub) -> Unit) = { _, _ -> }

    companion object HubDefaultDiff : DiffUtil.ItemCallback<HubMessage>() {
        override fun areItemsTheSame(oldItem: HubMessage, newItem: HubMessage): Boolean {
            return newItem.hub.id == oldItem.hub.id
        }

        override fun areContentsTheSame(oldItem: HubMessage, newItem: HubMessage): Boolean {
            return oldItem.hub == newItem.hub && oldItem.message?.date == newItem.message?.date
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HubViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_list_entry, parent, false)
                ?: throw RuntimeException("Error inflating message item")
        return HubViewHolder(view)
    }

    override fun onBindViewHolder(holder: HubViewHolder, position: Int) {
        getItem(position)?.apply {
            holder.header.text = hub.name
            // TODO: Show last message
            holder.lastMessage.text = message?.text ?: "No messages"
            loadAvatar(holder.avatar.context, holder.avatar, hub.image)
        }
    }

    inner class HubViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val avatar: ImageView = view.avatar
        val header: TextView = view.header
        val lastMessage: TextView = view.body

        init {
            view.setOnClickListener {
                getItem(adapterPosition)?.let { onItemClick.invoke(view, it.hub) }
            }
        }
    }
}