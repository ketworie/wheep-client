package com.ketworie.wheep.client

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory

class MessageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.add("Settings")?.apply {
            val bitmap = BitmapFactory.decodeResource(resources, R.raw.icon)
            val roundedBitmap = RoundedBitmapDrawableFactory.create(resources, bitmap)
            roundedBitmap.isCircular = true
            icon = roundedBitmap
            setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
            setOnMenuItemClickListener {
                onSettings()
                return@setOnMenuItemClickListener true
            }
        }
        return super.onCreateOptionsMenu(menu)
    }

    private fun onSettings() {
        TODO("Not yet implemented")
    }

}
