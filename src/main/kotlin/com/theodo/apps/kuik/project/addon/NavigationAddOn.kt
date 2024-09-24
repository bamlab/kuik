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
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class NavigationAddOn :
    AddOn(),
    KoinComponent {
    private val kmpModuleRecipe: KmpModuleRecipe by inject()

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

            kmpModuleRecipe.executeRecipe(
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
        } else {
            emptyList()
        }
    }
}
