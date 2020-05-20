package com.ketworie.wheep.client.hub

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ketworie.wheep.client.R

class HubViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val avatar: ImageView = view.findViewById(R.id.hubAvatarView)
    val header: TextView = view.findViewById(R.id.hubHeaderText)
    val lastMessage: TextView = view.findViewById(R.id.hubLastMessageText)

}