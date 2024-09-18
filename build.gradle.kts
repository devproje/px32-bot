plugins {
    id("java")
    kotlin("jvm") version "2.0.20"
    id("com.gradleup.shadow") version "8.3.0"
}

group = property("group")!!
version = property("version")!!

val ktor_version: String by project
val log4j_version: String by project
val exposed_version: String by project

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
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks {
    test {
        useJUnitPlatform()
    }
}
