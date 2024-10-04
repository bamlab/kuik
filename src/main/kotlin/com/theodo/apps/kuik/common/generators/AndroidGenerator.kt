package com.theodo.apps.kuik.common.generators

import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.ide.starters.local.GeneratorAsset
import com.intellij.ide.starters.local.GeneratorEmptyDirectory
import com.intellij.ide.starters.local.GeneratorTemplateFile
import com.theodo.apps.kuik.common.models.KmpModuleModel
import com.theodo.apps.kuik.common.utils.TemplateGroup
import com.theodo.apps.kuik.common.utils.toFolders

class AndroidGenerator(
    params: KmpModuleModel,
    private val isProject: Boolean,
) : PlatformGenerator(params) {
    override fun generateProject(
        ftManager: FileTemplateManager,
        packageName: String,
    ): List<GeneratorAsset> =
        if (isProject) {
            listOf(
                GeneratorTemplateFile(
                    "${params.composeNameLowerCase()}/src/androidMain/kotlin/${packageName.toFolders()}/${params.composeNameLowerCase()}/MainActivity.kt",
                    ftManager.getCodeTemplate(TemplateGroup.ANDROID_MAINACTIVITY),
                ),
                GeneratorTemplateFile(
                    "${params.composeNameLowerCase()}/src/androidMain/AndroidManifest.xml",
                    ftManager.getCodeTemplate(TemplateGroup.ANDROID_MANIFEST),
                ),
                GeneratorTemplateFile(
                    "${params.composeNameLowerCase()}/src/androidMain/res/values/strings.xml",
                    ftManager.getCodeTemplate(TemplateGroup.ANDROID_VALUES_XML),
                ),
            )
        } else {
            emptyList()
        }

    override fun addToCommon(
        ftManager: FileTemplateManager,
        packageName: String,
    ): List<GeneratorAsset> =
        if (isProject) {
            emptyList()
        } else {
            listOf(
                GeneratorEmptyDirectory(
                    "src/androidMain/kotlin/${
                        packageName.toFolders()
                    }/${params.moduleLowerCase}",
                ),
            )
        }
}
