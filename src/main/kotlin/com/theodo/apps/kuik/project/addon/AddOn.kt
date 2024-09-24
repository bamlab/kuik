package com.theodo.apps.kuik.project.addon

import com.intellij.ide.starters.local.GeneratorAsset
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.guessProjectDir
import com.intellij.openapi.vfs.VirtualFile
import com.theodo.apps.kuik.common.models.KmpModuleModel
import com.theodo.apps.kuik.module.model.ModuleType

abstract class AddOn {
    var model: KmpModuleModel = KmpModuleModel()

    abstract fun getMainProjectFiles(): List<GeneratorAsset>

    abstract fun moduleType(): ModuleType

    abstract fun moduleName(): String

    open fun shouldAddModuleDependencyToMainApp(): Boolean = false

    abstract fun generateAddOnModule(project: Project): List<GeneratorAsset>

    fun getModuleTypeDirectory(project: Project): VirtualFile? {
        val baseDir = project.guessProjectDir() ?: return null
        return baseDir.findFileByRelativePath(model.moduleType.folderName())
    }

    open fun initialize(projectPackageName: String) {
        model.moduleType = moduleType()
        model.packageName = "$projectPackageName.${moduleType().folderName()}"
        model.moduleName = moduleName()
        model.moduleLowerCase = moduleName().lowercase()
        model.shouldAddModuleDependencyToMainApp = shouldAddModuleDependencyToMainApp()
    }
}
