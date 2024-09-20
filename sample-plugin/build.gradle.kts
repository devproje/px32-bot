plugins {
    groovy
    id("com.gradleup.shadow") version "8.3.0"
}

group = rootProject.group
version = "0.1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(project(":px32-bot-api"))
    implementation("org.apache.groovy:groovy:4.0.14")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks {
    withType<GroovyCompile> {
        options.encoding = "UTF-8"
    }

    processResources {
        filesMatching("plugin.json") {
            expand(project.properties)
        }
    }

    shadowJar {
        archiveBaseName.set(project.name)
        archiveClassifier.set("")
        archiveVersion.set("")
    }

    test {
        useJUnitPlatform()
    }
}