package com.ketworie.wheep.client.hub

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ketworie.wheep.client.MainApplication
import com.ketworie.wheep.client.MainApplication.Companion.USER_IDS
import com.ketworie.wheep.client.R
import com.ketworie.wheep.client.ViewModelFactory
import com.ketworie.wheep.client.chat.ChatActivity
import com.ketworie.wheep.client.observeOnce
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_item_list.*
import java.util.*
import javax.inject.Inject

class HubListFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var viewModel: HubListFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_item_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onViewCreated(view, savedInstanceState)
        addItem.text = resources.getString(R.string.add_hub)
        val hubAdapter = HubAdapter()
        hubAdapter.onItemClick = this::startChat
        itemList.apply {
            val linearLayoutManager = LinearLayoutManager(this@HubListFragment.activity)
            layoutManager = linearLayoutManager
            val dividerItemDecoration =
                DividerItemDecoration(context, linearLayoutManager.orientation)
            ResourcesCompat.getDrawable(resources, R.drawable.divider, activity?.theme)?.let {
                dividerItemDecoration.setDrawable(it)
            }
            addItemDecoration(dividerItemDecoration)
            adapter = hubAdapter
        }
        viewModel = ViewModelProvider(this, viewModelFactory).get()
        viewModel.getHubs().observe(viewLifecycleOwner) {
            hubAdapter.submitList(it)
        }
        addItem.setOnClickListener { startAddHub() }
    }

    private fun startAddHub() {
        viewModel.getContacts().observeOnce(viewLifecycleOwner) { list ->
            val userIds = list.map { it.userId }
            val intent = Intent(this.context, HubAddActivity::class.java)
            intent.putStringArrayListExtra(USER_IDS, ArrayList(userIds))
            startActivity(intent)
        }
    }

    private fun startChat(view: View, hub: Hub) {
        startActivity(
            Intent(this.activity, ChatActivity::class.java).putExtra(
                MainApplication.HUB_ID,
                hub.id
            )
        )
    }

}