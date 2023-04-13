package com.example.mymediatube.helper

import android.content.Context
import android.content.Intent
import android.net.Uri

object IntentHelper {

    fun openAppForUri(context: Context, uri: Uri, onUnavailable: () -> Unit) {
        val intent = Intent(Intent.ACTION_VIEW, uri)
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        } else {
            onUnavailable()
        }
    }
}