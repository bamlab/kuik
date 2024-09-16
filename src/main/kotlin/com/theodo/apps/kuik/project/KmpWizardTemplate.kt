package com.theodo.apps.kuik.project

import com.android.tools.idea.wizard.template.*
import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.ide.starters.local.GeneratorAsset
import com.intellij.ide.starters.local.GeneratorEmptyDirectory
import com.intellij.ide.starters.local.GeneratorTemplateFile
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.theodo.apps.kuik.common.generators.CommonGenerator
import com.theodo.apps.kuik.common.models.*
import com.theodo.apps.kuik.common.utils.TemplateGroup.PACKAGE_NAME
import com.theodo.apps.kuik.common.utils.Utils
import org.jetbrains.kotlin.idea.core.util.toVirtualFile
import java.net.URL

class KmpWizardTemplate {

    val projectTemplate
        get() = template {
            name = "Theodo Kotlin Multiplatform Project"
            description =
                "Creates a new Kotlin Multiplatform project that include iOS and Android with theodo standards"
            minApi = 26
            category = Category.Application
            formFactor = FormFactor.Generic
            screens = listOf(
                WizardUiContext.NewProject,
            )

            val includeAndroid = booleanParameter {
                name = "Include Android"
                default = true
            }

            val includeIOS = booleanParameter {
                name = "Include IOS"
                default = true
            }

            val includeDesktop = booleanParameter {
                name = "Include Desktop"
                default = false
            }

            val includeWeb = booleanParameter {
                name = "Include Web"
                default = false
            }

            val includeServer = booleanParameter {
                name = "Include Server"
                default = false
            }

            val useKoin = booleanParameter {
                name = "Use Koin for Dependency Injection"
                default = false
            }

            val useKtor = booleanParameter {
                name = "Use Ktor for HTTP client app"
                default = false
            }


            thumb =
                { Thumb { URL("https://raw.githubusercontent.com/bamlab/kuik/master/images/project-logo.png") } }

            recipe = { data: TemplateData ->
                projectRecipe(
                    moduleData = data as ModuleTemplateData,
                    includeAndroid = includeAndroid.value,
                    includeIOS = includeIOS.value,
                    includeDesktop = includeDesktop.value,
                    includeWeb = includeWeb.value,
                    includeServer = includeServer.value,
                )
            }
        }


    private fun projectRecipe(
        moduleData: ModuleTemplateData,
        includeAndroid: Boolean,
        includeIOS: Boolean,
        includeDesktop: Boolean,
        includeWeb: Boolean,
        includeServer: Boolean,
    ) {
        val (projectData, _, _) = moduleData

        val model = KmpModuleModel()
        val packageName = moduleData.packageName

        model.hasAndroid = includeAndroid
        model.hasWeb = includeWeb
        model.hasIOS = includeIOS
        model.hasDesktop = includeDesktop
        model.hasServer = includeServer

        val dataModel = mapOf(
            "APP_NAME" to moduleData.themesData.appName,
            "APP_NAME_LOWERCASE" to moduleData.themesData.appName.lowercase(),
            "PACKAGE_NAME" to packageName,
            "SHARED_NAME" to model.moduleLowerCase,
            "COMPOSE_NAME" to model.composeName,
            "COMPOSE_NAME_LOWERCASE" to model.composeNameLowerCase(),
            "SERVER_NAME" to model.serverName,
            "MODULE_NAME" to moduleData.name,
            "SERVER_GREETING" to "\${Greeting().greet()}",
            "SHARED_GREETING" to "\${platform.name}",
            "BUNDLE_ID" to "\${BUNDLE_ID}",
            "TEAM_ID" to "\${TEAM_ID}",
            "PROJECT_DIR" to "\${PROJECT_DIR}",
            "USER_HOME" to "\${USER_HOME}",
            "ROOT_NODE" to "\${RootNode}",
            "PROJECT" to moduleData.themesData.appName,
            "BUILD_VERSION_SDK_INT" to "\${Build.VERSION.SDK_INT}",
            "JVM_JAVA_VERSION" to "\${System.getProperty(\"java.version\")}",
            model.hasAndroid(),
            model.hasDesktop(),
            model.hasIOS(),
            model.hasWeb(),
            model.hasServer(),
        )

        val virtualFile = projectData.rootDir.toVirtualFile()

        virtualFile?.let { file ->
            val ftManager = FileTemplateManager.getDefaultInstance()
            val generatorAssets = mutableListOf<GeneratorAsset>()
            val commonGeneratorList = CommonGenerator(model).generate(
                generatorAssets, ftManager, packageName
            )
            generatorAssets.addAll(commonGeneratorList)
            generatorAssets.forEach { asset ->
                when (asset) {
                    is GeneratorEmptyDirectory -> createEmptyDirectory(file, asset.relativePath)
                    is GeneratorTemplateFile -> Utils.generateFileFromTemplate(
                        templateName = "${asset.template.name}.${asset.template.extension}",
                        dataModel = dataModel,
                        outputDir = file,
                        outputFilePath = asset.relativePath
                    )

                    else -> println("Generator Asset: Nothing")
                }
            }
        }
    }

    private fun createEmptyDirectory(parent: VirtualFile, path: String) {
        VfsUtil.createDirectoryIfMissing(parent, path)
    }
}
