package com.theodo.apps.kuik.module

import com.intellij.ide.fileTemplates.FileTemplateManager
import com.theodo.apps.kuik.module.extrafilemodifier.ExtraFileModifierKoinModule
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

object KmpModuleModule {
    val module =
        module {
            includes(ExtraFileModifierKoinModule.module)
            factoryOf(::ModuleAssetGenerator)
            factory { FileTemplateManager.getDefaultInstance() }
            factory { KmpModuleRecipe() }
        }
}
