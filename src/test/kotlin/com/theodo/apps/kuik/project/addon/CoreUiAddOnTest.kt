package com.theodo.apps.kuik.project.addon

import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.theodo.apps.kuik.assertListEquals
import com.theodo.apps.kuik.module.KmpModuleRecipe
import com.theodo.apps.kuik.module.ModuleAssetGenerator
import com.theodo.apps.kuik.module.extrafilemodifier.AddKoinModuleToMainKoinModule
import com.theodo.apps.kuik.module.extrafilemodifier.AddModuleDepsToMainApp
import com.theodo.apps.kuik.module.extrafilemodifier.AddModuleToSettingsGradle
import com.theodo.apps.kuik.module.extrafilemodifier.AddScreenRoute
import com.theodo.apps.kuik.module.extrafilemodifier.AddScreenToNavHost
import com.theodo.apps.kuik.project.ProjectAssetGenerator
import io.mockk.mockkClass
import org.junit.Rule
import org.junit.Test
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.get
import org.koin.test.inject
import org.koin.test.mock.MockProviderRule
import org.koin.test.mock.declareMock

class CoreUiAddOnTest : KoinTest {
    @get:Rule
    val koinTestRule =
        KoinTestRule.create {
            modules(
                module {
                    declareMock<AddKoinModuleToMainKoinModule>()
                    declareMock<AddModuleDepsToMainApp>()
                    declareMock<AddModuleToSettingsGradle>()
                    declareMock<AddScreenRoute>()
                    declareMock<AddScreenToNavHost>()
                    declareMock<FileTemplateManager>()
                    declareMock<ProjectAssetGenerator>()
                    declareMock<Project>()
                    declareMock<VirtualFile>()
                    declareMock<ModuleAssetGenerator>()
                    factoryOf(::KmpModuleRecipe)
                    factoryOf(::CoreUiAddOn)
                },
            )
        }

    @get:Rule
    val mockProvider = MockProviderRule.create { clazz -> mockkClass(type = clazz, relaxed = true) }

    @Test
    fun `WHEN add on generate assets then it return the correct assets`() {
        // Given
        val addOn by inject<CoreUiAddOn>()
        addOn.initialize("com.theodo.apps.kuik")

        // When
        val assets = addOn.generateAssests(get(), get())

        println(assets.map { it.relativePath })
        // Then
        assertListEquals(
            expected =
            listOf(
                "src/commonMain/kotlin/com/theodo/apps/kuik/core/ui",
                "src/commonMain/kotlin/com/theodo/apps/kuik/core/ui/theme/Color.kt",
                "src/commonMain/kotlin/com/theodo/apps/kuik/core/ui/theme/Theme.kt",
                "src/commonMain/kotlin/com/theodo/apps/kuik/core/ui/theme/Type.kt",
                "src/commonMain/composeResources/font/rubik_mono_one_regular.ttf",
                "src/commonMain/composeResources/font/rubik_variable.ttf",
                "src/commonMain/composeResources/font/rubik_variable_italic.ttf",
                "src/commonMain/kotlin/com/theodo/apps/kuik/core/ui/di/uiKoinModule.kt",
                "src/androidMain/kotlin/com/theodo/apps/kuik/core/ui",
                "src/iosMain/kotlin/com/theodo/apps/kuik/core/ui",
                "build.gradle.kts",
            ),
            actual = assets.map { it.relativePath },
        )
    }
}
