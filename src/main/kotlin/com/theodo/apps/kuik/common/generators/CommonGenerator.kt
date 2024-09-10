package com.theodo.apps.kuik.common.generators

import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.ide.starters.local.GeneratorAsset
import com.intellij.ide.starters.local.GeneratorTemplateFile
import com.theodo.apps.kuik.common.models.KmpModuleModel
import com.theodo.apps.kuik.common.utils.TemplateGroup
import com.theodo.apps.kuik.common.utils.toFolders

class CommonGenerator(
    private val params: KmpModuleModel,
) {
    fun generate(
        list: MutableList<GeneratorAsset>,
        ftManager: FileTemplateManager,
        packageName: String,
    ) = list.apply {
        operator fun GeneratorAsset.unaryPlus() = add(this)

        val generatorList: List<PlatformGenerator> = listOfNotNull(
            if (params.hasAndroid) com.theodo.apps.kuik.common.generators.AndroidGenerator(params, true) else null,
            if (params.hasDesktop) DesktopGenerator(params, true) else null,
            if (params.hasWeb) WasmGenerator(params, true) else null,
            if (params.hasIOS) IOSGenerator(params, true) else null,
            if (params.hasServer) ServerGenerator(params, true) else null,
            BuildLogicGenerator(params),
        )

        //Project
        +GeneratorTemplateFile(
            "build.gradle.kts",
            ftManager.getCodeTemplate(TemplateGroup.COMPOSE_PROJECT_GRADLE)
        )

        +GeneratorTemplateFile(
            "settings.gradle.kts",
            ftManager.getCodeTemplate(TemplateGroup.PROJECT_SETTINGS)
        )

        +GeneratorTemplateFile(
            "gradle.properties",
            ftManager.getCodeTemplate(TemplateGroup.PROJECT_GRADLE)
        )

        +GeneratorTemplateFile(
            "gradle/wrapper/gradle-wrapper.properties",
            ftManager.getCodeTemplate(TemplateGroup.GRADLE_WRAPPER_PROPERTIES)
        )

        +GeneratorTemplateFile(
            "gradle/libs.versions.toml",
            ftManager.getCodeTemplate(TemplateGroup.PROJECT_TOML)
        )

        //Common
        +GeneratorTemplateFile(
            "${params.composeName}/src/commonMain/kotlin/${packageName.toFolders()}/${params.composeName}/App.kt",
            ftManager.getCodeTemplate(TemplateGroup.COMMON_APP)
        )

        +GeneratorTemplateFile(
            "${params.composeName}/src/commonMain/composeResources/drawable/compose-multiplatform.xml",
            ftManager.getCodeTemplate(TemplateGroup.COMMON_COMPOSE_RESOURCES_MULTIPLATFORM_XML)
        )

        +GeneratorTemplateFile(
            "${params.composeName}/build.gradle.kts",
            ftManager.getCodeTemplate(TemplateGroup.COMPOSE_GRADLE_KTS)
        )

        addAll(generatorList.flatMap { it.commonFiles(ftManager, packageName) })
        addAll(generatorList.flatMap { it.generate(ftManager, packageName) })
    }
}
