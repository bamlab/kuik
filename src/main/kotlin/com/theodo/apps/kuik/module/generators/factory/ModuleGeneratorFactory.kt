package com.theodo.apps.kuik.module.generators.factory

import com.theodo.apps.kuik.common.models.KmpModuleModel
import com.theodo.apps.kuik.module.generators.CoreModuleGenerator
import com.theodo.apps.kuik.module.generators.DataModuleGenerator
import com.theodo.apps.kuik.module.generators.DomainModuleGenerator
import com.theodo.apps.kuik.module.generators.FeatureModuleGenerator
import com.theodo.apps.kuik.module.generators.ModuleCommonGenerator
import com.theodo.apps.kuik.module.model.ModuleType

class ModuleGeneratorFactory {
    companion object {
        fun generate(params: KmpModuleModel): ModuleCommonGenerator =
            when (params.moduleType) {
                ModuleType.FEATURE -> FeatureModuleGenerator(params)
                ModuleType.DATA -> DataModuleGenerator(params)
                ModuleType.DOMAIN -> DomainModuleGenerator(params)
                ModuleType.CORE -> CoreModuleGenerator(params)
            }
    }
}
