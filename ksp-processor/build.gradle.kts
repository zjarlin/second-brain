plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ksp)
}

kotlin {
    sourceSets.main {
        kotlin.srcDir("build/generated/ksp/main/kotlin")
    }
}

dependencies {
    implementation(project(":ksp-annotation"))
    implementation("com.google.devtools.ksp:symbol-processing-api:+")
//    implementation("com.google.auto.service:auto-service-annotations:1.1.1")
//    ksp("com.google.auto.service:auto-service:1.1.1")
}
