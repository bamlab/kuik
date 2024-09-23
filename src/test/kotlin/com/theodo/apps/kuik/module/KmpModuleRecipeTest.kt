package com.theodo.apps.kuik.module

import com.intellij.ide.fileTemplates.FileTemplateManager
import com.theodo.apps.kuik.common.models.KmpModuleModel
import io.mockk.mockk
import org.koin.test.KoinTest
import kotlin.test.Test

class KmpModuleRecipeTest : KoinTest {
    @Test
    fun `toto`() {
        // Given
        val ftManager: FileTemplateManager = mockk(relaxed = true)
        val recipe = KmpModuleRecipe()

        // When
        recipe.executeRecipe(
            project = mockk(relaxed = true),
            model = KmpModuleModel(),
            moduleDir = mockk(relaxed = true),
            additionalAssets = listOf(),
        )
        // Then
    }
}
