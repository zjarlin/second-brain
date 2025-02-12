include(
    ":front",
    ":backend",
)

pluginManagement {
    repositories {
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://repo.spring.io/milestone")
        maven { url = uri("https://repo.spring.io/milestone") }

        google()
        gradlePluginPortal()
        mavenCentral()
    }


    plugins {
        id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
    }
}








rootProject.name = "second-brain"
