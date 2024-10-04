import org.jetbrains.changelog.markdownToHTML

plugins {
    id("java") // Java support
    alias(libs.plugins.kotlin) // Kotlin support
    alias(libs.plugins.intelliJPlatform) // IntelliJ Platform Gradle Plugin
    alias(libs.plugins.changelog) // Gradle Changelog Plugin
    alias(libs.plugins.kotlin.serialization)
}

group = "com.theodo.apps"
version = "1.0.0"

kotlin {
    jvmToolchain(21)
}

repositories {
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
    google()
    maven {
        url = uri("https://www.jetbrains.com/intellij-repository/snapshots/")
    }
    maven {
        url = uri("https://oss.sonatype.org/content/repositories/snapshots/")
    }
    maven {
        url = uri("https://www.jetbrains.com/intellij-repository/releases")
    }
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
        version = "1.0.0"

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
