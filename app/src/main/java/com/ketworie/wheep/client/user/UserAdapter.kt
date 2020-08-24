package com.ketworie.wheep.client.user

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.DiffUtil
import com.ketworie.wheep.client.BaseViewHolder
import com.ketworie.wheep.client.HasStringKey
import com.ketworie.wheep.client.R
import com.ketworie.wheep.client.image.loadAvatar
import kotlinx.android.synthetic.main.item_incoming_message.view.avatar
import kotlinx.android.synthetic.main.item_incoming_message.view.name
import kotlinx.android.synthetic.main.item_user.view.*

open class UserAdapter : PagedListAdapter<User, UserAdapter.UserViewHolder>(UserDefaultDiff),
    HasStringKey {

    var onItemClick: ((View, User) -> Unit) = { _, _ -> }

    companion object UserDefaultDiff : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.name == newItem.name && oldItem.alias == newItem.alias
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
            ?: throw RuntimeException("Error inflating user item")
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun getPosition(key: String): Int? {
        return currentList?.indexOfFirst { it.id == key }
    }

    override fun getKey(position: Int): String? {
        return getItem(position)?.id
    }

    inner class UserViewHolder(view: View) : BaseViewHolder(view) {

        val avatar: ImageView = view.avatar
        val name: TextView = view.name
        val alias: TextView = view.alias
        val root: LinearLayout = view.root

        init {
            view.setOnClickListener {
                getItem(adapterPosition)?.let { onItemClick.invoke(view, it) }
            }
        }

        fun bind(user: User) {
            name.text = user.name
            alias.text = user.alias
            loadAvatar(itemView.context, avatar, user.image)
        }

        fun getItemDetails(): ItemDetailsLookup.ItemDetails<String?> =
            object : ItemDetailsLookup.ItemDetails<String?>() {
                override fun getSelectionKey(): String? = getItem(adapterPosition)?.id
                override fun getPosition(): Int = adapterPosition
                override fun inSelectionHotspot(e: MotionEvent) = true
            }

    }


}
