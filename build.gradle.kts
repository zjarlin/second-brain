plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.spring) apply false
    alias(libs.plugins.spring.boot) apply false
    alias(libs.plugins.spring.dependency.management) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.compose) apply false
    alias(libs.plugins.jetbrainsCompose) apply false
//    kotlin("plugin.serialization") version "1.8.0"
//    id("org.jetbrains.compose") apply false
//    id("org.jetbrains.kotlin.plugin.compose") apply false
}

subprojects {
    group = "com.addzero"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")

        google()
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

dependencies {
//    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")

//    implementation(libs.kotlin.reflect)
}





