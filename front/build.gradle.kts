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
//    implementation("org.reflections:reflections:0.10.2")
//    implementation(libs.kotlin.reflect)
//    kotlin-reflect
    implementation("com.mikepenz:multiplatform-markdown-renderer-m3:+")

    implementation("com.mikepenz:multiplatform-markdown-renderer-code:+")

    // material3 contains opinionated components to streamline development
//    implementation("io.github.boswelja.markdown:material3:+")

    api(project(":backend"))
    implementation(compose.desktop.currentOs)
    implementation(compose.material3)
//    implementation(libs.compose.material3.desktop)
//    implementation(libs.compose.icons.extended)
    implementation(compose.materialIconsExtended)

//    implementation(libs.ktor.client.core)
//    implementation(libs.ktor.client.cio)
//    implementation(libs.ktor.serialization.json)
//    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.kotlinx.serialization.json)
}

compose.desktop {
    application {
        mainClass = "com.addzero.web.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "ui"
            packageVersion = "1.0.0"
        }
    }
}
