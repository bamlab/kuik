package com.theodo.apps.kuik.project

import com.android.tools.idea.wizard.template.Template
import com.android.tools.idea.wizard.template.WizardTemplateProvider
import com.theodo.apps.kuik.KoinInitializer
import org.koin.java.KoinJavaComponent

class KmpProjectTemplateProvider : WizardTemplateProvider() {
    override fun getTemplates(): List<Template> {
        KoinInitializer.initKoin()
        return listOf(KoinJavaComponent.getKoin().get<KmpWizardTemplate>().projectTemplate)
    }
}
