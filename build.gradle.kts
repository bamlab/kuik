import org.jetbrains.changelog.Changelog
import org.jetbrains.changelog.markdownToHTML
import org.jetbrains.intellij.platform.gradle.extensions.IntelliJPlatformExtension

plugins {
    id("java") // Java support
    alias(libs.plugins.kotlin) // Kotlin support
    alias(libs.plugins.intelliJPlatform) // IntelliJ Platform Gradle Plugin
    alias(libs.plugins.changelog) // Gradle Changelog Plugin
}

group = "com.theodo.apps"
version = providers.gradleProperty("pluginVersion")

kotlin {
    jvmToolchain(17)
}

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    intellijPlatform {
        //androidStudio("2024.2.1.9") // BUGGED https://intellij-support.jetbrains.com/hc/zh-cn/community/posts/21609427066130-Missing-essential-plugins-com-android-tools-design-org-jetbrains-android?page=1#community_comment_21621601482770
        androidStudio("2024.1.3.3")
        instrumentationTools()
        bundledPlugin("org.jetbrains.android")
        pluginVerifier()
        zipSigner()
    }
    implementation(libs.freemarker)
    implementation(libs.koin.jvm)
    testImplementation(libs.koin.test)
    implementation(libs.koin.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlin.test)
}

intellijPlatform {
    pluginConfiguration {
        name = "Kotlin Multiplatform Kuik"
        version = providers.gradleProperty("pluginVersion")
        vendor {
            name = "Dennis Bordet"
            email = "dennis.bordet@theodo.com"
        }


        // Extract the <!-- Plugin description --> section from README.md and provide for the plugin's manifest
        description = providers.fileContents(layout.projectDirectory.file("README.md")).asText.map {
            val start = "<!-- Plugin description -->"
            val end = "<!-- Plugin description end -->"

            with(it.lines()) {
                if (!containsAll(listOf(start, end))) {
                    throw GradleException("Plugin description section not found in README.md:\n$start ... $end")
                }
                subList(indexOf(start) + 1, indexOf(end)).joinToString("\n").let(::markdownToHTML)
            }
        }
        val changelog = project.changelog // local variable for configuration cache compatibility
        // Get the latest available change notes from the changelog file
        changeNotes = providers.gradleProperty("pluginVersion").map { pluginVersion ->
            with(changelog) {
                renderItem(
                    (getOrNull(pluginVersion) ?: getUnreleased())
                        .withHeader(false)
                        .withEmptySections(false),
                    Changelog.OutputType.HTML,
                )
            }
        }

        ideaVersion {
            sinceBuild = "231"
        }
    }

    signing {
        certificateChain = System.getenv("KUIK_CERTIFICATE_CHAIN")
        privateKey = System.getenv("KUIK_CERTIFICATE_KEY")
        password = System.getenv("KUIK_CERTIFICATE_PASSWORD")
    }

    publishing {
        token = System.getenv("KUIK_PUBLISH_TOKEN")
        channels = listOf("beta")
    }

    pluginVerification {
        ides {
            recommended()
        }
    }
}
