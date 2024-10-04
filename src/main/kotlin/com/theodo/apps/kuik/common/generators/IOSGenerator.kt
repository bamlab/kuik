package com.theodo.apps.kuik.common.generators

import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.ide.starters.local.GeneratorAsset
import com.intellij.ide.starters.local.GeneratorEmptyDirectory
import com.intellij.ide.starters.local.GeneratorTemplateFile
import com.theodo.apps.kuik.common.models.KmpModuleModel
import com.theodo.apps.kuik.common.utils.TemplateGroup
import com.theodo.apps.kuik.common.utils.toFolders

class IOSGenerator(
    params: KmpModuleModel,
    private val isProject: Boolean,
) : PlatformGenerator(params) {
    override fun generateProject(
        ftManager: FileTemplateManager,
        packageName: String,
    ): List<GeneratorAsset> =
        if (isProject) {
            listOf(
                GeneratorTemplateFile(
                    "${params.composeNameLowerCase()}/src/iosMain/kotlin/${packageName.toFolders()}/${params.composeNameLowerCase()}/MainViewController.kt",
                    ftManager.getCodeTemplate(TemplateGroup.COMPOSE_IOS_MAIN),
                ),
                GeneratorEmptyDirectory("iosApp/iosApp.xcodeproj/project.xcworkspace/xcshareddata/swiftpm/configuration"),
                GeneratorTemplateFile(
                    "iosApp/iosApp/ContentView.swift",
                    ftManager.getCodeTemplate(TemplateGroup.IOS_CONTENTVIEW_SWIFT),
                ),
                GeneratorTemplateFile(
                    "iosApp/iosApp/Assets.xcassets/AppIcon.appiconset/Contents.json",
                    ftManager.getCodeTemplate(TemplateGroup.IOS_ICONS_CONTENTS_JSON),
                ),
                GeneratorTemplateFile(
                    "iosApp/iosApp/Assets.xcassets/AccentColor.colorset/Contents.json",
                    ftManager.getCodeTemplate(TemplateGroup.IOS_COLORS_CONTENTS_JSON),
                ),
                GeneratorTemplateFile(
                    "iosApp/iosApp/Assets.xcassets/Contents.json",
                    ftManager.getCodeTemplate(TemplateGroup.IOS_ASSETS_CONTENTS_JSON),
                ),
                GeneratorTemplateFile(
                    "iosApp/iosApp/Preview Content/Preview Assets.xcassets/Contents.json",
                    ftManager.getCodeTemplate(TemplateGroup.IOS_PREVIEW_CONTENTS_JSON),
                ),
                GeneratorTemplateFile(
                    "iosApp/iosApp/iOSApp.swift",
                    ftManager.getCodeTemplate(TemplateGroup.IOS_IOSAPP),
                ),
                GeneratorTemplateFile(
                    "iosApp/Configuration/Config.xcconfig",
                    ftManager.getCodeTemplate(TemplateGroup.IOS_APP_CONFIGURATION),
                ),
                GeneratorTemplateFile(
                    "iosApp/iosApp.xcodeproj/project.pbxproj",
                    ftManager.getCodeTemplate(TemplateGroup.IOS_PROJECT),
                ),
                GeneratorTemplateFile(
                    "iosApp/iosApp/Info.plist",
                    ftManager.getCodeTemplate(TemplateGroup.IOS_INFO_PLIST),
                ),
            )
        } else {
            emptyList()
        }

    override fun addToCommon(
        ftManager: FileTemplateManager,
        packageName: String,
    ): List<GeneratorAsset> =
        if (isProject) {
            emptyList()
        } else {
            listOf(
                GeneratorEmptyDirectory(
                    "src/iosMain/kotlin/${
                        packageName.replace(
                            ".",
                            "/"
                        )
                    }/${params.moduleLowerCase}"
                ),
            )
        }
}
