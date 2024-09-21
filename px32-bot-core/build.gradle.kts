import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.gradleup.shadow") version "8.3.0"
}

group = rootProject.group
version = rootProject.version

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":${rootProject.name}-api"))
    implementation(project(":${rootProject.name}-func"))
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks {
    withType<KotlinCompile> {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }

    processResources {
        filesMatching("*.properties") {
            expand(project.properties)
        }
    }

    shadowJar {
        archiveBaseName.set(rootProject.name)
        archiveClassifier.set("")
        archiveVersion.set("")

        manifest {
            attributes["Main-Class"] = "net.projecttl.p.x32.Px32Kt"
        }
    }

    test {
        useJUnitPlatform()
    }
}