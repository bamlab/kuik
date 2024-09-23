package com.theodo.apps.kuik.module

import com.theodo.apps.kuik.module.extrafilemodifier.ExtraFileModifierKoinModule
import org.koin.dsl.module

object KmpModuleModule {
    val module =
        module {
            includes(ExtraFileModifierKoinModule.module)
            factory { KmpModuleRecipe() }
        }
}
