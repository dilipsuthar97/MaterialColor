package com.techflow.materialcolor.helpers

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

object PermissionHelper {
    const val REQUEST_CODE = 201

    fun isPermissionsGranted(context: Context, permissions: Array<String>): Boolean {
        return permissions.all { permission ->  // Function extension method
            ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        }
    }

    fun requestPermissions(activity: Activity, permissions: Array<String>) {
        ActivityCompat.requestPermissions(activity, permissions, REQUEST_CODE)
    }
}