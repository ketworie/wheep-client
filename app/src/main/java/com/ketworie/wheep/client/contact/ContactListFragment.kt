package com.ketworie.wheep.client.contact

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.ketworie.wheep.client.R
import com.ketworie.wheep.client.ViewModelFactory
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
        val contactAdapter = ContactAdapter { startContactAdd() }
        contactList?.apply {
            layoutManager = LinearLayoutManager(this@ContactListFragment.activity)
            adapter = contactAdapter
        }
        viewModel.getContacts().observe(this.viewLifecycleOwner) {
            contactAdapter.submitList(it)
        }
    }

    private fun startContactAdd() {
        Log.i("AAA", "AAAA")
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