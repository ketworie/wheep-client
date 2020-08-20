package com.ketworie.wheep.client.contact

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.ketworie.wheep.client.BaseViewHolder
import com.ketworie.wheep.client.R
import com.ketworie.wheep.client.image.loadAvatar
import com.ketworie.wheep.client.user.User
import kotlinx.android.synthetic.main.item_contact_list.view.*
import kotlinx.android.synthetic.main.item_incoming_message.view.avatar
import kotlinx.android.synthetic.main.item_incoming_message.view.name

class ContactAdapter(private val resources: Resources, private val onAdd: () -> Unit) :
    PagedListAdapter<User, BaseViewHolder>(
        UserDefaultDiff
    ) {

    var onItemClick: ((View, User) -> Unit) = { _, _ -> }

    companion object UserDefaultDiff : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.name == newItem.name && oldItem.alias == newItem.alias
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> R.layout.button_add_contact
            else -> R.layout.item_contact_list
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
            ?: throw RuntimeException("Error inflating contact item")
        if (viewType == R.layout.button_add_contact) {
            view.setOnClickListener { onAdd.invoke() }
            return BaseViewHolder(view)
        }
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        if (holder !is UserViewHolder)
            return
        getItem(position - 1)?.let {
            holder.name.text = it.name
            holder.alias.text = it.alias
            loadAvatar(holder.itemView.context, holder.avatar, it.image)
        }
    }

    inner class UserViewHolder(view: View) : BaseViewHolder(view) {
        val avatar: ImageView = view.avatar
        val name: TextView = view.name
        val alias: TextView = view.alias

        init {
            view.setOnClickListener {
                getItem(adapterPosition - 1)?.let { onItemClick.invoke(view, it) }
            }
        }

    }

}