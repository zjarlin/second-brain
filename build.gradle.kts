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

// 定义要发布到JitPack的模块列表及其描述
val publishingModules = mapOf(
    "ksp-annotation" to "Kotlin Symbol Processing DSL Annotations",
    "ksp-processor" to "Kotlin Symbol Processing DSL Processor"
)

subprojects {
    group = "com.addzero"
    version = "1.0.0"

    repositories {
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://jitpack.io")
        google()
    }

    // 为指定的模块配置发布设置
    if (publishingModules.containsKey(name)) {
        apply(plugin = "maven-publish")
        apply(plugin = "java")

        configure<JavaPluginExtension> {
            withSourcesJar()
            withJavadocJar()
        }

        configure<PublishingExtension> {
            publications {
                create<MavenPublication>("maven") {
                    groupId = project.group.toString()
                    artifactId = project.name
                    version = project.version.toString()

                    from(components["java"])

                    pom {
                        name.set(project.name)
                        description.set(publishingModules[project.name])
                        url.set("https://gitee.com/zjarlin/second-brain.git")

                        licenses {
                            license {
                                name.set("The Apache License, Version 2.0")
                                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                            }
                        }

                        developers {
                            developer {
                                id.set("zjarlin")
                                name.set("zjarlin")
                                email.set("zjarlin@outlook.com")
                            }
                        }
                    }
                }
            }
        }
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

dependencies {
//    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")

//    implementation(libs.kotlin.reflect)
}





