package com.ketworie.wheep.client

import android.widget.EditText

fun EditText.requireNonBlank(message: String): Boolean {
    if (text.isNullOrBlank()) {
        requestFocus()
        error = message
        return false
    }
    return true
}