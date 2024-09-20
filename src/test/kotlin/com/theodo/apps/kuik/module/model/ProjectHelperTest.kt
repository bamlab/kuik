package com.theodo.apps.kuik.module.model

import com.intellij.openapi.project.Project
import com.theodo.apps.kuik.module.model.mockContent.MainModuleBuild
import com.theodo.apps.kuik.module.model.mockContent.SettingsGradle1
import com.theodo.apps.kuik.module.model.mockContent.SettingsGradle2
import io.mockk.Ordering
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.verify
import org.junit.Test
import kotlin.test.assertEquals

class ProjectHelperTest {
    @Test
    fun `IF appId is defined between apostrophe WHEN getPackage is called THEN it returns app Id`() {
        // Given
        val content =
            """
            applicationId = 'com.theodo.myapplication'
            """

        // When
        val result = ProjectHelper.getPackage(content)

        // Then
        assertEquals(
            expected = "com.theodo.myapplication",
            actual = result,
        )
    }

    @Test
    fun `IF appId is defined between double quotes WHEN getPackage is called THEN it returns app Id`() {
        // Given
        val content =
            """
            applicationId = "com.theodo.myapplication"
            """

        // When
        val result = ProjectHelper.getPackage(content)

        // Then
        assertEquals(
            expected = "com.theodo.myapplication",
            actual = result,
        )
    }

    @Test
    fun `IF appId is not defined WHEN getPackage is called THEN it returns null`() {
        // Given
        val content = ""
        // When
        val result = ProjectHelper.getPackage(content)
        // Then
        assertEquals(
            expected = null,
            actual = result,
        )
    }

    @Test
    fun `WHEN getPackage is called THEN it uses the correct parameters`() {
        // Given
        val mockedProject = mockk<Project>()
        val mainModule = "composeApp"
        mockkObject(ProjectHelper)
        every { ProjectHelper.getProject() } returns mockedProject
        every { ProjectHelper.getContent(any(), any()) } returns ""
        every { ProjectHelper.getProjectMainModule() } returns mainModule
        every { ProjectHelper.getPackage() } answers { callOriginal() }
        // When
        ProjectHelper.getPackage()
        // Then
        verify(Ordering.ORDERED) {
            ProjectHelper.getContent(mockedProject, "$mainModule/build.gradle.kts")
            ProjectHelper.getPackage(any())
        }
    }

    @Test
    fun `IF there is real conditions WHEN getPackage is called THEN it returns the correct value`() {
        // Given
        val content = MainModuleBuild
        // When
        val result = ProjectHelper.getPackage(content)
        // Then
        assertEquals(
            expected = "com.theodo.myapplication",
            actual = result,
        )
    }

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
