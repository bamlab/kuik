package com.theodo.apps.kuik.common.generators

import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.ide.starters.local.GeneratorAsset
import com.theodo.apps.kuik.common.models.KmpModuleModel

abstract class PlatformGenerator(protected val params: KmpModuleModel) {

    fun generate(
        ftManager: FileTemplateManager,
        packageName: String,
    ) = generateProject(ftManager, packageName)

    protected abstract fun generateProject(ftManager: FileTemplateManager, packageName: String): List<GeneratorAsset>

    fun commonFiles(
        ftManager: FileTemplateManager,
        packageName: String,
    ) = addToCommon(ftManager, packageName)

    protected abstract fun addToCommon(
        ftManager: FileTemplateManager,
        packageName: String,
    ): List<GeneratorAsset>
}
