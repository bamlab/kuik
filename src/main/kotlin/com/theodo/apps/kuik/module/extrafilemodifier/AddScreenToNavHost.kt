package com.theodo.apps.kuik.module.extrafilemodifier

import com.intellij.openapi.editor.Document
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.vfs.VirtualFile
import com.theodo.apps.kuik.common.models.KmpModuleModel
import com.theodo.apps.kuik.common.utils.toFolders

class AddScreenToNavHost : MainAppModifier() {
    override fun VirtualFile?.findFileToModify(module: KmpModuleModel): VirtualFile? =
        this?.findFileByRelativePath(
            "src/commonMain/kotlin/${
                module.packageName
                    .substringBeforeLast(".") // delete core,feature,domain or data
                    .toFolders()
            }/composeapp/App.kt",
        )

    override fun writeInFile(
        document: Document,
        params: KmpModuleModel,
    ) {
        val content = document.text

        document
            .insertComposable(content, params)
            .insertImport(content, params)
        FileDocumentManager.getInstance().saveDocument(document)
    }

    private fun Document.insertImport(
        content: String,
        params: KmpModuleModel,
    ) = this.apply {
        val insertionPoint = content.indexOf("import org.koin.compose.KoinApplication")

        if (insertionPoint != 0) {
            val newImportEntry =
                "import ${params.packageName}.${params.moduleLowerCase}.${params.moduleName}Screen"
            if (!text.contains(newImportEntry)) {
                insertString(insertionPoint, "$newImportEntry\n")
            }
        } else {
            println("Error: Could not find a suitable insert point for ${this::class.simpleName}")
        }
    }

    private fun Document.insertComposable(
        content: String,
        params: KmpModuleModel,
    ): Document =
        this.apply {
            val insertionPoint =
                Regex("""(\}\s*){4}""")
                    .find(content)
                    ?.range
                    ?.first

            if (insertionPoint != null) {
                val newModuleEntry = """
                    composable("MainDestination.${params.moduleName}.route") {
                        ${params.moduleName}Screen()
                    }
                    """
                if (!text.contains(newModuleEntry)) {
                    insertString(insertionPoint, "$newModuleEntry\n")
                }
            } else {
                println("Error: Could not find a suitable insert point for ${this::class.simpleName}")
            }
        }
}
