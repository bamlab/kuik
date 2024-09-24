package com.theodo.apps.kuik.project

import com.android.tools.idea.wizard.template.ProjectTemplateData
import com.intellij.ide.starters.local.GeneratorAsset
import com.intellij.ide.starters.local.GeneratorEmptyDirectory
import com.intellij.ide.starters.local.GeneratorTemplateFile
import com.theodo.apps.kuik.common.utils.Utils
import org.jetbrains.kotlin.idea.core.util.toVirtualFile

class ProjectAssetGenerator {
    fun generateAssets(
        projectData: ProjectTemplateData,
        generatorAssets: MutableList<GeneratorAsset>,
        dataModel: Map<String, Any>,
    ) {
        val virtualFile = projectData.rootDir.toVirtualFile()
        virtualFile?.let { file ->
            generatorAssets.forEach { asset ->
                when (asset) {
                    is GeneratorEmptyDirectory -> createEmptyDirectory(file, asset.relativePath)
                    is GeneratorTemplateFile ->
                        Utils.generateFileFromTemplate(
                            templateName = "${asset.template.name}.${asset.template.extension}",
                            dataModel = dataModel,
                            outputDir = file,
                            outputFilePath = asset.relativePath,
                        )

                    else -> println("Generator Asset: Nothing")
                }
            }
        }
    }
}
