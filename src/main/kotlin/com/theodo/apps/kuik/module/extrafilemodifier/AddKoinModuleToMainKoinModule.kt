package com.theodo.apps.kuik.module.extrafilemodifier

import com.intellij.openapi.editor.Document
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.vfs.VirtualFile
import com.theodo.apps.kuik.common.models.KmpModuleModel
import com.theodo.apps.kuik.common.utils.toFolders

class AddKoinModuleToMainKoinModule : MainAppModifier() {
    override fun VirtualFile?.findFileToModify(module: KmpModuleModel): VirtualFile? =
        this?.findFileByRelativePath(
            "src/commonMain/kotlin/${
                module.packageName
                    .substringBeforeLast(".") // delete core,feature,domain or data
                    .toFolders()
            }/composeapp/di/appModule.kt",
        )

    override fun writeInFile(
        document: Document,
        params: KmpModuleModel,
    ) {
        val content = document.text

        insertModule(content, params, document)
        insertImport(content, params, document)
    }

    private fun insertImport(
        content: String,
        params: KmpModuleModel,
        document: Document,
    ) {
        val insertionPoint = content.lastIndexOf("import org.koin.dsl.module") + 1

        if (insertionPoint != 1) {
            val newImportEntry =
                "import ${params.packageName}.${params.moduleLowerCase}.di.${params.moduleName}KoinModule"
            if (!document.text.contains(newImportEntry)) {
                document.insertString(insertionPoint, "$newImportEntry\n")
            }
            FileDocumentManager.getInstance().saveDocument(document)
        } else {
            println("Error: Could not find a suitable included koin module block.")
        }
    }

    private fun insertModule(
        content: String,
        params: KmpModuleModel,
        document: Document,
    ) {
        val insertionPoint =
            Regex("""\s*listOf\s*\(\s*""")
                .find(content)
                ?.range
                ?.last
                ?.plus(1)

        if (insertionPoint != null) {
            val newModuleEntry =
                "${params.moduleName}KoinModule,"
            if (!document.text.contains(newModuleEntry)) {
                document.insertString(insertionPoint, "$newModuleEntry\n")
            }
            FileDocumentManager.getInstance().saveDocument(document)
        } else {
            println("Error: Could not find a suitable included koin module block.")
        }
    }
}
