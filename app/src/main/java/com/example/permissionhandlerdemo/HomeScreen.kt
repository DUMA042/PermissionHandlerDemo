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




@Composable
fun HomeScreen(permissionViewmodel: PermissionViewmodel=viewModel(),modifier: Modifier=Modifier){

    val context = LocalContext.current
    val permissions= Manifest.permission.ACCESS_FINE_LOCATION

    val toShowrational by  permissionViewmodel.showRationaleDialog
    val permissionState by permissionViewmodel.permistionState

    val permissionLauncher= rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                permissionViewmodel.updatePermistionState(true)
            } else {

                if (PermissionUtils.shouldShowRationale(context,permissions)) {
                    //will be changed
                    permissionViewmodel.updateShowRational(true)
                }
                Toast.makeText(
                    context,
                    "Camera permission was denied.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    )



    LaunchedEffect(Unit) {

        permissionLauncher.launch(permissions)

    }




    if(toShowrational){
        ShowRationaleDialog(
            onDismiss = { permissionViewmodel.updateShowRational(false) },
            onConfirm = {
                permissionViewmodel.updateShowRational(false) // Dismiss the dialog
                permissionLauncher.launch(permissions) // Retry permission
            }
        )

    }

    if(permissionState){
        NotificationUtils.sendtheNotification(context,"Notification Demo","The permission has been accepted!!!")
        Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()

    }


}