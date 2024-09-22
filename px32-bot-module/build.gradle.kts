plugins {
    id("java")
}

group = rootProject.group
version = rootProject.version

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(project(":px32-bot-api"))
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks {
    processResources {
        filesMatching("plugin.json") {
            expand(project.properties)
        }
    }

    test {
        useJUnitPlatform()
    }
}