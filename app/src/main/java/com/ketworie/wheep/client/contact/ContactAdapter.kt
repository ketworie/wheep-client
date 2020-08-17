package com.ketworie.wheep.client.contact

import android.content.res.Resources
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ketworie.wheep.client.MainApplication
import com.ketworie.wheep.client.R
import com.ketworie.wheep.client.user.User
import kotlinx.android.synthetic.main.item_contact_list.view.*
import kotlinx.android.synthetic.main.item_incoming_message.view.avatar
import kotlinx.android.synthetic.main.item_incoming_message.view.name

class ContactAdapter(private val resources: Resources, private val onAdd: () -> Unit) :
    PagedListAdapter<User, ContactAdapter.UserViewHolder>(
        UserDefaultDiff
    ) {

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
            ?: throw RuntimeException("Error inflating contact item")
        if (viewType == R.layout.button_add_contact)
            view.setOnClickListener { onAdd.invoke() }
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        if (position == 0)
            return
        getItem(position - 1)?.let {
            val bitmap = BitmapFactory.decodeResource(
                resources,
                R.raw.icon
            )
            val roundedBitmap = RoundedBitmapDrawableFactory.create(resources, bitmap)
            roundedBitmap.isCircular = true
            holder.name.text = it.name
            holder.alias.text = it.alias
            Glide.with(holder.itemView)
                .asBitmap()
                .placeholder(roundedBitmap)
                .circleCrop()
                .load(MainApplication.RESOURCE_BASE + it.image)
                .into(holder.avatar)
        }
    }

    inner class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val avatar = view.avatar
        val name = view.name
        val alias = view.alias
    }

}