package com.example.maximumhackathon

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

@JvmName("inflateCasted")
inline fun <reified T : View> ViewGroup.inflate(
    @LayoutRes layoutRes: Int,
    attachToRoot: Boolean = true
): T = LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot) as T

inline fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = true) {
    LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}
