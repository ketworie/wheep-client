package com.ketworie.wheep.client.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ketworie.wheep.client.ListDataSource
import com.ketworie.wheep.client.R
import kotlinx.android.synthetic.main.fragment_item_list.*

class UserListFragment : Fragment() {

    val userAdapter = UserAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_item_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        itemList.apply {
            val linearLayoutManager = LinearLayoutManager(this@UserListFragment.context)
            layoutManager = linearLayoutManager
            val dividerItemDecoration =
                DividerItemDecoration(context, linearLayoutManager.orientation)
            ResourcesCompat.getDrawable(resources, R.drawable.divider, this.context.theme)?.let {
                dividerItemDecoration.setDrawable(it)
            }
            addItemDecoration(dividerItemDecoration)
            adapter = userAdapter
        }
        addItem.text = resources.getString(R.string.add_users)
    }

    fun submitUsers(users: List<User>) {
        userAdapter.submitList(ListDataSource.toPagedList(users))
    }

}