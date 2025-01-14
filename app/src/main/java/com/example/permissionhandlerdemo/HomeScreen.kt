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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import kotlin.collections.toTypedArray


@Composable
fun HomeScreen(permissionViewmodel: PermissionViewmodel = viewModel(), modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val permissions = listOf(
        Manifest.permission.CALL_PHONE,
        Manifest.permission.CAMERA
    )
    val toShowrational by permissionViewmodel.showRationaleDialog



    var permissionStatus by remember { mutableStateOf(false) }




    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissionsResult ->
            permissionsResult.forEach { (permission, isGranted) ->
                permissionViewmodel.updatePermissionState(permission, isGranted)
            }
            if (PermissionUtils.areAllPermissionsGranted(context, permissions)) {
                 permissionStatus=true
                Toast.makeText(context, "All Permissions Granted", Toast.LENGTH_SHORT).show()

            } else {
                if (PermissionUtils.shouldShowRationaleForMultiplePermissions(
                        context,
                        permissions
                    )
                ) {
                    permissionViewmodel.updateShowRational(true)
                } else {
                    permissionStatus=false
                    Toast.makeText(context, "Some permissions were denied.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    )

    permissionViewmodel.setCurrentPermissions(permissions)//to register  the permissions in the viewmodel



    LaunchedEffect(Unit) {
        permissionLauncher.launch(permissions.toTypedArray())
    }







    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
    if (permissionStatus) {
        Text("Permissions Granted")

    }
            else{  Text("Permissions Not Granted")}

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
            body = permissionViewmodel.formatStringList(permissionViewmodel.getDeniedPermissions())
        )
    }
}