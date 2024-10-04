package com.theodo.apps.kuik.module

import com.android.tools.idea.nav.safeargs.psi.java.toUpperCamelCase
import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.ide.starters.local.GeneratorAsset
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.theodo.apps.kuik.common.models.KmpModuleModel
import com.theodo.apps.kuik.module.extrafilemodifier.AddKoinModuleToMainKoinModule
import com.theodo.apps.kuik.module.extrafilemodifier.AddModuleDepsToMainApp
import com.theodo.apps.kuik.module.extrafilemodifier.AddModuleToSettingsGradle
import com.theodo.apps.kuik.module.extrafilemodifier.AddScreenRoute
import com.theodo.apps.kuik.module.extrafilemodifier.AddScreenToNavHost
import com.theodo.apps.kuik.module.generators.FeatureModuleGenerator
import com.theodo.apps.kuik.module.generators.factory.ModuleGeneratorFactory
import org.jetbrains.annotations.VisibleForTesting
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class KmpModuleRecipe(
    private val assetGenerator: ModuleAssetGenerator,
    private val ftManager: FileTemplateManager,
) : KoinComponent {
    fun executeRecipe(
        project: Project,
        model: KmpModuleModel,
        moduleDir: VirtualFile,
        additionalAssets: List<GeneratorAsset> = emptyList(),
    ): List<GeneratorAsset> {
        val assetsToGenerate =
            defineAssets(
                project = project,
                model = model,
                additionalAssets = additionalAssets,
            )
        assetGenerator.generateAssets(
            moduleDir = moduleDir,
            generatorAssets = assetsToGenerate,
            templateData = templateData(model),
        )
        return assetsToGenerate
    }

    private fun templateData(model: KmpModuleModel): Map<String, Any> =
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

    @VisibleForTesting
    fun defineAssets(
        project: Project,
        model: KmpModuleModel,
        additionalAssets: List<GeneratorAsset> = emptyList(),
    ): List<GeneratorAsset> {
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
        val moduleCommonList = moduleGenerator.generate(ftManager, model.packageName)
        generatorAssets.addAll(moduleCommonList)

        return generatorAssets
    }
}
