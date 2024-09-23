package com.theodo.apps.kuik.module

import com.intellij.ide.fileTemplates.FileTemplateManager
import com.theodo.apps.kuik.common.models.KmpModuleModel
import com.theodo.apps.kuik.module.extrafilemodifier.AddKoinModuleToMainKoinModule
import com.theodo.apps.kuik.module.extrafilemodifier.AddModuleDepsToMainApp
import com.theodo.apps.kuik.module.extrafilemodifier.AddModuleToSettingsGradle
import com.theodo.apps.kuik.module.extrafilemodifier.AddScreenRoute
import com.theodo.apps.kuik.module.extrafilemodifier.AddScreenToNavHost
import com.theodo.apps.kuik.module.model.ModuleType
import io.mockk.mockk
import io.mockk.mockkClass
import org.junit.Rule
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject
import org.koin.test.mock.MockProviderRule
import org.koin.test.mock.declareMock
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class KmpModuleRecipeTest : KoinTest {
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
                    factoryOf(::KmpModuleRecipe)
                },
            )
        }

    @get:Rule
    val mockProvider = MockProviderRule.create { clazz -> mockkClass(type = clazz, relaxed = true) }

    @Test
    fun `WHEN defineAssets is called THEN correct files are generated`() {
        // Given
        val recipe by inject<KmpModuleRecipe>()

        // When
        val assets =
            recipe.defineAssets(
                project = mockk(relaxed = true),
                model =
                    KmpModuleModel().apply {
                        packageName = "com.theodo.feature"
                        moduleName = "myFeature"
                        moduleLowerCase = "myfeature"
                        moduleType = ModuleType.FEATURE
                    },
                additionalAssets = listOf(),
            )

        // Then
        assertListEquals(
            expected =
                listOf(
                    "src/commonMain/kotlin/com/theodo/feature/myfeature",
                    "src/commonMain/kotlin/com/theodo/feature/myfeature/di/myFeatureKoinModule.kt",
                    "src/iosMain/kotlin/com/theodo/feature/myfeature",
                    "src/androidMain/kotlin/com/theodo/feature/myfeature",
                    "src/commonMain/kotlin/com/theodo/feature/myfeature/MyFeatureScreen.kt",
                    "build.gradle.kts",
                ),
            actual = assets.map { it.relativePath },
        )
    }

    private fun assertListEquals(
        expected: List<String>,
        actual: List<String>,
    ) {
        assertEquals(expected.count(), actual.count())
        expected.forEach {
            assertContains(actual, it, "Expected $it not found in $actual")
        }
        actual.forEach {
            assertContains(expected, it, "Unexpected $it found in $actual")
        }
    }
}
