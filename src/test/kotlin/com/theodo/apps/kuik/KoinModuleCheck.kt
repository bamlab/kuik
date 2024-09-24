package com.theodo.apps.kuik

import com.theodo.apps.kuik.project.KmpProjectModule
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.verify.verify
import kotlin.test.Test

class KoinModuleCheck {
    @OptIn(KoinExperimentalAPI::class)
    @Test
    fun checkKoinModule() {
        KmpProjectModule.module.verify()
    }
}
