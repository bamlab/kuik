package com.theodo.apps.kuik.project

import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.openapi.project.Project
import com.theodo.apps.kuik.common.models.KmpModuleModel
import com.theodo.apps.kuik.module.KmpModuleRecipe
import com.theodo.apps.kuik.module.extrafilemodifier.AddKoinModuleToMainKoinModule
import com.theodo.apps.kuik.module.extrafilemodifier.AddModuleDepsToMainApp
import com.theodo.apps.kuik.module.extrafilemodifier.AddModuleToSettingsGradle
import com.theodo.apps.kuik.module.extrafilemodifier.AddScreenRoute
import com.theodo.apps.kuik.module.extrafilemodifier.AddScreenToNavHost
import com.theodo.apps.kuik.module.model.ProjectHelper
import com.theodo.apps.kuik.project.addon.HomeAddOn
import com.theodo.apps.kuik.project.addon.NavigationAddOn
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkClass
import io.mockk.mockkObject
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject
import org.koin.test.mock.MockProviderRule
import org.koin.test.mock.declareMock

class KmpWizardTemplateTest : KoinTest {
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
                    declareMock<NavigationAddOn>()
                    declareMock<HomeAddOn>()
                    // factoryOf(::NavigationAddOn) // Not ready yet
                    // factoryOf(::HomeAddOn) // Not ready yet
                    factoryOf(::KmpModuleRecipe)
                    factoryOf(::KmpWizardTemplate)
                },
            )
        }

    @get:Rule
    val mockProvider = MockProviderRule.create { clazz -> mockkClass(type = clazz, relaxed = true) }

    @Test
    fun `Can run KmpWizardTemplate without error`() {
        // Given
        val template by inject<KmpWizardTemplate>()

        // When
        val assets =
            template.defineAssets(
                model = KmpModuleModel(),
            )

        // Then no throw
        assets.forEach {
            println(it.relativePath)
        }
    }

    companion object {
        @JvmStatic
        @BeforeClass
        fun setUp() {
            mockkObject(ProjectHelper)
            every { ProjectHelper.getProject() } returns mockk<Project>()
        }
    }
}
