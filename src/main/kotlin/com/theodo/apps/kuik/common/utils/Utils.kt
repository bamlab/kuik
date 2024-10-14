package com.theodo.apps.kuik.common.utils

import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import freemarker.template.Configuration
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
        val template = configuration.getTemplate("$templateName.ft")

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
}

fun String.toFolders() =
    replace(
        ".",
        "/",
    )
