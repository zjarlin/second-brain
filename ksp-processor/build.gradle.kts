plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ksp)
}
//ksp { arg("ksp.experimental", "true") }
kotlin {
    sourceSets.main {
        kotlin.srcDir("build/generated/ksp/main/kotlin")
    }
}

java {
    withSourcesJar()
    withJavadocJar()
}

dependencies {
    api(project(":ksp-annotation"))
    implementation("com.google.devtools.ksp:symbol-processing-api:1.9.21-1.0.15")
//    implementation("androidx.compose.material:material-icons-extended:+")
//    implementation("com.google.auto.service:auto-service-annotations:1.1.1")
//    ksp("com.google.auto.service:auto-service:1.1.1")
}
