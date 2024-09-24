package com.theodo.apps.kuik.project

import com.theodo.apps.kuik.module.KmpModuleModule
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

object KmpProjectModule {
    val module =
        module {
            includes(KmpModuleModule.module)
            factoryOf(::KmpWizardTemplate)
        }
}
