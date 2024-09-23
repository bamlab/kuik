package com.theodo.apps.kuik.module

import org.koin.dsl.module

object KmpModuleModule {
    val module =
        module {
            factory { KmpModuleRecipe() }
        }
}
