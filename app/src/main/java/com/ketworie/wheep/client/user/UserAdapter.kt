package com.ketworie.wheep.client.user

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.paging.PagedListAdapter
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ketworie.wheep.client.HasStringKey
import com.ketworie.wheep.client.R
import com.ketworie.wheep.client.image.loadAvatar
import kotlinx.android.synthetic.main.item_incoming_message.view.avatar
import kotlinx.android.synthetic.main.item_list_entry.view.*

open class UserAdapter : PagedListAdapter<User, UserAdapter.UserViewHolder>(UserDefaultDiff),
    HasStringKey {

    var onItemClick: ((View, User) -> Unit) = { _, _ -> }
    var onItemRemove: ((View, User) -> Unit)? = null

    companion object UserDefaultDiff : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.name == newItem.name && oldItem.alias == newItem.alias
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_list_entry, parent, false)
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

    inner class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val avatar: ImageView = view.avatar
        val name: TextView = view.header
        val alias: TextView = view.body
        val root: LinearLayoutCompat = view.root
        val remove: AppCompatImageButton = view.remove

        init {
            view.setOnClickListener {
                getItem(adapterPosition)?.let { onItemClick.invoke(view, it) }
            }
            remove.setOnClickListener {
                getItem(adapterPosition)?.let { onItemRemove?.invoke(remove, it) }
            }
        }

        fun bind(user: User) {
            name.text = user.name
            alias.text = user.alias
            loadAvatar(itemView.context, avatar, user.image)
            if (onItemRemove != null)
                remove.visibility = View.VISIBLE
        }

        fun getItemDetails(): ItemDetailsLookup.ItemDetails<String?> =
            object : ItemDetailsLookup.ItemDetails<String?>() {
                override fun getSelectionKey(): String? = getItem(adapterPosition)?.id
                override fun getPosition(): Int = adapterPosition
                override fun inSelectionHotspot(e: MotionEvent) = true
            }

    }


}
