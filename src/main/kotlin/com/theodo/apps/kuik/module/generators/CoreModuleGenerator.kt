package com.theodo.apps.kuik.module.generators

import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.ide.starters.local.GeneratorAsset
import com.intellij.ide.starters.local.GeneratorTemplateFile
import com.theodo.apps.kuik.common.models.KmpModuleModel
import com.theodo.apps.kuik.common.utils.TemplateGroup

class CoreModuleGenerator(
    private val params: KmpModuleModel,
) : ModuleCommonGenerator(params) {
    override fun generate(
        ftManager: FileTemplateManager,
        packageName: String,
    ) = buildList {
        addAll(super.generate(ftManager, packageName))

        operator fun GeneratorAsset.unaryPlus() = add(this)
        +GeneratorTemplateFile(
            "build.gradle.kts",
            ftManager.getCodeTemplate(TemplateGroup.MODULE_CORE_BUILD),
        )
    }
}
