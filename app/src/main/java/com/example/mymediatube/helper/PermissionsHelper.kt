package com.example.mymediatube.helper

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object PermissionsHelper {

    fun hasPermissions(context: Context, permission: String): Boolean {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermissions(
        activity: Activity,
        message: String,
        requestCode: Int,
        permission: String
    ) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
            MaterialAlertDialogBuilder(activity)
                .setTitle("Permission Required")
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    ActivityCompat.requestPermissions(
                        activity, arrayOf(permission), requestCode)
                }
                .show()
        } else {
            ActivityCompat.requestPermissions(
                activity, arrayOf(permission), requestCode)
        }
    }

    fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray, callback: PermissionCallbacks
    ) {
        val denied = mutableListOf<String>()
        val granted = mutableListOf<String>()
        for (i in grantResults.indices)
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED)
                granted.add(permissions[i])
            else
                denied.add(permissions[i])
        if (granted.isNotEmpty()) callback.onPermissionsGranted(requestCode, granted)
        if (denied.isNotEmpty()) callback.onPermissionsDenied(requestCode, granted)
    }

    interface PermissionCallbacks {
        fun onPermissionsGranted(requestCode: Int, list: List<String>)
        fun onPermissionsDenied(requestCode: Int, list: List<String>)
    }
}