package com.ketworie.wheep.client.hub

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.google.android.material.snackbar.Snackbar
import com.ketworie.wheep.client.FileService
import com.ketworie.wheep.client.MainApplication
import com.ketworie.wheep.client.MainApplication.Companion.REQUEST_AVATAR
import com.ketworie.wheep.client.MainApplication.Companion.USER_IDS
import com.ketworie.wheep.client.R
import com.ketworie.wheep.client.UserSelectorFragment
import com.ketworie.wheep.client.image.ImageCropperActivity
import com.ketworie.wheep.client.image.loadAvatar
import com.ketworie.wheep.client.network.NetworkResponse
import com.ketworie.wheep.client.network.toastError
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_add_hub.*
import kotlinx.android.synthetic.main.activity_home.avatar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddHubActivity : AppCompatActivity() {

    var avatarAddress = ""

    @Inject
    lateinit var fileService: FileService

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_hub)
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
            Snackbar.make(create, R.string.select_more_users, Snackbar.LENGTH_SHORT)
                .setBackgroundTint(resources.getColor(R.color.design_default_color_error, theme))
                .show()
            return
        }
        TODO("Not yet implemented")
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
        avatar.isEnabled = false
        val drawable = image.path?.let { RoundedBitmapDrawableFactory.create(resources, it) }
        drawable?.isCircular = true
        avatar.setImageDrawable(drawable)
        CoroutineScope(Dispatchers.IO).launch {
            val response = fileService.uploadImage(image)
            withContext(Dispatchers.Main) { avatar.isEnabled = true }
            if (response.toastError(this@AddHubActivity))
                return@launch
            avatarAddress = (response as NetworkResponse.Success<String>).body
        }
    }

}