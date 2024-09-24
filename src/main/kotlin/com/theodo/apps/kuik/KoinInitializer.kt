package com.theodo.apps.kuik

import com.theodo.apps.kuik.project.KmpProjectModule
import org.koin.core.context.startKoin
import org.koin.core.error.KoinApplicationAlreadyStartedException

object KoinInitializer {
    fun initKoin() {
        try {
            startKoin {
                modules(KmpProjectModule.module)
            }
        } catch (e: KoinApplicationAlreadyStartedException) {
            // Koin is already started, do nothing
        }
    }
}
