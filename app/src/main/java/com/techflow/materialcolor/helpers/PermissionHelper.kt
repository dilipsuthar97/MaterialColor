package com.techflow.materialcolor.helpers

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

object PermissionHelper {
    const val REQUEST_CODE = 201

    /**
     * @func check if permission granted or not
     * @param context Context value
     * @param permissions list of permissions
     *
     * @return return boolean value
     */
    fun isPermissionsGranted(context: Context, permissions: Array<String>): Boolean {
        return permissions.all { permission ->  // Function extension method
            ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        }
    }

    /**
     * @func request for permissions
     * @param activity Activity
     * @param permissions list of permissions
     */
    fun requestPermissions(activity: Activity, permissions: Array<String>) {
        ActivityCompat.requestPermissions(activity, permissions, REQUEST_CODE)
    }
}