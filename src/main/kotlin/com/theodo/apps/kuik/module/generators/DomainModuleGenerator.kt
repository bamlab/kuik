package com.theodo.apps.kuik.module.generators

import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.ide.starters.local.GeneratorAsset
import com.intellij.ide.starters.local.GeneratorTemplateFile
import com.theodo.apps.kuik.common.generators.*
import com.theodo.apps.kuik.common.models.KmpModuleModel
import com.theodo.apps.kuik.common.utils.TemplateGroup

class DomainModuleGenerator(
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
                "build.gradle.kts",
                ftManager.getCodeTemplate(TemplateGroup.MODULE_DOMAIN_BUILD),
            )
        }
}
