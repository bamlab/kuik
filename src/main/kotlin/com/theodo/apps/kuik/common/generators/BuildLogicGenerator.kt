package com.theodo.apps.kuik.common.generators

import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.ide.starters.local.GeneratorAsset
import com.intellij.ide.starters.local.GeneratorTemplateFile
import com.theodo.apps.kuik.common.models.KmpModuleModel
import com.theodo.apps.kuik.common.utils.TemplateGroup

class BuildLogicGenerator(
    params: KmpModuleModel,
) : PlatformGenerator(params) {
    override fun generateProject(
        ftManager: FileTemplateManager,
        packageName: String,
    ): List<GeneratorAsset> =
        listOf(
            // Configuration
            GeneratorTemplateFile(
                "build-logic/gradle.properties",
                ftManager.getCodeTemplate(TemplateGroup.BUILD_LOGIC_GRADLE),
            ),
            GeneratorTemplateFile(
                "build-logic/settings.gradle.kts",
                ftManager.getCodeTemplate(TemplateGroup.BUILD_LOGIC_SETTINGS),
            ),

            // Convention
            GeneratorTemplateFile(
                "build-logic/convention/build.gradle.kts",
                ftManager.getCodeTemplate(TemplateGroup.BUILD_LOGIC_GRADLE_KTS),
            ),
            GeneratorTemplateFile(
                "build-logic/convention/src/main/kotlin/Libs.kt",
                ftManager.getCodeTemplate(TemplateGroup.BUILD_LOGIC_LIBS),
            ),
            GeneratorTemplateFile(
                "build-logic/src/main/kotlin/convention/KotlinMultiplatformFeatureModule.kt",
                ftManager.getCodeTemplate(TemplateGroup.BUILD_LOGIC_KOTLIN_MULTIPLATFORM_FEATURE_MODULE),
            ),
            GeneratorTemplateFile(
                "build-logic/src/main/kotlin/convention/KmpModule.kt",
                ftManager.getCodeTemplate(TemplateGroup.BUILD_LOGIC_KMP_MODULE),
            ),
            GeneratorTemplateFile(
                "build-logic/src/main/kotlin/convention/AndroidSdkVersion.kt",
                ftManager.getCodeTemplate(TemplateGroup.BUILD_LOGIC_ANDROID_SDK_VERSION),
            ),

            )


    override fun addToCommon(
        ftManager: FileTemplateManager,
        packageName: String,
    ): List<GeneratorAsset> =
        emptyList()
}
