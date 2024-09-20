plugins {
    kotlin("jvm") version "2.0.20"
    kotlin("plugin.serialization") version "2.0.20"
}

group = property("group")!!
version = property("version")!!
plugins
val ktor_version: String by project
val log4j_version: String by project
val exposed_version: String by project

allprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.plugin.serialization")

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(21))
        }

        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    repositories {
        mavenCentral()
    }
}

subprojects {
    dependencies {
        implementation(kotlin("stdlib"))
        implementation(kotlin("reflect"))
        implementation("net.dv8tion:JDA:5.1.0")
        implementation("io.ktor:ktor-client-cio:$ktor_version")
        implementation("io.ktor:ktor-client-core:$ktor_version")
        implementation("org.apache.logging.log4j:log4j-api:$log4j_version")
        implementation("org.apache.logging.log4j:log4j-core:$log4j_version")
        implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
        implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
        implementation("org.apache.logging.log4j:log4j-slf4j2-impl:$log4j_version")
        implementation("io.ktor:ktor-client-okhttp-jvm:2.3.12")
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.2")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
        testImplementation(platform("org.junit:junit-bom:5.10.0"))
        testImplementation("org.junit.jupiter:junit-jupiter")
    }
}

tasks.test {
    useJUnitPlatform()
}
