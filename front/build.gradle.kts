import org.jetbrains.compose.desktop.application.dsl.TargetFormat
plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.compose)
    alias(libs.plugins.jetbrainsCompose)

    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
//    alias(libs.plugins.ksp)

}

dependencies {

    api(project(":backend"))
    implementation(compose.desktop.currentOs)
    implementation(compose.material3)
//    implementation(libs.compose.material3.desktop)
//    implementation(libs.compose.icons.extended)
    implementation(libs.kotlinx.coroutines.core)
    implementation(compose.materialIconsExtended)

//    implementation(libs.ktor.client.core)
//    implementation(libs.ktor.client.cio)
//    implementation(libs.ktor.serialization.json)
//    implementation(libs.ktor.client.content.negotiation)
//    implementation(libs.kotlinx.serialization.json)
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "ui"
            packageVersion = "1.0.0"
        }
    }
}
