package com.theodo.apps.kuik.project

import com.android.ide.common.repository.AgpVersion
import com.android.tools.idea.wizard.template.Language
import com.android.tools.idea.wizard.template.ProjectTemplateData
import com.intellij.ide.fileTemplates.FileTemplateManager
import com.theodo.apps.kuik.common.models.KmpModuleModel
import com.theodo.apps.kuik.module.KmpModuleRecipe
import com.theodo.apps.kuik.module.extrafilemodifier.AddKoinModuleToMainKoinModule
import com.theodo.apps.kuik.module.extrafilemodifier.AddModuleDepsToMainApp
import com.theodo.apps.kuik.module.extrafilemodifier.AddModuleToSettingsGradle
import com.theodo.apps.kuik.module.extrafilemodifier.AddScreenRoute
import com.theodo.apps.kuik.module.extrafilemodifier.AddScreenToNavHost
import com.theodo.apps.kuik.project.addon.HomeAddOn
import com.theodo.apps.kuik.project.addon.NavigationAddOn
import io.mockk.mockkClass
import org.junit.Rule
import org.junit.Test
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject
import org.koin.test.mock.MockProviderRule
import org.koin.test.mock.declareMock
import java.io.File

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
                    factoryOf(::NavigationAddOn)
                    factoryOf(::HomeAddOn)
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
        template.defineAssets(
            projectData = mockProjectTemplateData(),
            model = KmpModuleModel(),
            dataModel = mapOf(),
        )
        // Then
    }

    private fun mockProjectTemplateData() =
        ProjectTemplateData(
            androidXSupport = false,
            agpVersion = AgpVersion.parse("8.5.2"),
            sdkDir = null,
            language = Language.Kotlin,
            kotlinVersion = "2.0.0",
            rootDir = File(""),
            applicationPackage = null,
            includedFormFactorNames = mapOf(),
            debugKeystoreSha1 = null,
            overridePathCheck = null,
            isNewProject = true,
        )
}
