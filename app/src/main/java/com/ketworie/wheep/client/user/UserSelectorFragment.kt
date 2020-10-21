package com.ketworie.wheep.client.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.toLiveData
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionManager
import com.ketworie.wheep.client.R
import com.ketworie.wheep.client.observeOnce
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_user_selector.*
import javax.inject.Inject

class UserSelectorFragment() : DaggerFragment() {

    @Inject
    lateinit var userService: UserService

    lateinit var tracker: SelectionTracker<String>
    private val adapter = SelectableUserAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_selector, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userList.adapter = adapter
        userList.layoutManager = LinearLayoutManager(this@UserSelectorFragment.requireActivity())
        tracker = SelectionTracker.Builder<String>(
            "userSelector",
            userList,
            StringKeyProvider(userList),
            SelectableUserAdapter.UserDetailsLookup(
                userList
            ),
            StorageStrategy.createStringStorage()
        )
            .withSelectionPredicate(SelectionPredicates.createSelectAnything())
            .build()
        adapter.tracker = tracker
        tracker.addObserver(object : SelectionTracker.SelectionObserver<String>() {
            override fun onSelectionChanged() {
                TransitionManager.beginDelayedTransition(root)
                if (tracker.hasSelection()) {
                    selectionHeader.visibility = View.VISIBLE
                    selectionHeader.text =
                        resources.getString(R.string.users_selected, tracker.selection.size())
                } else
                    selectionHeader.visibility = View.GONE
            }
        })
    }

    fun submitIds(ids: List<String>) {
        userService.getAllPaged(ids).toLiveData(10).observeOnce(this.viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }
}