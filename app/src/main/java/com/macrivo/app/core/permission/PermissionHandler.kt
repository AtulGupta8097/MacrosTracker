package com.macrivo.app.core.permission

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect

@Stable
class PermissionHandler(
    val status: PermissionStatus,
    val request: () -> Unit,
    val openSettings: () -> Unit,
)

@Composable
fun rememberPermissionHandler(
    permission: String,
    requestOnFirstComposition: Boolean = true,
): PermissionHandler {
    val context = LocalContext.current
    var hasRequested by rememberSaveable(permission) { mutableStateOf(false) }
    var autoRequested by rememberSaveable(permission) { mutableStateOf(false) }

    fun resolveStatus(): PermissionStatus = when {
        context.hasPermission(permission) -> PermissionStatus.GRANTED
        !hasRequested -> PermissionStatus.NOT_REQUESTED
        else -> context.deniedStatusAfterRequest(permission)
    }

    var status by remember { mutableStateOf(resolveStatus()) }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        hasRequested = true
        status = resolveStatus()
    }

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        status = resolveStatus()
    }

    LaunchedEffect(Unit) {
        if (requestOnFirstComposition && !autoRequested && status == PermissionStatus.NOT_REQUESTED) {
            autoRequested = true
            launcher.launch(permission)
        }
    }

    return remember(status) {
        PermissionHandler(
            status = status,
            request = { launcher.launch(permission) },
            openSettings = { context.openAppSettings() }
        )
    }
}
