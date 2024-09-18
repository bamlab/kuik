package com.theodo.apps.kuik.module.extraasset

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.guessProjectDir
import com.theodo.apps.kuik.common.models.KmpModuleModel
import java.io.IOException

class AddModuleToSettingsGradle : ExistingFileModifier {
    override fun modify(
        module: KmpModuleModel,
        project: Project,
    ) {
        val moduleName = module.moduleName
        val moduleType = module.moduleType
        val settingsFile = project.guessProjectDir()?.findFileByRelativePath("settings.gradle.kts")
        if (settingsFile != null) {
            WriteCommandAction.runWriteCommandAction(project) {
                try {
                    val document = FileDocumentManager.getInstance().getDocument(settingsFile)
                    if (document != null) {
                        val newModuleEntry = "include(\":${moduleType.folderName()}:$moduleName\")"
                        if (!document.text.contains(newModuleEntry)) {
                            document.insertString(document.textLength, "\n$newModuleEntry")
                        }
                        FileDocumentManager.getInstance().saveDocument(document)
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        } else {
            println("Error: settings.gradle.kts file not found.")
        }
    }
}
