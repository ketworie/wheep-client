package com.ketworie.wheep.client

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ketworie.wheep.client.hub.HubListFragment
import com.ketworie.wheep.client.notebook.ContactListFragment

class HomeCollectionAdapter(val fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        fragmentActivity.intent
        return when (position) {
            0 -> HubListFragment()
            else -> ContactListFragment()
        }

    }
}