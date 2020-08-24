package com.ketworie.wheep.client

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.RecyclerView
import com.ketworie.wheep.client.user.UserAdapter

class SelectableUserAdapter() : UserAdapter() {

    var tracker: SelectionTracker<String>? = null

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
            holder.root.isActivated = tracker?.isSelected(it.id) ?: false
        }
    }

    class UserDetailsLookup(private val recyclerView: RecyclerView) : ItemDetailsLookup<String>() {
        override fun getItemDetails(e: MotionEvent): ItemDetails<String?>? {
            val view = recyclerView.findChildViewUnder(e.x, e.y)
            if (view != null) {
                return (recyclerView.getChildViewHolder(view) as UserViewHolder)
                    .getItemDetails()
            }
            return null
        }
    }
}