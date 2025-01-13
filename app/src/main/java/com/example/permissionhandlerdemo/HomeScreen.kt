package com.example.permissionhandlerdemo

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Modifier
import android.Manifest
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlin.collections.toTypedArray


@Composable
fun HomeScreen(permissionViewmodel: PermissionViewmodel = viewModel(), modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val permissions = listOf(
        Manifest.permission.CALL_PHONE,
        Manifest.permission.CAMERA
    )
    val toShowrational by permissionViewmodel.showRationaleDialog

    var showPermissionButton by remember { mutableStateOf(true) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissionsResult ->
            permissionsResult.forEach { (permission, isGranted) ->
                permissionViewmodel.updatePermissionState(permission, isGranted)
            }
            if (PermissionUtils.areAllPermissionsGranted(context, permissions)) {

                showPermissionButton = false
                Toast.makeText(context, "All Permissions Granted", Toast.LENGTH_SHORT).show()
            } else {
                if (PermissionUtils.shouldShowRationaleForMultiplePermissions(context, permissions)) {
                    permissionViewmodel.updateShowRational(true)
                } else {
                    Toast.makeText(context, "Some permissions were denied.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    )

    LaunchedEffect(Unit) {
        permissionViewmodel.setCurrentPermissions(permissions)
    }

    if (showPermissionButton) {
        Button(onClick = {
            if (!PermissionUtils.areAllPermissionsGranted(context, permissions)) {
                permissionLauncher.launch(permissions.toTypedArray())
            } else {
                Toast.makeText(context, "All Permissions Granted", Toast.LENGTH_SHORT).show()
                showPermissionButton = false
            }
        }) {
            Text("Request Permissions")
        }
    }

    if (toShowrational) {
        ShowRationaleDialog(
            onDismiss = { permissionViewmodel.updateShowRational(false) },
            onConfirm = {
                permissionViewmodel.updateShowRational(false)
                permissionLauncher.launch(permissionViewmodel.getCurrentPermissions().toTypedArray())
            },
            title = "Permissions Required",
            body = "This app needs these permissions to function properly."
        )
    }
}