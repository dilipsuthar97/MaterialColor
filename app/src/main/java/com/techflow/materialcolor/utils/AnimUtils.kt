package com.techflow.materialcolor.utils

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.view.View
import android.view.animation.OvershootInterpolator

object AnimUtils {

    fun bounceAnim(view: View) {
        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, *floatArrayOf(0.5f, 1.0f))
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, *floatArrayOf(0.5f, 1.0f))
        val alpha = PropertyValuesHolder.ofFloat(View.ALPHA, *floatArrayOf(0.0f, 1.0f))

        val animator = ObjectAnimator.ofPropertyValuesHolder(view, scaleX, scaleY, alpha)
        animator.interpolator = OvershootInterpolator()
        animator.start()
    }

}