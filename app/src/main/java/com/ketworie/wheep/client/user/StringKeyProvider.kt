package com.ketworie.wheep.client.user

import androidx.recyclerview.selection.ItemKeyProvider
import androidx.recyclerview.widget.RecyclerView
import com.ketworie.wheep.client.HasStringKey

class StringKeyProvider(private val recyclerView: RecyclerView) :
    ItemKeyProvider<String>(SCOPE_CACHED) {

    override fun getKey(position: Int): String? {
        return (recyclerView.adapter as HasStringKey?)?.getKey(position)
    }

    override fun getPosition(key: String): Int {
        return (recyclerView.adapter as HasStringKey?)?.getPosition(key) ?: RecyclerView.NO_POSITION
    }
}