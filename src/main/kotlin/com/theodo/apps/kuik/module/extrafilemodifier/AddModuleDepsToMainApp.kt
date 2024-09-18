package com.theodo.apps.kuik.module.extrafilemodifier

import com.intellij.openapi.editor.Document
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.vfs.VirtualFile
import com.theodo.apps.kuik.common.models.KmpModuleModel
import com.theodo.apps.kuik.common.utils.file.findBlock
import com.theodo.apps.kuik.common.utils.file.findDependencyInsertionPoint

class AddModuleDepsToMainApp : MainAppModifier() {
    override fun VirtualFile?.findFileToModify(module: KmpModuleModel): VirtualFile? = this?.findFileByRelativePath("build.gradle.kts")

    override fun writeInFile(
        document: Document,
        params: KmpModuleModel,
    ) {
        val content = document.text

        // Find the full kotlin { } block
        val kotlinBlockMatch = findBlock(Regex("""kotlin\s*\{\s*"""), content)
        if (kotlinBlockMatch != null) {
            val kotlinBlockContent = content.substring(kotlinBlockMatch.startIndex, kotlinBlockMatch.endIndex + 1)

            // Find the position to insert the dependency within the kotlin block
            val insertionPoint = findDependencyInsertionPoint(kotlinBlockContent)

            if (insertionPoint != null) {
                // Adjust the insertion point relative to the whole content
                val adjustedInsertionPoint: Int = kotlinBlockMatch.startIndex + insertionPoint

                // Insert the dependency line at the found position
                val newModuleEntry =
                    "implementation(project(\":${params.moduleType.folderName()}:${params.moduleName}\"))"
                if (!document.text.contains(newModuleEntry)) {
                    document.insertString(adjustedInsertionPoint, "$newModuleEntry\n")
                }
                FileDocumentManager.getInstance().saveDocument(document)
            } else {
                println("Error: Could not find a suitable 'commonMain.dependencies { }' block.")
            }
        } else {
            println("Error: Could not find the 'kotlin { }' block.")
        }
    }
}
