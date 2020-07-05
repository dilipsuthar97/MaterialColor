package com.techflow.materialcolor.helpers

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment

object PermissionHelper {
    const val REQUEST_CODE = 201

    /**
     * @func check if permission granted or not
     * @param context Context value
     * @param permissions list of permissions
     *
     * @return return boolean value
     */
    fun isGranted(context: Context, permissions: Array<String>): Boolean {
        return permissions.all { permission ->  // Function extension method
            ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        }
    }

    /**
     * @func request for permissions
     * @param activity Activity
     * @param permissions list of permissions
     * @param request_code
     */
    fun request(activity: Activity, permissions: Array<String>, request_code: Int = REQUEST_CODE) {
        ActivityCompat.requestPermissions(activity, permissions, request_code)
    }

    /**
     * @func request for permissions
     * @param fragment Fragment
     * @param permissions list of permissions
     * @param request_code
     */
    fun requestInFragment(fragment: Fragment, permissions: Array<String>, request_code: Int = REQUEST_CODE) {
        fragment.requestPermissions(permissions, request_code)
    }

    /**
     * @fun return default request code
     */
    fun getRequestCode(): Int = REQUEST_CODE
}