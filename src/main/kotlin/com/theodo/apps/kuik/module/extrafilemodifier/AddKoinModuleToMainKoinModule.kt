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

        document
            .insertModule(content, params)
            .insertImport(content, params)
        FileDocumentManager.getInstance().saveDocument(document)
    }

    private fun Document.insertImport(
        content: String,
        params: KmpModuleModel,
    ) = this.apply {
        val insertionPoint = content.indexOf("import org.koin.dsl.module")

        if (insertionPoint != 1) {
            val newImportEntry =
                "import ${params.packageName}.${params.moduleLowerCase}.di.${params.moduleName}KoinModule"
            if (!text.contains(newImportEntry)) {
                insertString(insertionPoint, "$newImportEntry\n")
            }
        } else {
            println("Error: Could not find a suitable included koin module block.")
        }
    }

    private fun Document.insertModule(
        content: String,
        params: KmpModuleModel,
    ): Document =
        this.apply {
            val insertionPoint =
                Regex("""\s*listOf\s*\(\s*""")
                    .find(content)
                    ?.range
                    ?.last
                    ?.plus(1)

            if (insertionPoint != null) {
                val newModuleEntry =
                    "${params.moduleName}KoinModule,"
                if (!text.contains(newModuleEntry)) {
                    insertString(insertionPoint, "$newModuleEntry\n")
                }
            } else {
                println("Error: Could not find a suitable included koin module block.")
            }
        }
}
