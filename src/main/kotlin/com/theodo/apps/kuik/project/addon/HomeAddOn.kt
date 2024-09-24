package com.theodo.apps.kuik.project.addon

import com.intellij.ide.starters.local.GeneratorAsset
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.theodo.apps.kuik.module.KmpModuleRecipe
import com.theodo.apps.kuik.module.model.ModuleType
import com.theodo.apps.kuik.project.createEmptyDirectory
import org.jetbrains.annotations.VisibleForTesting
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class HomeAddOn :
    AddOn(),
    KoinComponent {
    private val kmpModuleRecipe: KmpModuleRecipe by inject()

    override fun getMainProjectFiles(): List<GeneratorAsset> = emptyList()

    override fun moduleType(): ModuleType = ModuleType.FEATURE

    override fun moduleName(): String = "home"

    override fun shouldAddModuleDependencyToMainApp(): Boolean = true

    override fun generateAddOnModule(project: Project): List<GeneratorAsset> {
        val moduleTypeDir = getModuleTypeDirectory(project)
        return if (moduleTypeDir != null) {
            val moduleDir =
                createEmptyDirectory(
                    moduleTypeDir,
                    model.moduleLowerCase,
                )

            generateAssests(project, moduleDir)
        } else {
            emptyList()
        }
    }

    @VisibleForTesting
    fun generateAssests(
        project: Project,
        moduleDir: VirtualFile,
    ) = kmpModuleRecipe.executeRecipe(
        project = project,
        model = model,
        moduleDir = moduleDir,
        additionalAssets =
            listOf(
                // not used for now
                // GeneratorTemplateFile(
                //    "src/commonMain/kotlin/${model.packageName.toFolders()}/${model.moduleLowerCase}/di/homeKoinModule.kt",
                //    FileTemplateManager.getDefaultInstance().getCodeTemplate(TemplateGroup.HOME_DI_MODULE),
                // ),
        ),
    )
}
