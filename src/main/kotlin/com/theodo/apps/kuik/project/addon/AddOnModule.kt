package com.theodo.apps.kuik.project.addon

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

object AddOnModule {
    val module =
        module {
            factoryOf(::NavigationAddOn)
            factoryOf(::HomeAddOn)
            factoryOf(::CoreUiAddOn)
        }
}
