package com.ketworie.wheep.client.hub

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.ketworie.wheep.client.MainApplication
import com.ketworie.wheep.client.MainApplication.Companion.IS_NEW_SESSION
import com.ketworie.wheep.client.R
import com.ketworie.wheep.client.ViewModelFactory
import com.ketworie.wheep.client.chat.ChatActivity
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_hub_list.*
import kotlinx.android.synthetic.main.hub_list_item.view.*
import javax.inject.Inject

class HubListFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var viewModel: HubListFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_hub_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onViewCreated(view, savedInstanceState)
        val hubAdapter = HubAdapter()
        hubAdapter.onItemClick = this::startChat
        hubList.apply {
            layoutManager = LinearLayoutManager(this@HubListFragment.activity)
            adapter = hubAdapter
        }
        viewModel = ViewModelProvider(this, viewModelFactory).get()
        viewModel.getHubs().observe(this.viewLifecycleOwner) {
            hubAdapter.submitList(it)
        }
        if (arguments?.getBoolean(IS_NEW_SESSION) == true)
            viewModel.refreshHubs()
    }

    private fun startChat(view: View, hub: Hub) {
        val avatarElement: Pair<View, String> = Pair(view.avatar, "avatar_${hub.id}")
        val headerElement: Pair<View, String> = Pair(view.header, "text_${hub.id}")
        startActivity(
            Intent(this.activity, ChatActivity::class.java).putExtra(
                MainApplication.HUB_ID,
                hub.id
            ),
            this.activity?.let {
                ActivityOptionsCompat.makeSceneTransitionAnimation(it, avatarElement, headerElement)
                    .toBundle()
            }
        )
    }

}