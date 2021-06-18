package com.oblessing.mobliepay.core

import android.os.Handler
import android.os.Looper
import android.view.View
import com.oblessing.mobliepay.BuildConfig
import java.util.*
import kotlin.concurrent.timerTask

fun debug(action: () -> Unit) {
    if (BuildConfig.DEBUG) action()
}

fun View.setBlockingOnClickListener(debounce: Long = 400L, callback: () -> Unit) {
    val handler = Looper.myLooper()?.let { Handler(it) }

    var delayTimer = Timer()
    setOnClickListener {
        delayTimer.cancel()
        delayTimer = Timer().apply {
            schedule(timerTask {
                handler?.post(callback)
            }, debounce)
        }
    }
}

fun View.showIf(shouldShow: Boolean) {
    visibility = if (shouldShow) View.VISIBLE else View.GONE
}
