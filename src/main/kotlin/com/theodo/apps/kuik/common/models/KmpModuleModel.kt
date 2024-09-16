package com.theodo.apps.kuik.common.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.android.tools.idea.wizard.model.WizardModel
import com.theodo.apps.kuik.module.model.ModuleType

class KmpModuleModel : WizardModel() {
    var hasAndroid: Boolean by mutableStateOf(true)
    var hasWeb: Boolean by mutableStateOf(true)
    var hasIOS: Boolean by mutableStateOf(false)
    var hasDesktop: Boolean by mutableStateOf(false)
    var hasServer: Boolean by mutableStateOf(false)

    var packageName by mutableStateOf("com.example")
    var moduleLowerCase by mutableStateOf("shared")
    var composeName by mutableStateOf("ComposeApp")
    var serverName by mutableStateOf("server")
    var moduleName by mutableStateOf("")
    var moduleType by mutableStateOf(ModuleType.FEATURE)
    var shouldAddModuleDependencyToMainApp by mutableStateOf(false)

    fun composeNameLowerCase() = composeName.lowercase()

    override fun handleFinished() {
    }
}

fun KmpModuleModel.hasAndroid() = "HAS_ANDROID" to hasAndroid

fun KmpModuleModel.hasIOS() = "HAS_IOS" to hasIOS

fun KmpModuleModel.hasWeb() = "HAS_WEB" to hasWeb

fun KmpModuleModel.hasDesktop() = "HAS_DESKTOP" to hasDesktop

fun KmpModuleModel.hasServer() = "HAS_SERVER" to hasServer
