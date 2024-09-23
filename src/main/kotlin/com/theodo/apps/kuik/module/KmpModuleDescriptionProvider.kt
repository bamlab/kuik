package com.theodo.apps.kuik.module

import com.android.tools.idea.npw.model.ProjectSyncInvoker
import com.android.tools.idea.npw.module.ModuleDescriptionProvider
import com.android.tools.idea.npw.module.ModuleGalleryEntry
import com.android.tools.idea.wizard.model.SkippableWizardStep
import com.google.common.collect.ImmutableList
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.IconLoader
import com.theodo.apps.kuik.common.models.KmpModuleModel
import javax.swing.Icon

class KmpModuleDescriptionProvider : ModuleDescriptionProvider {
    override fun getDescriptions(project: Project): MutableCollection<out ModuleGalleryEntry> = ImmutableList.of(FeatureModuleEntry())

    class FeatureModuleEntry : ModuleGalleryEntry {
        override val description: String
            get() = "Generates a Kotlin Multiplatform module with Theodo standards (android, iOS and common)"
        override val icon: Icon
            get() = IconLoader.getIcon("/icons/theodokotlin.svg", KmpModuleModel::class.java)
        override val name: String
            get() = "Theodo KMP Module"

        override fun createStep(
            project: Project,
            moduleParent: String,
            projectSyncInvoker: ProjectSyncInvoker,
        ): SkippableWizardStep<*> {
            val kmpModuleModel = KmpModuleModel()
            return KmpConfigureModuleStep(project, kmpModuleModel)
        }
    }
}
