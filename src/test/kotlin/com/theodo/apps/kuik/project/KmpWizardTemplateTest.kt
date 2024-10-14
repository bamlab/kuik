package com.theodo.apps.kuik.project

import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.openapi.project.Project
import com.theodo.apps.kuik.assertListEquals
import com.theodo.apps.kuik.common.models.KmpModuleModel
import com.theodo.apps.kuik.module.KmpModuleRecipe
import com.theodo.apps.kuik.module.ModuleAssetGenerator
import com.theodo.apps.kuik.module.extrafilemodifier.AddKoinModuleToMainKoinModule
import com.theodo.apps.kuik.module.extrafilemodifier.AddModuleDepsToMainApp
import com.theodo.apps.kuik.module.extrafilemodifier.AddModuleToSettingsGradle
import com.theodo.apps.kuik.module.extrafilemodifier.AddScreenRoute
import com.theodo.apps.kuik.module.extrafilemodifier.AddScreenToNavHost
import com.theodo.apps.kuik.module.model.ProjectHelper
import com.theodo.apps.kuik.project.addon.CoreUiAddOn
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
                    declareMock<ModuleAssetGenerator>()
                    declareMock<ProjectAssetGenerator>()
                    factoryOf(::NavigationAddOn)
                    factoryOf(::HomeAddOn)
                    factoryOf(::CoreUiAddOn)
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
        val model =
            KmpModuleModel().apply {
                packageName = "com.theodo.kuik"
                composeName = "mainApp"
            }

        // When
        val baseAssets =
            template.defineAssets(
                model = model,
            )
        // val addOnAssests = template.generateAddOnAssets(mockk(relaxed = true)) // Not ready yet

        // Then
        assertListEquals(
            expected =
                listOf(
                    "feature",
                    "core",
                    "domain",
                    "data",
                    "build.gradle.kts",
                    ".editorconfig",
                    "settings.gradle.kts",
                    "gradle.properties",
                    "gradle/wrapper/gradle-wrapper.properties",
                    "gradle/libs.versions.toml",
                    "mainapp/src/commonMain/kotlin/com/theodo/kuik/mainapp/App.kt",
                    "mainapp/src/commonMain/composeResources/drawable/compose-multiplatform.xml",
                    "mainapp/build.gradle.kts",
                    "mainapp/src/commonMain/kotlin/com/theodo/kuik/mainapp/di/appModule.kt",
                    "mainapp/src/commonMain/kotlin/com/theodo/kuik/mainapp/di/initKoin.kt",
                    "mainapp/src/commonMain/kotlin/com/theodo/kuik/mainapp/logging/initLogger.kt",
                    "mainapp/src/androidMain/kotlin/com/theodo/kuik/mainapp/MainActivity.kt",
                    "mainapp/src/androidMain/AndroidManifest.xml",
                    "mainapp/src/androidMain/res/values/strings.xml",
                    "mainapp/src/iosMain/kotlin/com/theodo/kuik/mainapp/MainViewController.kt",
                    "iosApp/iosApp.xcodeproj/project.xcworkspace/xcshareddata/swiftpm/configuration",
                    "iosApp/iosApp/ContentView.swift",
                    "iosApp/iosApp/Assets.xcassets/AppIcon.appiconset/Contents.json",
                    "iosApp/iosApp/Assets.xcassets/AccentColor.colorset/Contents.json",
                    "iosApp/iosApp/Assets.xcassets/Contents.json",
                    "iosApp/iosApp/Preview Content/Preview Assets.xcassets/Contents.json",
                    "iosApp/iosApp/iOSApp.swift",
                    "iosApp/Configuration/Config.xcconfig",
                    "iosApp/iosApp.xcodeproj/project.pbxproj",
                    "iosApp/iosApp/Info.plist",
                    "build-logic/gradle.properties",
                    "build-logic/settings.gradle.kts",
                    "build-logic/convention/build.gradle.kts",
                    "build-logic/convention/src/main/kotlin/Libs.kt",
                    "build-logic/convention/src/main/kotlin/KotlinMultiplatformFeatureModule.kt",
                    "build-logic/convention/src/main/kotlin/KotlinMultiplatformDataModule.kt",
                    "build-logic/convention/src/main/kotlin/KotlinMultiplatformCoreModule.kt",
                    "build-logic/convention/src/main/kotlin/KotlinMultiplatformDomainModule.kt",
                    "build-logic/convention/src/main/kotlin/KotlinMultiplatformModule.kt",
                    "build-logic/convention/src/main/kotlin/KmpModule.kt",
                    "build-logic/convention/src/main/kotlin/AndroidSdkVersion.kt",
                    "build-logic/convention/src/main/kotlin/KmpTargets.kt",
                ),
            actual = baseAssets.map { it.relativePath },
        )
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
