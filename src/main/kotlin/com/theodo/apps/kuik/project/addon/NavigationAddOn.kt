package com.theodo.apps.kuik.project.addon

import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.ide.starters.local.GeneratorAsset
import com.intellij.ide.starters.local.GeneratorTemplateFile
import com.intellij.openapi.project.Project
import com.theodo.apps.kuik.common.utils.TemplateGroup
import com.theodo.apps.kuik.common.utils.toFolders
import com.theodo.apps.kuik.module.KmpModuleRecipe
import com.theodo.apps.kuik.module.model.ModuleType
import com.theodo.apps.kuik.project.createEmptyDirectory

class NavigationAddOn : AddOn() {
    override fun getMainProjectFiles(): List<GeneratorAsset> = emptyList()

    override fun moduleType(): ModuleType = ModuleType.CORE

    override fun moduleName(): String = "navigation"

    override fun shouldAddModuleDependencyToMainApp(): Boolean = true

    override fun generateAddOnModule(project: Project) {
        val moduleTypeDir = getModuleTypeDirectory(project)
        if (moduleTypeDir != null) {
            val moduleDir =
                createEmptyDirectory(
                    moduleTypeDir,
                    model.moduleLowerCase,
                )

            KmpModuleRecipe().executeRecipe(
                project = project,
                model = model,
                moduleDir = moduleDir,
                additionalAssets =
                    listOf(
                        GeneratorTemplateFile(
                            "src/commonMain/kotlin/${model.packageName.toFolders()}/${model.moduleLowerCase}/MainDestination.kt",
                            FileTemplateManager.getDefaultInstance().getCodeTemplate(TemplateGroup.NAVIGATION_DESTINATIONS),
                        ),
                    ),
            )
        }
    }
}
