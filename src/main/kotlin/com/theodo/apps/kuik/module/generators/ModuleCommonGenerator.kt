package com.theodo.apps.kuik.module.generators

import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.ide.starters.local.GeneratorAsset
import com.intellij.ide.starters.local.GeneratorEmptyDirectory
import com.intellij.ide.starters.local.GeneratorTemplateFile
import com.theodo.apps.kuik.common.generators.AndroidGenerator
import com.theodo.apps.kuik.common.generators.IOSGenerator
import com.theodo.apps.kuik.common.generators.PlatformGenerator
import com.theodo.apps.kuik.common.models.KmpModuleModel
import com.theodo.apps.kuik.common.utils.TemplateGroup

abstract class ModuleCommonGenerator(
    private val params: KmpModuleModel,
) {
    open fun generate(
        ftManager: FileTemplateManager,
        packageName: String,
    ): List<GeneratorAsset> =
        buildList {
            operator fun GeneratorAsset.unaryPlus() = add(this)

            val generatorList: List<PlatformGenerator> =
                listOfNotNull(
                    if (params.hasAndroid) AndroidGenerator(params, false) else null,
                    if (params.hasIOS) IOSGenerator(params, false) else null,
                )

            // Common
            +GeneratorEmptyDirectory("src/commonMain/kotlin/${packageName.replace(".", "/")}/${params.moduleLowerCase}")
            +GeneratorTemplateFile(
                "src/commonMain/kotlin/${
                    packageName.replace(
                        ".",
                        "/",
                    )
                }/${params.moduleLowerCase}/di/${params.moduleName}KoinModule.kt",
                ftManager.getCodeTemplate(TemplateGroup.MODULE_KOIN_MODULE),
            )

            addAll(generatorList.flatMap { it.commonFiles(ftManager, packageName) })
            addAll(generatorList.flatMap { it.generate(ftManager, packageName) })
        }

    open fun shouldAddModuleDependencyToMainApp(): Boolean = params.shouldAddModuleDependencyToMainApp

    open fun shouldAddKoinModuleToMainKoinModule(): Boolean = false
}
