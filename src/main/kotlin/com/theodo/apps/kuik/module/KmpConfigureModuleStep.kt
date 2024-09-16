package com.theodo.apps.kuik.module

import com.android.tools.idea.wizard.model.SkippableWizardStep
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Document
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.guessProjectDir
import com.intellij.openapi.vfs.VirtualFile
import com.theodo.apps.kuik.common.models.KmpModuleModel
import com.theodo.apps.kuik.common.utils.file.findBlock
import com.theodo.apps.kuik.common.utils.file.findDependencyInsertionPoint
import com.theodo.apps.kuik.module.model.ModuleType
import java.io.IOException
import javax.swing.JComponent

class KmpConfigureModuleStep(
    private val project: Project,
    private val model: KmpModuleModel,
) : SkippableWizardStep<KmpModuleModel>(model, "KMM Module Configuration") {
    private lateinit var panel: KmpModuleConfigurationPanel

    override fun getComponent(): JComponent {
        if (!this::panel.isInitialized) {
            panel = KmpModuleConfigurationPanel()
        }
        return panel
    }

    override fun onProceeding() {
        super.onProceeding()
        model.moduleName = panel.getModuleName()
        model.packageName = panel.getPackageName().substringBeforeLast(".") + ".${model.moduleType.name.lowercase()}"
        model.moduleLowerCase = panel.getPackageName().substringAfterLast(".")
        model.hasAndroid = panel.isIncludeAndroid()
        model.hasIOS = panel.isIncludeIos()
        model.hasWeb = panel.isIncludeWeb()
        model.hasDesktop = panel.isIncludeDesktop()
        model.hasServer = panel.isIncludeServer()
        model.moduleType = panel.getModuleType()
    }

    override fun onWizardFinished() {
        super.onWizardFinished()
        createKmmModule(project, model)
    }

    private fun createKmmModule(
        project: Project,
        model: KmpModuleModel,
    ) {
        WriteCommandAction.runWriteCommandAction(project) {
            try {
                val baseDir = project.guessProjectDir() ?: return@runWriteCommandAction
                val moduleDir = createDirectory(baseDir, model.moduleName)
                KmpModuleRecipe().executeRecipe(project, model, moduleDir)
                addModuleToSettingsGradle(project, model.moduleName)
                if (model.moduleType == ModuleType.FEATURE) {
                    addModuleDependencyToMainApp(project, model.moduleName)
                }
                addModuleDependencyToMainApp(project, model.moduleName)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun addModuleDependencyToMainApp(
        project: Project,
        moduleName: String,
    ) {
        // TODO can find the main app in another module than composeApp
        val buildFile =
            project.guessProjectDir()?.findFileByRelativePath("composeApp")?.findFileByRelativePath("build.gradle.kts")
        if (buildFile != null) {
            WriteCommandAction.runWriteCommandAction(project) {
                try {
                    val document = FileDocumentManager.getInstance().getDocument(buildFile)
                    if (document != null) {
                        reallyWrite(document, moduleName)
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        } else {
            println("Error: main app build.gradle.kts file not found.")
        }
    }

    private fun addModuleToSettingsGradle(
        project: Project,
        moduleName: String,
    ) {
        val settingsFile = project.guessProjectDir()?.findFileByRelativePath("settings.gradle.kts")
        if (settingsFile != null) {
            WriteCommandAction.runWriteCommandAction(project) {
                try {
                    val document = FileDocumentManager.getInstance().getDocument(settingsFile)
                    if (document != null) {
                        val newModuleEntry = "include(\":$moduleName\")"
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

    private fun reallyWrite(
        document: Document,
        moduleName: String,
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
                val newModuleEntry = "implementation(project(\":$moduleName\"))"
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

    private fun createDirectory(
        parent: VirtualFile,
        name: String,
    ): VirtualFile = parent.createChildDirectory(null, name)
}
