package com.theodo.apps.kuik.module.model

import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.project.guessProjectDir
import com.intellij.openapi.vfs.VirtualFile
import org.jetbrains.annotations.VisibleForTesting

object ProjectHelper {
    fun getProject(): Project = ProjectManager.getInstance().openProjects.last()

    fun getProjectMainModule(): String? {
        val relPath = "settings.gradle.kts"
        val content = getContent(getProject(), relPath) ?: return null
        return getProjectMainModule(content).also {
            if (it == null) {
                println("Regex not found in $relPath")
            }
        }
    }

    @VisibleForTesting
    fun getProjectMainModule(content: String): String? {
        val matchResult =
            Regex("""include\(":((?!core|data|feature|domain).*)"\)""")
                .findAll(content)
                .firstOrNull()

        return matchResult?.range?.let {
            content
                .substring(it)
                .substringBefore("\")")
                .substringAfter(":")
        }
    }

    @VisibleForTesting
    fun getContent(
        project: Project,
        relPath: String,
    ): String? {
        val file: VirtualFile? = project.guessProjectDir()?.findFileByRelativePath(relPath)

        val content =
            if (file != null) {
                println("Error: $relPath module not found.")
                FileDocumentManager.getInstance().getDocument(file)?.text
            } else {
                null
            }
        if (content == null) {
            println("Error: Could not read $relPath content.")
        }
        return content
    }
}
