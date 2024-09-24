package com.theodo.apps.kuik.project

import com.theodo.apps.kuik.module.KmpModuleModule
import com.theodo.apps.kuik.project.addon.AddOnModule
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

object KmpProjectModule {
    val module =
        module {
            includes(KmpModuleModule.module, AddOnModule.module)
            factoryOf(::ProjectAssetGenerator)
            factoryOf(::KmpWizardTemplate)
        }
}
