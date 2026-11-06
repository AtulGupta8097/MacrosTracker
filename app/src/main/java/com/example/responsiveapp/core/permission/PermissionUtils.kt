package com.example.responsiveapp.core.permission

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

fun Context.hasPermission(permission: String): Boolean =
    ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

fun Context.findActivity(): Activity? {
    var current = this
    while (current is ContextWrapper) {
        if (current is Activity) return current
        current = current.baseContext
    }
    return null
}

fun Context.deniedStatusAfterRequest(permission: String): PermissionStatus {
    val activity = findActivity() ?: return PermissionStatus.PERMANENTLY_DENIED
    return if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
        PermissionStatus.RATIONALE_REQUIRED
    } else {
        PermissionStatus.PERMANENTLY_DENIED
    }
}

fun Context.openAppSettings() {
    startActivity(
        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", packageName, null))
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    )
}
