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

// TODO add special build.gradle
// TODO add new type of assets to just copy files
class CoreUiAddOn(
    private val kmpModuleRecipe: KmpModuleRecipe,
    private val ftManager: FileTemplateManager,
) : AddOn() {
    override fun getMainProjectFiles(): List<GeneratorAsset> = emptyList()

    override fun moduleType(): ModuleType = ModuleType.CORE

    override fun moduleName(): String = "ui"

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
            GeneratorTemplateFile(
                "src/commonMain/kotlin/${model.packageName.toFolders()}/${model.moduleLowerCase}/theme/Color.kt",
                ftManager
                    .getCodeTemplate(TemplateGroup.COLOR_CORE_UI),
            ),
            GeneratorTemplateFile(
                "src/commonMain/kotlin/${model.packageName.toFolders()}/${model.moduleLowerCase}/theme/Type.kt",
                ftManager
                    .getCodeTemplate(TemplateGroup.TYPE_CORE_UI),
            ),
            GeneratorTemplateFile(
                "src/commonMain/kotlin/${model.packageName.toFolders()}/${model.moduleLowerCase}/theme/Theme.kt",
                ftManager
                    .getCodeTemplate(TemplateGroup.THEME_CORE_UI),
            ),

            GeneratorTemplateFile(
                "src/commonMain/composeResources/font/rubik_mono_one_regular.ttf",
                ftManager.getCodeTemplate(TemplateGroup.RUBIK_MONO_CORE_UI),
            ),
            GeneratorTemplateFile(
                "src/commonMain/composeResources/font/rubik_variable.ttf",
                ftManager.getCodeTemplate(TemplateGroup.RUBIK_CORE_UI),
            ),
            GeneratorTemplateFile(
                "src/commonMain/composeResources/font/rubik_variable_italic.ttf",
                ftManager.getCodeTemplate(TemplateGroup.RUBIK_ITALIC_CORE_UI),
            ),
        ),
    )
}
