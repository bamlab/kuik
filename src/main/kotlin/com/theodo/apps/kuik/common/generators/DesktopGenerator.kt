package com.theodo.apps.kuik.common.generators

import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.ide.starters.local.GeneratorAsset
import com.intellij.ide.starters.local.GeneratorTemplateFile
import com.theodo.apps.kuik.common.models.KmpModuleModel
import com.theodo.apps.kuik.common.utils.TemplateGroup
import com.theodo.apps.kuik.common.utils.toFolders

class DesktopGenerator(params: KmpModuleModel, private val isProject: Boolean) : PlatformGenerator(params) {
    override fun generateProject(ftManager: FileTemplateManager, packageName: String): List<GeneratorAsset> {
        return if (isProject) {
            listOf(
                GeneratorTemplateFile(
                    "${params.composeName}/src/desktopMain/kotlin${packageName.toFolders()}/${params.composeName}/main.kt",
                    ftManager.getCodeTemplate(TemplateGroup.DESKTOP_MAIN)
                )
            )
        } else emptyList()
    }

    override fun addToCommon(ftManager: FileTemplateManager, packageName: String): List<GeneratorAsset> {
        val relativePath = if (isProject) {
            "${params.moduleLowerCase}/src/jvmMain/kotlin${packageName.toFolders()}/${params.moduleLowerCase}/Platform.jvm.kt"
        } else {
            "src/jvmMain/kotlin${packageName.toFolders()}/${params.moduleName}/Platform.jvm.kt"
        }
        return listOf(
            GeneratorTemplateFile(
                relativePath,
                ftManager.getCodeTemplate(TemplateGroup.JVM_PLATFORM)
            )
        )
    }
}
