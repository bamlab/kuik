package com.theodo.apps.kuik.common.models

import com.intellij.ide.starters.local.GeneratorTemplateFile

val GeneratorTemplateFile.justCopy: Boolean
    get() {
        return this.relativePath.endsWith(".ttf")
    }