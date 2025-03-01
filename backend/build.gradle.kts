plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
    alias(libs.plugins.ksp)
//    application
}

version = "1.0.0"

ksp {
    arg("jimmer.dto.dirs", "src/main/kotlin")
    arg("jimmer.dto.defaultNullableInputModifier", "dynamic")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}


tasks.register<Copy>("copyLibs") {
    into("build/libs/lib")
    from(configurations.runtimeClasspath)
}

tasks.bootJar {
    enabled = true
    archiveClassifier = "exec"
    setExcludes(listOf("*.jar"))
    dependsOn("copyLibs")
    manifest {
        attributes["Class-Path"] = configurations.runtimeClasspath
            .get().files
            .joinToString(" ") { "lib/${it.name}" }
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

dependencies {

    // Kotlin Dependencies
    // ----------------------------------------
//    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.coroutines.core)

    implementation("org.tomlj:tomlj:1.1.1")
    // 引入 Spring Boot 相关依赖
    api(libs.bundles.spring.boot)

    // 引入工具库
    api(libs.bundles.tools)

    // 引入数据库驱动
    runtimeOnly(libs.bundles.database)

    api(libs.jimmer)
    ksp(libs.jimmer.ksp)

    // 引入 Spring AI 相关依赖
    api(platform(libs.spring.ai.bom))
    api(libs.bundles.spring.ai)

    // 测试依赖
    testRuntimeOnly(libs.junit.platform.launcher)
    testImplementation(libs.bundles.testing)




}
