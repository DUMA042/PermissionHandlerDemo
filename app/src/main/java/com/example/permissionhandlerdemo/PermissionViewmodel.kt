package com.example.permissionhandlerdemo

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel




class PermissionViewmodel : ViewModel() {

    private val _permissionStates = mutableStateOf<Map<String, Boolean>>(emptyMap())

    private val _showRationaleDialog = mutableStateOf(false)
    val showRationaleDialog get() = _showRationaleDialog

    private var _currentPermissions: List<String> = emptyList()

    fun updatePermissionState(permission: String, isGranted: Boolean) {
        _permissionStates.value = _permissionStates.value.toMutableMap().apply {
            this[permission] = isGranted
        }
    }

    fun updateShowRational(newOption: Boolean = true) {
        _showRationaleDialog.value = newOption
    }

    fun setCurrentPermissions(permissions: List<String>) {
        _currentPermissions = permissions
    }

    fun getCurrentPermissions(): List<String> {
        return _currentPermissions
    }

    fun getDeniedPermissions(): List<String> {
        return _permissionStates.value.filterValues { !it }.keys.toList()
    }

    fun formatStringList(strings: List<String>): String {
        if (strings.isEmpty()) {
            return ""
        }
        if (strings.size == 1) {
            return strings[0]
        }

        val builder = StringBuilder()
        for (i in 0 until strings.size - 1) {
            builder.append(strings[i])
            if (i < strings.size - 2) {
                builder.append(", ")
            } else {
                builder.append(" and ")
            }
        }
        builder.append(strings.last())
        return builder.toString()
    }
}