package com.theodo.apps.kuik.module.model.mockContent

val SettingsGradle2 = """
rootProject.name = "MyApplication"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}


include(":core:navigation")
include(":feature:home")
include(":feature:kmpsharedmodule")
include(":feature:dennisDT")
include(":feature:toto")
include(":composeApp")
"""
