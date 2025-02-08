import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version "1.9.10"
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
}

group = "com.addzero"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {

    implementation(libs.cn.hutool.hutool.all)
    implementation(compose.desktop.currentOs)
    implementation(libs.compose.material3.desktop)
    implementation(libs.compose.icons.extended)

    implementation(libs.kotlinx.coroutines.core)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.7.3")

    implementation(libs.fastjson.kotlin)
    // Material Icons
    implementation(compose.materialIconsExtended)

    // Ktor
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.json)

    // Kotlinx serialization

    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlin.reflect)

    // Logging
    implementation(libs.logback.classic)

}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "second-brain"
            packageVersion = "1.0.0"
        }
    }
}