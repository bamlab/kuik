package com.theodo.apps.kuik.module.extrafilemodifier

import org.koin.core.module.dsl.factoryOf

object ExtraFileModifierKoinModule {
    val module =
        org.koin.dsl.module {
            factoryOf(::AddKoinModuleToMainKoinModule)
            factoryOf(::AddModuleDepsToMainApp)
            factoryOf(::AddModuleToSettingsGradle)
            factoryOf(::AddScreenRoute)
            factoryOf(::AddScreenToNavHost)
        }
}
