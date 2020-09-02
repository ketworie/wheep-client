package com.ketworie.wheep.client.hub

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.ketworie.wheep.client.*
import com.ketworie.wheep.client.MainApplication.Companion.REQUEST_AVATAR
import com.ketworie.wheep.client.MainApplication.Companion.USER_IDS
import com.ketworie.wheep.client.image.ImageCropperActivity
import com.ketworie.wheep.client.image.loadAvatar
import com.ketworie.wheep.client.image.uploadImage
import com.ketworie.wheep.client.network.toastError
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_home.avatar
import kotlinx.android.synthetic.main.activity_hub_add.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HubAddActivity : AppCompatActivity() {

    var avatarAddress = ""

    @Inject
    lateinit var fileService: FileService

    @Inject
    lateinit var hubService: HubService

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hub_add)
        intent?.getStringArrayListExtra(USER_IDS)?.let {
            val fragment = UserSelectorFragment(it)
            supportFragmentManager.beginTransaction().add(R.id.fragment, fragment)
                .commit()
            create.setOnClickListener { createHub(fragment) }
        }
        loadAvatar(applicationContext, avatar, "")
        avatar.setOnClickListener { pickAvatar() }
    }

    private fun createHub(fragment: UserSelectorFragment) {
        if (fragment.tracker.selection.size() <= 1) {
            snack(R.string.select_more_users)
            return
        }
        if (!avatar.isEnabled) {
            snack(R.string.avatar_is_loading)
            return
        }
        if (!name.requireNonBlank(resources.getString(R.string.name_empty)))
            return

        create.isEnabled = false
        CoroutineScope(Dispatchers.IO).launch {
            val users = fragment.tracker.selection.toList()
            val hubAdd = HubAdd(name.text.toString(), avatarAddress, users)
            val isSuccessful = !hubService.addHub(hubAdd).toastError(this@HubAddActivity)
            withContext(Dispatchers.Main) {
                if (isSuccessful) finish()
                create.isEnabled = true
            }
        }
    }

    private fun snack(resId: Int) {
        Snackbar.make(create, resId, Snackbar.LENGTH_SHORT)
            .setBackgroundTint(resources.getColor(R.color.design_default_color_error, theme))
            .show()
    }

    private fun pickAvatar() {
        startActivityForResult(
            Intent(applicationContext, ImageCropperActivity::class.java),
            REQUEST_AVATAR
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK)
            return
        if (requestCode == REQUEST_AVATAR && data != null) {
            data.extras?.get(MainApplication.IMAGE_PATH)?.let { uploadAvatar(it as Uri) }
        }
    }

    private fun uploadAvatar(image: Uri) {
        CoroutineScope(Dispatchers.IO).launch {
            avatarAddress =
                uploadImage(this@HubAddActivity, avatar, image) { fileService.uploadImage(it) }
        }
    }

}