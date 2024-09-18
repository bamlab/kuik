package com.theodo.apps.kuik.module.extrafilemodifier

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Document
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.guessProjectDir
import com.intellij.openapi.vfs.VirtualFile
import com.theodo.apps.kuik.common.models.KmpModuleModel
import java.io.IOException

abstract class MainAppModifier : ExistingFileModifier {
    override fun modify(
        module: KmpModuleModel,
        project: Project,
    ) {
        // TODO make it work for other main name
        val buildFile =
            project.guessProjectDir()?.findFileByRelativePath("composeapp")?.findFileToModify()
        if (buildFile != null) {
            WriteCommandAction.runWriteCommandAction(project) {
                try {
                    val document = FileDocumentManager.getInstance().getDocument(buildFile)
                    if (document != null) {
                        writeInFile(document, module)
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        } else {
            println("Error: main app build.gradle.kts file not found.")
        }
    }

    abstract fun VirtualFile?.findFileToModify(): VirtualFile?

    abstract fun writeInFile(
        document: Document,
        params: KmpModuleModel,
    )
}
