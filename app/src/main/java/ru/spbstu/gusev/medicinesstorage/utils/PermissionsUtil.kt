package ru.spbstu.gusev.medicinesstorage.utils

import android.Manifest
import android.content.Context
import com.nabinbhandari.android.permissions.PermissionHandler
import com.nabinbhandari.android.permissions.Permissions

class PermissionsUtil {
    companion object {
        val cameraPermission = Manifest.permission.CAMERA

        fun requestPermission(
            context: Context,
            permission: String,
            rationaleString: String = "",
            onCompleteListener: () -> Unit
        ) {
            Permissions.check(context, permission, rationaleString, object : PermissionHandler() {
                override fun onGranted() {
                    onCompleteListener.invoke()
                }
            })
        }
    }
}