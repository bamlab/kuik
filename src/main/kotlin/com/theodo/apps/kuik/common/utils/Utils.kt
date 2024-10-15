package com.theodo.apps.kuik.common.utils

import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import freemarker.template.Configuration
import freemarker.template.Template
import java.io.IOException
import java.io.StringWriter

object Utils {
    fun generateFileFromTemplate(
        templateName: String,
        dataModel: Map<String, Any>,
        outputDir: VirtualFile,
        outputFilePath: String,
    ) {
        val configuration =
            Configuration(Configuration.VERSION_2_3_30).apply {
                setClassLoaderForTemplateLoading(this::class.java.classLoader, "fileTemplates/code")
            }
        val template: Template = configuration.getTemplate("$templateName.ft")

        val outputFilePathParts = outputFilePath.split('/')
        val fileName = outputFilePathParts.last()
        val dirPath = outputFilePathParts.dropLast(1).joinToString("/")

        val targetDir =
            VfsUtil.createDirectoryIfMissing(outputDir, dirPath)
                ?: throw IOException("Could not create directory: $dirPath")

        val outputFile = try {
            targetDir.createChildData(this, fileName)
        } catch (e: IOException) {
            VfsUtil.refreshAndFindChild(targetDir, fileName)
        }
        if (outputFile != null) {
            StringWriter().use { writer ->
                template.process(dataModel, writer)
                VfsUtil.saveText(outputFile, writer.toString())
            }
        }


    }

    fun copyFileFromTemplate(
        fileName: String,
        outputDir: VirtualFile,
        outputFilePath: String
    ) {

        val resourcePath = "/copyFiles/$fileName"
        val resource = this::class.java.getResourceAsStream(resourcePath)
            ?: throw IOException("Resource not found: $resourcePath")

        val content = resource.readBytes()
        val outputFilePathParts = outputFilePath.split('/')
        val targetDirPath = outputFilePathParts.dropLast(1).joinToString("/")

        val targetDir = VfsUtil.createDirectoryIfMissing(outputDir, targetDirPath)
            ?: throw IOException("Could not create directory: $targetDirPath")

        try {
            targetDir.createChildData(this, fileName)
        } catch (e: IOException) {
            VfsUtil.refreshAndFindChild(targetDir, fileName)
        }?.setBinaryContent(content)

    }

}


fun String.toFolders() =
    replace(
        ".",
        "/",
    )
