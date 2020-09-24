package com.ketworie.wheep.client

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

inline fun <T> LiveData<T>.observeOnce(
    owner: LifecycleOwner,
    crossinline onChanged: (T) -> Unit
): Observer<T> {
    val wrappedObserver = object : Observer<T> {
        override fun onChanged(t: T) {
            removeObserver(this)
            onChanged(t)
        }
    }
    observe(owner, wrappedObserver)
    return wrappedObserver
}