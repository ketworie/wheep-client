package com.ketworie.wheep.client

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ketworie.wheep.client.MainApplication.Companion.IS_NEW_SESSION
import com.ketworie.wheep.client.hub.HubListFragment

class HomeCollectionAdapter(val fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        fragmentActivity.intent
        return when (position) {
            0 -> HubListFragment().apply {
                arguments = Bundle().apply {
                    fragmentActivity.intent.extras?.getBoolean(IS_NEW_SESSION)?.let {
                        putBoolean(
                            IS_NEW_SESSION,
                            it
                        )
                    }
                }
            }
            else -> HubListFragment()
        }

    }
}