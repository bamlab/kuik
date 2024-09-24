package com.theodo.apps.kuik.project.addon

import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.ide.starters.local.GeneratorAsset
import com.intellij.ide.starters.local.GeneratorTemplateFile
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.theodo.apps.kuik.common.utils.TemplateGroup
import com.theodo.apps.kuik.common.utils.toFolders
import com.theodo.apps.kuik.module.KmpModuleRecipe
import com.theodo.apps.kuik.module.model.ModuleType
import com.theodo.apps.kuik.project.createEmptyDirectory
import org.jetbrains.annotations.VisibleForTesting

class NavigationAddOn(
    private val kmpModuleRecipe: KmpModuleRecipe,
    private val ftManager: FileTemplateManager,
) : AddOn() {
    override fun getMainProjectFiles(): List<GeneratorAsset> = emptyList()

    override fun moduleType(): ModuleType = ModuleType.CORE

    override fun moduleName(): String = "navigation"

    override fun shouldAddModuleDependencyToMainApp(): Boolean = true

    override fun generateAddOnModule(project: Project): List<GeneratorAsset> {
        val moduleTypeDir = getModuleTypeDirectory(project)
        return if (moduleTypeDir != null) {
            val moduleDir =
                createEmptyDirectory(
                    moduleTypeDir,
                    model.moduleLowerCase,
                )

            generateAssets(project, moduleDir)
        } else {
            emptyList()
        }
    }

    @VisibleForTesting
    fun generateAssets(
        project: Project,
        moduleDir: VirtualFile,
    ) = kmpModuleRecipe.executeRecipe(
        project = project,
        model = model,
        moduleDir = moduleDir,
        additionalAssets =
            listOf(
                GeneratorTemplateFile(
                    "src/commonMain/kotlin/${model.packageName.toFolders()}/${model.moduleLowerCase}/MainDestination.kt",
                    ftManager.getCodeTemplate(TemplateGroup.NAVIGATION_DESTINATIONS),
                ),
            ),
    )
}
