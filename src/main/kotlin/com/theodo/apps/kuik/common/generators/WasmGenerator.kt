package com.theodo.apps.kuik.common.generators

import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.ide.starters.local.GeneratorAsset
import com.intellij.ide.starters.local.GeneratorTemplateFile
import com.theodo.apps.kuik.common.models.KmpModuleModel
import com.theodo.apps.kuik.common.utils.TemplateGroup
import com.theodo.apps.kuik.common.utils.toFolders

class WasmGenerator(params: KmpModuleModel, private val isProject: Boolean) : PlatformGenerator(params) {
    override fun generateProject(ftManager: FileTemplateManager, packageName: String): List<GeneratorAsset> {
        return if (isProject) {
            listOf(
                GeneratorTemplateFile(
                    "${params.composeName}/src/wasmJsMain/resources/styles.css",
                    ftManager.getCodeTemplate(TemplateGroup.WASMJS_STYLES_CSS)
                ),
                GeneratorTemplateFile(
                    "${params.composeName}/src/wasmJsMain/resources/index.html",
                    ftManager.getCodeTemplate(TemplateGroup.WASMJS_INDEX_HTML)
                ),
                GeneratorTemplateFile(
                    "${params.composeName}/src/wasmJsMain/kotlin${packageName.toFolders()}/${params.composeName}/main.kt",
                    ftManager.getCodeTemplate(TemplateGroup.COMPOSE_WASM_JS_MAIN)
                )
            )
        } else emptyList()
    }

    override fun addToCommon(ftManager: FileTemplateManager, packageName: String): List<GeneratorAsset> {
        val relativePath = if (isProject) {
            "${params.moduleLowerCase}/src/wasmJsMain/kotlin${packageName.toFolders()}/${params.moduleLowerCase}/Platform.wasmJs.kt"
        } else "src/wasmJsMain/kotlin${packageName.toFolders()}/${params.moduleName}/Platform.wasmJs.kt"
        return listOf(
            GeneratorTemplateFile(
                relativePath,
                ftManager.getCodeTemplate(TemplateGroup.WASM_JS_MAIN)
            )
        )
    }
}
