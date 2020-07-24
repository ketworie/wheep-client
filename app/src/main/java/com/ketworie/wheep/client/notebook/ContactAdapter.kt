package com.ketworie.wheep.client.notebook

import android.view.View
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ketworie.wheep.client.user.User
import kotlinx.android.synthetic.main.incoming_message.view.*

class ContactAdapter : PagedListAdapter<User, ContactAdapter.UserViewHolder>(UserDefaultDiff) {

    companion object UserDefaultDiff : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.name == newItem.name && oldItem.alias == newItem.alias
        }
    }


    inner class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val avatar = view.avatar
        val name = view.name
        val text = view.text
    }

}