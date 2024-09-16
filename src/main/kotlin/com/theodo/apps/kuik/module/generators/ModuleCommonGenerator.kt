package com.theodo.apps.kuik.module.generators

import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.ide.starters.local.GeneratorAsset
import com.intellij.ide.starters.local.GeneratorEmptyDirectory
import com.intellij.ide.starters.local.GeneratorTemplateFile
import com.theodo.apps.kuik.common.generators.*
import com.theodo.apps.kuik.common.models.KmpModuleModel
import com.theodo.apps.kuik.common.utils.TemplateGroup

open class ModuleCommonGenerator(
    private val params: KmpModuleModel,
) {
    open fun generate(
        list: MutableList<GeneratorAsset>,
        ftManager: FileTemplateManager,
        packageName: String,
    ) = list.apply {
        operator fun GeneratorAsset.unaryPlus() = add(this)

        val generatorList: List<PlatformGenerator> =
            listOfNotNull(
                if (params.hasAndroid) AndroidGenerator(params, false) else null,
                if (params.hasIOS) IOSGenerator(params, false) else null,
            )

        // Common
        +GeneratorEmptyDirectory("src/commonMain/kotlin/${packageName.replace(".", "/")}/${params.moduleLowerCase}")
        +GeneratorTemplateFile(
            "build.gradle.kts",
            ftManager.getCodeTemplate(TemplateGroup.MODULE_FEATURE_BUILD),
        )

        addAll(generatorList.flatMap { it.commonFiles(ftManager, packageName) })
        addAll(generatorList.flatMap { it.generate(ftManager, packageName) })
    }
}
