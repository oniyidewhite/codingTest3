package com.oblessing.mobliepay.core

import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.oblessing.mobliepay.R

object SnackbarUtil {
    fun showSnackBarMessage(view: View, callback: () -> Unit) {
        Snackbar.make(view, view.context.getString(R.string.error_message), Snackbar.LENGTH_INDEFINITE).apply {
            setAction(R.string.label_retry) {
               callback()
            }
        }.show()
    }
}