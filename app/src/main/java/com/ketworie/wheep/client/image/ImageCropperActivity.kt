package com.ketworie.wheep.client.image

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.listener.multi.BaseMultiplePermissionsListener
import com.ketworie.wheep.client.MainApplication.Companion.IMAGE_PATH
import com.yalantis.ucrop.UCrop
import java.io.File


class ImageCropperActivity : AppCompatActivity() {

    private val requestGalleryImage = 666
    private val requestCameraImage = 667

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pickFromGallery()
    }

    private fun pickFromGallery() {
        Dexter.withContext(applicationContext)
            .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object : BaseMultiplePermissionsListener() {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    report?.let {
                        if (report.areAllPermissionsGranted())
                            openGallery()
                        else
                            setResultCancelled()
                    }
                }
            }).check()
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, requestGalleryImage)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            setResultCancelled()
        }
        when (requestCode) {
            requestGalleryImage -> {
                val uri = data?.data
                if (uri == null) {
                    setResultCancelled()
                    return
                }
                crop(uri)
            }
            UCrop.REQUEST_CROP -> handleUCropResult(data)
            UCrop.RESULT_ERROR -> {
                val error = UCrop.getError(intent)
                Log.e("UCROP", "Error during image crop", error)
                setResultCancelled()
            }
        }
    }

    private fun crop(uri: Uri) {
        var uCrop = UCrop.of(uri, Uri.fromFile(File(cacheDir, "uCrop.jpg")))
        uCrop = getAvatarCrop(uCrop)
        uCrop.start(this)
    }

    private fun getAvatarCrop(uCrop: UCrop): UCrop {
        val options = UCrop.Options()
        options.withAspectRatio(1F, 1F)
        options.withMaxResultSize(2048, 2048)
        options.setCircleDimmedLayer(true)
        options.setShowCropGrid(false)
        return uCrop
            .withOptions(options)
    }

    private fun handleUCropResult(data: Intent?) {
        val resultUri = data?.let { UCrop.getOutput(it) }
        if (resultUri == null) {
            setResultCancelled()
            return
        }
        setResultOk(resultUri)
    }

    private fun setResultOk(imagePath: Uri) {
        val intent = Intent()
        intent.putExtra(IMAGE_PATH, imagePath)
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun setResultCancelled() {
        val intent = Intent()
        setResult(RESULT_CANCELED, intent)
        finish()
    }
}