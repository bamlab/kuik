package com.theodo.apps.kuik.module.generators

import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.ide.starters.local.GeneratorAsset
import com.intellij.ide.starters.local.GeneratorTemplateFile
import com.theodo.apps.kuik.common.models.KmpModuleModel
import com.theodo.apps.kuik.common.utils.TemplateGroup
import com.theodo.apps.kuik.common.utils.toFolders

class FeatureModuleGenerator(
    private val params: KmpModuleModel,
) : ModuleCommonGenerator(params) {
    override fun generate(
        list: MutableList<GeneratorAsset>,
        ftManager: FileTemplateManager,
        packageName: String,
    ): MutableList<GeneratorAsset> =
        super.generate(list, ftManager, packageName).apply {
            operator fun GeneratorAsset.unaryPlus() = add(this)
            +GeneratorTemplateFile(
                "src/commonMain/kotlin/${params.packageName.toFolders()}/${params.moduleLowerCase}/${params.moduleUpperCamelCase()}Screen.kt",
                ftManager.getCodeTemplate(TemplateGroup.MODULE_SCREEN),
            )
            +GeneratorTemplateFile(
                "build.gradle.kts",
                ftManager.getCodeTemplate(TemplateGroup.MODULE_FEATURE_BUILD),
            )
        }

    override fun shouldAddModuleDependencyToMainApp(): Boolean = true

    override fun shouldAddKoinModuleToMainKoinModule(): Boolean = true
}
