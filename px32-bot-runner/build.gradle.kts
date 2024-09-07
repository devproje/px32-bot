import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.gradleup.shadow") version "8.3.0"
}

group = rootProject.group
version = rootProject.version

tasks {
    test {
        useJUnitPlatform()
    }

    processResources {
        filesMatching("default.properties") {
            expand(project.properties)
        }
    }

    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    withType<KotlinCompile> {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }
}