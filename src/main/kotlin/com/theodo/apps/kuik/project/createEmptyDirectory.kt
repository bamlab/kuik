package com.theodo.apps.kuik.project

import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile

fun createEmptyDirectory(
    parent: VirtualFile,
    path: String,
): VirtualFile = VfsUtil.createDirectoryIfMissing(parent, path)
