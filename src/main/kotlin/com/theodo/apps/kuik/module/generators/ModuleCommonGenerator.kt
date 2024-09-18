package com.theodo.apps.kuik.module.generators

import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.ide.starters.local.GeneratorAsset
import com.intellij.ide.starters.local.GeneratorEmptyDirectory
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Document
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.guessProjectDir
import com.theodo.apps.kuik.common.generators.*
import com.theodo.apps.kuik.common.models.KmpModuleModel
import com.theodo.apps.kuik.common.utils.file.findBlock
import com.theodo.apps.kuik.common.utils.file.findDependencyInsertionPoint
import java.io.IOException

abstract class ModuleCommonGenerator(
    private val params: KmpModuleModel,
) {
    open fun generate(
        list: MutableList<GeneratorAsset>,
        ftManager: FileTemplateManager,
        packageName: String,
    ) = list.apply {
        operator fun GeneratorAsset.unaryPlus() = add(this)

        val generatorList: List<PlatformGenerator> =
            listOfNotNull(
                if (params.hasAndroid) AndroidGenerator(params, false) else null,
                if (params.hasIOS) IOSGenerator(params, false) else null,
            )

        // Common
        +GeneratorEmptyDirectory("src/commonMain/kotlin/${packageName.replace(".", "/")}/${params.moduleLowerCase}")

        addAll(generatorList.flatMap { it.commonFiles(ftManager, packageName) })
        addAll(generatorList.flatMap { it.generate(ftManager, packageName) })
    }

    fun addModuleDependencyToMainApp(project: Project) {
        // TODO can find the main app in another module than composeApp
        val buildFile =
            project.guessProjectDir()?.findFileByRelativePath("composeapp")?.findFileByRelativePath("build.gradle.kts")
        if (buildFile != null) {
            WriteCommandAction.runWriteCommandAction(project) {
                try {
                    val document = FileDocumentManager.getInstance().getDocument(buildFile)
                    if (document != null) {
                        reallyWrite(document)
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        } else {
            println("Error: main app build.gradle.kts file not found.")
        }
    }

    private fun reallyWrite(document: Document) {
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

    open fun shouldAddModuleDependencyToMainApp(): Boolean = params.shouldAddModuleDependencyToMainApp
}
