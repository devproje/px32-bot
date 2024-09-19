import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `maven-publish`
    signing
}

group = rootProject.group
version = rootProject.version

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks {
    withType<Javadoc> {
        options.encoding = "UTF-8"
    }

    withType<KotlinCompile> {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }

    create<Jar>("sourcesJar") {
        archiveClassifier.set("sources")
        from(sourceSets["main"].allSource)
    }

    create<Jar>("javadocJar") {
        archiveClassifier.set("javadoc")
        dependsOn("dokkaHtml")
        from("${projectDir}/build/dokka/html")
    }

    test {
        useJUnitPlatform()
    }
}

publishing {
    publications {
        create<MavenPublication>("${rootProject.name}-api") {
            from(components["java"])
            artifacts {
                tasks["sourcesJar"]
                tasks["javadocJar"]
            }

            repositories {
                maven {
                    name = "ProjectCentral"
                    val releasesRepoUrl = "https://repo.wh64.net/repository/maven-releases"
                    val snapshotsRepoUrl = "https://repo.wh64.net/repository/maven-snapshots"
                    url = uri(if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl)

                    credentials.runCatching {
                        val nexusUsername: String by project
                        val nexusPassword: String by project

                        username = nexusUsername
                        password = nexusPassword
                    }
                }
            }

            pom {
                name.set(rootProject.name)
                description.set("Px32 Discord Bot framework api for Kotlin")

                licenses {
                    license {
                        name.set("MIT License")
                    }
                }

                developers {
                    developer {
                        id.set("devproje")
                        name.set("Project_IO")
                        email.set("me@projecttl.net")
                    }
                }
            }
        }
    }
}

signing {
    isRequired = true
    sign(publishing.publications["${rootProject.name}-api"])
}
