package io.github.estivensh4.kotlinmultiplatformwizard.common.generators

import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.ide.starters.local.GeneratorAsset
import com.intellij.ide.starters.local.GeneratorEmptyDirectory
import com.intellij.ide.starters.local.GeneratorTemplateFile
import io.github.estivensh4.kotlinmultiplatformwizard.common.models.KmpModuleModel
import io.github.estivensh4.kotlinmultiplatformwizard.common.utils.TemplateGroup

class AndroidGenerator(
    params: KmpModuleModel,
    private val isProject: Boolean
) : PlatformGenerator(params) {

    override fun generateProject(ftManager: FileTemplateManager, packageName: String): List<GeneratorAsset> {
        return if (isProject) {
            listOf(
                GeneratorTemplateFile(
                    "${params.composeName}/src/androidMain/kotlin/$packageName/${params.composeName}/MainActivity.kt",
                    ftManager.getCodeTemplate(TemplateGroup.ANDROID_MAINACTIVITY)
                ),
                GeneratorTemplateFile(
                    "${params.composeName}/src/androidMain/AndroidManifest.xml",
                    ftManager.getCodeTemplate(TemplateGroup.ANDROID_MANIFEST)
                ),
                GeneratorTemplateFile(
                    "${params.composeName}/src/androidMain/res/values/strings.xml",
                    ftManager.getCodeTemplate(TemplateGroup.ANDROID_VALUES_XML)
                ),
            )
        } else emptyList()
    }

    override fun addToCommon(ftManager: FileTemplateManager, packageName: String): List<GeneratorAsset> {
        return listOf(
            GeneratorEmptyDirectory("src/androidMain/kotlin/${packageName.replace(".", "/")}/${params.moduleLowerCase}")
        )
    }
}
