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

class HomeAddOnTest : KoinTest {
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
                    declareMock<ProjectAssetGenerator>()
                    declareMock<Project>()
                    declareMock<VirtualFile>()
                    declareMock<ModuleAssetGenerator>()
                    factoryOf(::KmpModuleRecipe)
                    factoryOf(::HomeAddOn)
                },
            )
        }

    @get:Rule
    val mockProvider = MockProviderRule.create { clazz -> mockkClass(type = clazz, relaxed = true) }

    @Test
    fun `WHEN add on generate assets then it return the correct assets`() {
        // Given
        val addOn by inject<HomeAddOn>()
        addOn.initialize("com.theodo.apps.kuik")

        // When
        val assets = addOn.generateAssests(get(), get())

        // Then
        assertListEquals(
            expected =
                listOf(
                    "src/commonMain/kotlin/com/theodo/apps/kuik/feature/home",
                    "src/commonMain/kotlin/com/theodo/apps/kuik/feature/home/di/homeKoinModule.kt",
                    "src/androidMain/kotlin/com/theodo/apps/kuik/feature/home",
                    "src/iosMain/kotlin/com/theodo/apps/kuik/feature/home",
                    "src/commonMain/kotlin/com/theodo/apps/kuik/feature/home/HomeScreen.kt",
                    "build.gradle.kts",
                ),
            actual = assets.map { it.relativePath },
        )
    }
}
