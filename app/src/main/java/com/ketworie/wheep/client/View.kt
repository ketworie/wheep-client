package com.ketworie.wheep.client

import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation

fun View.startBlinking() {
    val alphaAnimation = AlphaAnimation(1F, 0F).apply {
        repeatCount = Animation.INFINITE
        duration = 800
    }
    startAnimation(alphaAnimation)
}

fun View.stopBlinking(float: Float) {
    clearAnimation()
    alpha = float
}

fun View.stopBlinking() {
    stopBlinking(1.0F)
}