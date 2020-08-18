package com.ketworie.wheep.client.contact

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ketworie.wheep.client.MainApplication.Companion.USER_ID
import com.ketworie.wheep.client.R
import com.ketworie.wheep.client.ViewModelFactory
import com.ketworie.wheep.client.user.User
import com.ketworie.wheep.client.user.UserInfoActivity
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_contact_list.*
import javax.inject.Inject

class ContactListFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var viewModel: ContactListFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_contact_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get()
        val contactAdapter = ContactAdapter(resources) { startContactAdd() }
        contactList?.apply {
            val linearLayoutManager = LinearLayoutManager(this@ContactListFragment.activity)
            layoutManager = linearLayoutManager
            val dividerItemDecoration =
                DividerItemDecoration(context, linearLayoutManager.orientation)
            dividerItemDecoration.setDrawable(
                resources.getDrawable(
                    R.drawable.divider,
                    activity?.theme
                )
            )
            addItemDecoration(dividerItemDecoration)
            adapter = contactAdapter
        }
        viewModel.getContacts().observe(this.viewLifecycleOwner) {
            contactAdapter.submitList(it)
        }
        contactAdapter.onItemClick = this::startContactInfo
    }

    private fun startContactInfo(view: View, user: User) {
        startActivity(
            Intent(this.context, UserInfoActivity::class.java).putExtra(USER_ID, user.id)
            ,
            this.activity?.parent?.let {
                ActivityOptionsCompat.makeSceneTransitionAnimation(it)
                    .toBundle()
            }
        )
    }

    private fun startContactAdd() {
        startActivity(
            Intent(this.context, AddContactActivity::class.java)
            ,
            this.activity?.parent?.let {
                ActivityOptionsCompat.makeSceneTransitionAnimation(it)
                    .toBundle()
            }
        )
    }
}