package com.theodo.apps.kuik.module

import com.android.tools.idea.nav.safeargs.psi.java.toUpperCamelCase
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
import com.theodo.apps.kuik.module.extrafilemodifier.AddScreenRoute
import com.theodo.apps.kuik.module.extrafilemodifier.AddScreenToNavHost
import com.theodo.apps.kuik.module.generators.FeatureModuleGenerator
import com.theodo.apps.kuik.module.generators.factory.ModuleGeneratorFactory
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class KmpModuleRecipe : KoinComponent {
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
                "MODULE_UPPER_CAMELCASE_NAME" to model.moduleName.toUpperCamelCase(),
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
                add(get<AddModuleToSettingsGradle>())
                // 2 - add Module dep to main app
                if (moduleGenerator.shouldAddModuleDependencyToMainApp()) {
                    add(get<AddModuleDepsToMainApp>())
                }
                if (moduleGenerator.shouldAddKoinModuleToMainKoinModule()) {
                    add(get<AddKoinModuleToMainKoinModule>())
                }
                if (moduleGenerator is FeatureModuleGenerator) {
                    addAll(listOf(get<AddScreenRoute>(), get<AddScreenToNavHost>()))
                }
            }
        for (modifier in existingFileModifiers) {
            modifier.modify(model, project)
        }
        // 3 - Add additional assets can override module files
        generatorAssets.addAll(additionalAssets)
        // 4 - Generate module files
        val moduleCommonList = moduleGenerator.generate(generatorAssets, ftManager, model.packageName)
        generatorAssets.addAll(moduleCommonList)

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
