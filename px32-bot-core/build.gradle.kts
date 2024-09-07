plugins {
    id("java")
}

group = rootProject.group
version = rootProject.version

repositories {
    mavenCentral()
}

tasks.test {
    useJUnitPlatform()
}