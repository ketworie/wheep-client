package com.ketworie.wheep.client.hub

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.observe
import androidx.transition.AutoTransition
import androidx.transition.Transition
import androidx.transition.TransitionListenerAdapter
import androidx.transition.TransitionManager
import com.ketworie.wheep.client.MainApplication
import com.ketworie.wheep.client.MainApplication.Companion.HUB_ID
import com.ketworie.wheep.client.R
import com.ketworie.wheep.client.contact.Contact
import com.ketworie.wheep.client.hideKeyboard
import com.ketworie.wheep.client.image.ImageCropperActivity
import com.ketworie.wheep.client.image.loadAvatar
import com.ketworie.wheep.client.image.uploadImage
import com.ketworie.wheep.client.network.toastError
import com.ketworie.wheep.client.observeOnce
import com.ketworie.wheep.client.user.User
import com.ketworie.wheep.client.user.UserListFragment
import com.ketworie.wheep.client.user.UserSelectorFragment
import com.ketworie.wheep.client.user.UserService
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_home.avatar
import kotlinx.android.synthetic.main.activity_hub_add.*
import kotlinx.android.synthetic.main.fragment_item_list.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HubInfoActivity : AppCompatActivity() {

    @Inject
    lateinit var hubService: HubService

    @Inject
    lateinit var userService: UserService
    private lateinit var hubId: String
    private val userListFragment = UserListFragment()
    private var userSelectorFragment = UserSelectorFragment()
    private var isSelectMode = false
    private var hubWithUsers: HubWithUsers? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        intent.getStringExtra(HUB_ID)?.let { hubId = it } ?: finish()
        setContentView(R.layout.activity_hub_add)
        userListFragment.userAdapter.onItemRemove = this::onUserRemove
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment, userListFragment)
            .runOnCommit {
                userListFragment.addItem.setOnClickListener { _ -> switchToSelection() }
                loadHub()
            }
            .commit()
        avatar.setOnClickListener { pickAvatar() }
        applyEdit.visibility = View.VISIBLE
        applyEdit.setOnClickListener { onApplyEdit() }
        name.addTextChangedListener { text ->
            circle.isActivated = this.hubWithUsers?.hub?.name != text.toString()
        }
        apply.visibility = View.GONE
        apply.text = resources.getString(R.string.add)
    }

    private fun onApplyEdit() {
        if (!circle.isActivated || name.text.isNullOrBlank())
            return
        hideKeyboard()
        root.requestFocus()
        applyEdit.isEnabled = false
        CoroutineScope(Dispatchers.IO).launch {
            hubService.rename(hubId, name.text.toString()).toastError(this@HubInfoActivity)
            applyEdit.isEnabled = true
        }
    }

    private fun switchToSelection() {
        isSelectMode = true
        val autoTransition = AutoTransition()
        autoTransition.duration = 200L
        autoTransition.addListener(object : TransitionListenerAdapter() {
            override fun onTransitionEnd(transition: Transition) {
                showUserSelector()
            }
        })
        TransitionManager.beginDelayedTransition(root, autoTransition)
        fragment.alpha = 0.5F
        header.visibility = View.GONE
        selectionHeader.visibility = View.VISIBLE
        apply.visibility = View.VISIBLE
    }

    private fun showUserSelector() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment, userSelectorFragment)
            .runOnCommit {
                userService.getContacts().observeOnce(this@HubInfoActivity) {
                    val hubUsers = hubWithUsers?.users?.map(User::id) ?: emptyList()
                    val ids =
                        it.map(Contact::userId).filter { id -> !hubUsers.contains(id) }
                    userSelectorFragment.submitIds(ids)
                    fragment.alpha = 1F
                    fragment.scheduleLayoutAnimation()
                }
            }
            .commit()
    }

    private fun switchToEdit() {
        isSelectMode = false
        val autoTransition = AutoTransition()
        autoTransition.duration = 200L
        autoTransition.addListener(object : TransitionListenerAdapter() {
            override fun onTransitionEnd(transition: Transition) {
                showUserList()
            }
        })
        TransitionManager.beginDelayedTransition(root, autoTransition)
        fragment.alpha = 0.5F
        selectionHeader.visibility = View.GONE
        header.visibility = View.VISIBLE
        apply.visibility = View.GONE
    }

    private fun showUserList() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment, userListFragment)
            .runOnCommit {
                userListFragment.addItem.setOnClickListener { _ -> switchToSelection() }
                fragment.alpha = 1F
                fragment.scheduleLayoutAnimation()
            }
            .commit()
    }

    private fun loadHub() {
        hubService.getWithUsers(hubId).observe(userListFragment.viewLifecycleOwner) {
            hubWithUsers = it
            name.setText(it.hub.name)
            loadAvatar(this, avatar, it.hub.image)
            userListFragment.submitUsers(it.users)
        }
    }

    private fun onUserRemove(view: View, user: User) {
        view.isEnabled = false
        AlertDialog.Builder(this)
            .setMessage(resources.getString(R.string.remove_user, user.name))
            .setPositiveButton(R.string.ok) { _, _ -> removeUser(view, user) }
            .setNegativeButton(R.string.cancel) { _, _ -> view.isEnabled = true }
            .show()
    }

    private fun removeUser(view: View, user: User) {
        CoroutineScope(Dispatchers.IO).launch {
            hubService.removeUser(hubId, user.id).toastError(this@HubInfoActivity)
            withContext(Dispatchers.Main) { view.isEnabled = true }
        }
    }

    private fun pickAvatar() {
        startActivityForResult(
            Intent(applicationContext, ImageCropperActivity::class.java),
            MainApplication.REQUEST_AVATAR
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK)
            return
        if (requestCode == MainApplication.REQUEST_AVATAR && data != null) {
            data.extras?.get(MainApplication.IMAGE_PATH)?.let { uri ->
                uploadAvatar(hubId, uri as Uri)
            }
        }
    }

    private fun uploadAvatar(id: String, image: Uri) {
        CoroutineScope(Dispatchers.IO).launch {
            uploadImage(this@HubInfoActivity, avatar, image) { hubService.updateAvatar(id, it) }
        }
    }

    override fun onBackPressed() {
        if (isSelectMode) {
            switchToEdit()
            return
        }
        super.onBackPressed()
    }


}