@file:Suppress("UnstableApiUsage")

package io.github.estivensh4.kotlinmultiplatformwizard

import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.ide.projectWizard.ProjectSettingsStep
import com.intellij.ide.starters.local.*
import com.intellij.ide.starters.local.wizard.StarterInitialStep
import com.intellij.ide.starters.shared.KOTLIN_STARTER_LANGUAGE
import com.intellij.ide.starters.shared.StarterLanguage
import com.intellij.ide.starters.shared.StarterProjectType
import com.intellij.ide.util.projectWizard.ModuleWizardStep
import com.intellij.ide.util.projectWizard.WizardContext
import com.intellij.openapi.module.ModuleType
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.roots.ui.configuration.ModulesProvider
import com.intellij.openapi.util.io.FileUtil
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.pom.java.LanguageLevel
import com.intellij.psi.codeStyle.CodeStyleManager
import com.intellij.util.lang.JavaVersion
import io.github.estivensh4.kotlinmultiplatformwizard.generators.*
import io.github.estivensh4.kotlinmultiplatformwizard.generators.COMPOSE_NAME
import io.github.estivensh4.kotlinmultiplatformwizard.generators.PACKAGE_NAME
import io.github.estivensh4.kotlinmultiplatformwizard.generators.SERVER_NAME
import io.github.estivensh4.kotlinmultiplatformwizard.generators.SHARED_NAME
import io.github.estivensh4.kotlinmultiplatformwizard.steps.ComposeStarterStep
import io.github.estivensh4.kotlinmultiplatformwizard.steps.PlatformOptionsStep
import io.github.estivensh4.kotlinmultiplatformwizard.utils.NetworkVersions
import org.jetbrains.kotlin.idea.KotlinIcons
import org.jetbrains.kotlin.idea.core.util.toPsiFile
import java.io.File
import javax.swing.Icon

/**
 * Following https://github.com/JetBrains/intellij-community/blob/master/plugins/kotlin/project-wizard/compose/src/org/jetbrains/kotlin/tools/composeProjectWizard/ComposeModuleBuilder.kt
 */
class BuilderWizardBuilder : StarterModuleBuilder() {
    val params = BuilderParams()
    private val network = NetworkVersions()

    override fun getModuleType(): ModuleType<*> = BuilderModuleType()
    override fun getNodeIcon(): Icon = KotlinIcons.Wizard.MULTIPLATFORM

    override fun getPresentableName(): String = "Kotlin Multiplatform Wizard"

    override fun getProjectTypes(): List<StarterProjectType> = emptyList()

    override fun getMinJavaVersion(): JavaVersion = LanguageLevel.JDK_11.toJavaVersion()

    override fun getStarterPack(): StarterPack = StarterPack(
        "compose",
        listOf(
            Starter("compose", "Compose", getDependencyConfig("/starters/compose.pom"), emptyList())
        )
    )

    private fun formatCode(project: Project, root: VirtualFile) {
        val csm = CodeStyleManager.getInstance(project)
        File(root.path)
            .walk()
            .filter { it.extension == "kt" || it.extension == "kts" }
            .forEach { it.toPsiFile(project)?.let(csm::scheduleReformatWhenSettingsComputed) }
    }

    private fun createAndGetRoot(): VirtualFile? {
        val path = contentEntryPath?.let { FileUtil.toSystemIndependentName(it) } ?: return null
        return LocalFileSystem.getInstance().refreshAndFindFileByPath(File(path).apply { mkdirs() }.absolutePath)
    }

    override fun createOptionsStep(contextProvider: StarterContextProvider): StarterInitialStep {
        return ComposeStarterStep(this, params, contextProvider)
    }

    override fun createWizardSteps(
        context: WizardContext,
        modulesProvider: ModulesProvider,
    ): Array<ModuleWizardStep> {
        return arrayOf(
            //PlatformOptionsStep(this),
        )
    }

    override fun getAssets(starter: Starter): List<GeneratorAsset> {
        val ftManager = FileTemplateManager.getInstance(ProjectManager.getInstance().defaultProject)
        val standardAssetsProvider = StandardAssetsProvider()

        return mutableListOf<GeneratorAsset>().apply {
            val packageName = starterContext.group.split(".").joinToString("/")

            operator fun GeneratorAsset.unaryPlus() = add(this)

            addAll(standardAssetsProvider.getGradlewAssets())

            if (starterContext.isCreatingNewProject) {
                addAll(standardAssetsProvider.getGradleIgnoreAssets())
            }

            CommonGenerator(params, starterContext).generate(this, ftManager, packageName)
        }
    }

    override fun getTemplateProperties(): Map<String, Any> {
       // val versions = runBlocking { network.getVersions(params.remoteVersions) }
        val sanitizedPackageName = sanitizePackage(starterContext.artifact)
        return mapOf(
            PACKAGE_NAME to starterContext.group,
            SHARED_NAME to params.sharedName,
            COMPOSE_NAME to params.composeName,
            SERVER_NAME to params.serverName,
            "APP_NAME" to starterContext.artifact,
            params.hasAndroid(),
            params.hasDesktop(),
            params.hasIOS(),
            params.hasWeb(),
            params.hasServer(),
            "USE_MATERIAL3" to params.compose.useMaterial3,
            /*"COMPOSE" to versions.composeVersion,
            "KOTLIN" to versions.kotlinVersion,
            "AGP" to versions.agpVersion,
            "KTOR" to versions.ktor,
            "KOIN" to versions.koin,
            "androidxAppCompat" to versions.androidxAppCompat,
            "androidxCore" to versions.androidxCore,*/
            "LAST_PACKAGE_NAME" to sanitizedPackageName,
            "MINSDK" to params.android.minimumSdk,
            "USE_KTOR" to params.library.useKtor,
            "USE_KOIN" to params.library.useKoin,
        )
    }

    override fun getBuilderId(): String = BuilderModuleType.ID

    override fun getDescription(): String = """
        A project wizard to create a compose multiplatform application in your choice of android, ios, web,
        and/or desktop!
    """.trimIndent()

    override fun getIgnoredSteps(): MutableList<Class<out ModuleWizardStep>> {
        return mutableListOf(ProjectSettingsStep::class.java)
    }

    override fun getLanguages(): List<StarterLanguage> = listOf(KOTLIN_STARTER_LANGUAGE)

    override fun setupModule(module: com.intellij.openapi.module.Module) {
        starterContext.starter = starterContext.starterPack.starters.first()
        starterContext.starterDependencyConfig = loadDependencyConfig()[starterContext.starter?.id]
        super.setupModule(module)
    }
}

private fun BuilderParams.hasAndroid() = "HAS_ANDROID" to hasAndroid
private fun BuilderParams.hasIOS() = "HAS_IOS" to hasiOS
private fun BuilderParams.hasWeb() = "HAS_WEB" to hasWeb
private fun BuilderParams.hasDesktop() = "HAS_DESKTOP" to hasDesktop
private fun BuilderParams.hasServer() = "HAS_SERVER" to hasServer