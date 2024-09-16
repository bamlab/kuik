package com.theodo.apps.kuik.module.model

enum class ModuleType {
    FEATURE,
    CORE,
    DOMAIN,
    DATA,
    ;

    fun folderName() = this.name.lowercase()
}
