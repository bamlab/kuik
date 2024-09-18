package com.theodo.apps.kuik.module

import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.ide.starters.local.GeneratorAsset
import com.intellij.ide.starters.local.GeneratorEmptyDirectory
import com.intellij.ide.starters.local.GeneratorTemplateFile
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.theodo.apps.kuik.common.models.*
import com.theodo.apps.kuik.common.utils.Utils
import com.theodo.apps.kuik.module.extrafilemodifier.AddKoinModuleToMainKoinModule
import com.theodo.apps.kuik.module.extrafilemodifier.AddModuleDepsToMainApp
import com.theodo.apps.kuik.module.extrafilemodifier.AddModuleToSettingsGradle
import com.theodo.apps.kuik.module.generators.factory.ModuleGeneratorFactory

class KmpModuleRecipe {
    fun executeRecipe(
        project: Project,
        model: KmpModuleModel,
        moduleDir: VirtualFile,
        additionalAssets: List<GeneratorAsset> = emptyList(),
    ) {
        val ftManager = FileTemplateManager.getInstance(project)
        val templateData =
            mapOf(
                "PACKAGE_NAME" to model.packageName,
                "SHARED_NAME" to model.moduleLowerCase,
                "MODULE_NAME" to model.moduleName,
                "HAS_ANDROID" to model.hasAndroid,
                "HAS_IOS" to model.hasIOS,
                "HAS_WEB" to model.hasWeb,
                "HAS_DESKTOP" to model.hasDesktop,
                "BUILD_VERSION_SDK_INT" to "\${Build.VERSION.SDK_INT}",
                "JVM_JAVA_VERSION" to "\${System.getProperty(\"java.version\")}",
                model.hasAndroid(),
                model.hasDesktop(),
                model.hasIOS(),
                model.hasWeb(),
                model.hasServer(),
            )

        val generatorAssets = mutableListOf<GeneratorAsset>()
        val moduleGenerator = ModuleGeneratorFactory.generate(model)
        val existingFileModifiers =
            buildList {
                // 1 - Add module to settings.gradle.kts
                add(AddModuleToSettingsGradle())
                // 2 - add Module dep to main app
                if (moduleGenerator.shouldAddModuleDependencyToMainApp()) {
                    add(AddModuleDepsToMainApp())
                }
                if (moduleGenerator.shouldAddKoinModuleToMainKoinModule()) {
                    add(AddKoinModuleToMainKoinModule())
                }
            }
        for (modifier in existingFileModifiers) {
            modifier.modify(model, project)
        }
        // 3 - Generate module files
        val moduleCommonList = moduleGenerator.generate(generatorAssets, ftManager, model.packageName)
        generatorAssets.addAll(moduleCommonList)
        // 4 - Add additional assets
        generatorAssets.addAll(additionalAssets)

        // Finally generate assets
        generatorAssets.forEach { asset ->
            when (asset) {
                is GeneratorEmptyDirectory -> createEmptyDirectory(moduleDir, asset.relativePath)
                is GeneratorTemplateFile ->
                    Utils.generateFileFromTemplate(
                        templateName = "${asset.template.name}.${asset.template.extension}",
                        dataModel = templateData,
                        outputDir = moduleDir,
                        outputFilePath = asset.relativePath,
                    )

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
