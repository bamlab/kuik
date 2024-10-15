package com.theodo.apps.kuik.module

import com.intellij.ide.starters.local.GeneratorAsset
import com.intellij.ide.starters.local.GeneratorEmptyDirectory
import com.intellij.ide.starters.local.GeneratorTemplateFile
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.theodo.apps.kuik.common.models.justCopy
import com.theodo.apps.kuik.common.utils.Utils

class ModuleAssetGenerator {
    fun generateAssets(
        moduleDir: VirtualFile,
        generatorAssets: List<GeneratorAsset>,
        templateData: Map<String, Any>,
    ) {
        generatorAssets.forEach { asset ->
            when (asset) {
                is GeneratorEmptyDirectory -> createEmptyDirectory(moduleDir, asset.relativePath)
                is GeneratorTemplateFile ->
                    if (asset.justCopy) {
                        Utils.copyFileFromTemplate(
                            fileName = "${asset.template.name}.${asset.template.extension}",
                            outputDir = moduleDir,
                            outputFilePath = asset.relativePath,
                        )
                    } else {
                        Utils.generateFileFromTemplate(
                            templateName = "${asset.template.name}.${asset.template.extension}",
                            dataModel = templateData,
                            outputDir = moduleDir,
                            outputFilePath = asset.relativePath,
                        )
                    }

                else -> println("Module Generator: Nothing")
            }
        }
    }

    private fun createEmptyDirectory(
        parent: VirtualFile,
        path: String,
    ) {
        VfsUtil.createDirectoryIfMissing(parent, path)
    }
}
