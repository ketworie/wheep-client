package com.ketworie.wheep.client

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.paging.toLiveData
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionManager
import com.ketworie.wheep.client.user.StringKeyProvider
import com.ketworie.wheep.client.user.UserService
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_user_selector.*
import javax.inject.Inject

class UserSelectorFragment(private val ids: List<String>) : Fragment() {

    @Inject
    lateinit var userService: UserService

    lateinit var tracker: SelectionTracker<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_selector, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onViewCreated(view, savedInstanceState)
        val adapter = SelectableUserAdapter()
        userList.adapter = adapter
        userList.layoutManager = LinearLayoutManager(this@UserSelectorFragment.requireActivity())
        tracker = SelectionTracker.Builder<String>(
            "userSelector",
            userList,
            StringKeyProvider(userList),
            SelectableUserAdapter.UserDetailsLookup(userList),
            StorageStrategy.createStringStorage()
        )
            .withSelectionPredicate(SelectionPredicates.createSelectAnything())
            .build()
        adapter.tracker = tracker
        tracker.addObserver(object : SelectionTracker.SelectionObserver<String>() {
            override fun onSelectionChanged() {
                TransitionManager.beginDelayedTransition(root)
                if (tracker.hasSelection()) {
                    selectionText.visibility = View.VISIBLE
                    selectionText.text =
                        resources.getString(R.string.users_selected, tracker.selection.size())
                } else
                    selectionText.visibility = View.GONE
            }
        })
        userService.getAllPaged(ids).toLiveData(10).observeOnce(this.viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }
}