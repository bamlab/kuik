package com.theodo.apps.kuik.module

import com.android.tools.idea.wizard.model.SkippableWizardStep
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.guessProjectDir
import com.intellij.openapi.vfs.VirtualFile
import com.theodo.apps.kuik.common.models.KmpModuleModel
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
        model.packageName = panel.getPackageName().substringBeforeLast(".")
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
                val moduleTypeDir =
                    baseDir.findFileByRelativePath(model.moduleType.folderName()) ?: return@runWriteCommandAction
                val moduleDir = createDirectory(moduleTypeDir, model.moduleName)
                KmpModuleRecipe().executeRecipe(project, model, moduleDir)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun createDirectory(
        parent: VirtualFile,
        name: String,
    ): VirtualFile = parent.createChildDirectory(null, name)
}
