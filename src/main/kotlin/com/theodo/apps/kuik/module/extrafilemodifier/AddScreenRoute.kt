package com.theodo.apps.kuik.module.extrafilemodifier

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.guessProjectDir
import com.theodo.apps.kuik.common.models.KmpModuleModel
import com.theodo.apps.kuik.common.utils.toFolders
import kotlinx.html.dom.document
import java.io.IOException

class AddScreenRoute : ExistingFileModifier {
    override fun modify(
        module: KmpModuleModel,
        project: Project,
    ) {
        val fileToModify =
            project.guessProjectDir()?.findFileByRelativePath(
                "core/navigation/src/commonMain/kotlin/${
                    module.packageName
                        .substringBeforeLast(".") // delete core,feature,domain or data
                        .toFolders()
                }/core/navigation/MainDestination.kt",
            )
        if (fileToModify != null) {
            WriteCommandAction.runWriteCommandAction(project) {
                try {
                    val document = FileDocumentManager.getInstance().getDocument(fileToModify)
                    if (document != null) {
                        val insertPoint = document.text.lastIndexOf('}')
                        val newModuleEntry = """
    data object ${module.moduleUpperCamelCase()} : MainDestination("${module.moduleLowerCase}")
"""
                        if (!document.text.contains(newModuleEntry)) {
                            document.insertString(insertPoint, newModuleEntry)
                        }
                        FileDocumentManager.getInstance().saveDocument(document)
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        } else {
            println("Error: MainDestination file not found.")
        }
    }
}
