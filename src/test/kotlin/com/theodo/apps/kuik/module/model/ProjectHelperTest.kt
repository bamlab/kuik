package com.theodo.apps.kuik.module.model

import com.intellij.openapi.project.Project
import com.theodo.apps.kuik.module.model.mockContent.SettingsGradle1
import com.theodo.apps.kuik.module.model.mockContent.SettingsGradle2
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.verify
import org.junit.Test
import kotlin.test.assertEquals

class ProjectHelperTest {
    @Test
    fun `IF include main app is the first WHEN get getProjectMainModule is called THEN it returns the main module`() {
        // Given
        val content = SettingsGradle1
        // When
        val result = ProjectHelper.getProjectMainModule(content)

        // Then
        assertEquals(
            expected = "composeApp",
            actual = result,
        )
    }

    @Test
    fun `IF include main app is the last WHEN get getProjectMainModule is called THEN it returns the main module`() {
        // Given
        val content = SettingsGradle2
        // When
        val result = ProjectHelper.getProjectMainModule(content)

        // Then
        assertEquals(
            expected = "composeApp",
            actual = result,
        )
    }

    @Test
    fun `IF include main app is not present WHEN get getProjectMainModule is called THEN it returns null`() {
        // Given
        val content = ""
        // When
        val result = ProjectHelper.getProjectMainModule(content)
        // Then
        assertEquals(
            expected = null,
            actual = result,
        )
    }

    @Test
    fun `WHEN getProjectMainModule is called THEN it uses the correct parameters`() {
        // Given
        val mockedProject = mockk<Project>()
        mockkObject(ProjectHelper)
        every { ProjectHelper.getProject() } returns mockedProject
        every { ProjectHelper.getContent(any(), any()) } returns ""
        every { ProjectHelper.getProjectMainModule() } answers { callOriginal() }

        // When
        ProjectHelper.getProjectMainModule()

        // Then
        verify {
            ProjectHelper.getContent(mockedProject, "settings.gradle.kts")
            ProjectHelper.getProjectMainModule(any())
        }
    }
}
