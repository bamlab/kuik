import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.0.0"
    id("org.jetbrains.intellij") version "1.17.4"
    id("idea")
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.0"
}

group = "com.theodo.apps"
version = "1.0.0"

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
}

// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    val platformType = providers.gradleProperty("platformType")
    val platformVersion = providers.gradleProperty("platformVersion")

    type.set(platformType)
    version.set(platformVersion)

    plugins.set(
        listOf(
            "org.jetbrains.android",
            "com.intellij.java",
        ),
    )
}

dependencies {
    implementation("org.freemarker:freemarker:2.3.31")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:2.0.20")
    testImplementation("io.mockk:mockk-jvm:1.13.12")
    implementation("io.insert-koin:koin-core-jvm:4.0.0")
    testImplementation("io.insert-koin:koin-test-jvm:4.0.0")
}

tasks {
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    patchPluginXml {
        sinceBuild.set("231")
        untilBuild.set("241.*")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}

configurations.all {
    exclude("org.slf4j", "slf4j-api")
}
