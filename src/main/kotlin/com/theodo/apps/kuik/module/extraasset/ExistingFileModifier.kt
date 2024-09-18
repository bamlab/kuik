package com.theodo.apps.kuik.module.extraasset

import com.intellij.openapi.project.Project
import com.theodo.apps.kuik.common.models.KmpModuleModel

interface ExistingFileModifier {
    fun modify(
        module: KmpModuleModel,
        project: Project,
    )
}
