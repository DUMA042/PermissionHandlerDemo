package com.example.permissionhandlerdemo

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.State


class PermissionViewmodel:ViewModel() {

    private val _permistionState = mutableStateOf<Boolean>(false)
    val permistionState: State<Boolean> get() = _permistionState


    private val _showRationaleDialog = mutableStateOf(false)
    val showRationaleDialog get() = _showRationaleDialog


    fun updatePermistionState(newValue: Boolean = false){
        _permistionState.value= newValue
    }


    fun updateShowRational(newOption: Boolean = true){
        _showRationaleDialog.value= newOption
    }


}