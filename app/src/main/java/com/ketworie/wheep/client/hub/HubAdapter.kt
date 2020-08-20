package com.ketworie.wheep.client.hub

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.ketworie.wheep.client.BaseViewHolder
import com.ketworie.wheep.client.MainApplication.Companion.RESOURCE_BASE
import com.ketworie.wheep.client.R
import kotlinx.android.synthetic.main.item_hub_list.view.*

class HubAdapter(private val onAdd: () -> Unit) :
    PagedListAdapter<HubMessage, BaseViewHolder>(
        HubDefaultDiff
    ) {

    var onItemClick: ((View, Hub) -> Unit) = { _, _ -> }

    companion object HubDefaultDiff : DiffUtil.ItemCallback<HubMessage>() {
        override fun areItemsTheSame(oldItem: HubMessage, newItem: HubMessage): Boolean {
            return newItem.hub.id == oldItem.hub.id
        }

        override fun areContentsTheSame(oldItem: HubMessage, newItem: HubMessage): Boolean {
            return oldItem.hub.lastModified == newItem.hub.lastModified && oldItem.message?.date == newItem.message?.date
        }

    }

    override fun getItemCount(): Int {
        return super.getItemCount() + 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> R.layout.button_add_hub
            else -> R.layout.item_hub_list
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(viewType, parent, false)
                ?: throw RuntimeException("Error inflating message item")
        if (viewType == R.layout.button_add_hub) {
            view.setOnClickListener { onAdd.invoke() }
            return BaseViewHolder(view)
        }
        return HubViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        if (holder !is HubViewHolder)
            return
        getItem(position.dec())?.apply {
            holder.header.text = hub.name
            // TODO: Show last message
            holder.lastMessage.text = message?.text ?: "No messages"
            Glide
                .with(holder.itemView)
                .asBitmap()
                .circleCrop()
                .load(RESOURCE_BASE + hub.image)
                .into(holder.avatar)
        }
    }

    inner class HubViewHolder(view: View) : BaseViewHolder(view) {
        val avatar: ImageView = view.avatar
        val header: TextView = view.header
        val lastMessage: TextView = view.lastMessage

        init {
            view.setOnClickListener {
                getItem(adapterPosition.dec())?.let { onItemClick.invoke(view, it.hub) }
            }
        }
    }
}