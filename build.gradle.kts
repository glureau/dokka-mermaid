import org.gradle.api.plugins.internal.DefaultAdhocSoftwareComponent
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.net.URI

plugins {
    kotlin("jvm") version "1.7.20"
    id("org.jetbrains.dokka") version "1.7.20"
    `maven-publish`
    signing
}

group = "com.glureau"
version = "0.4.7"

repositories {
    mavenCentral()
    jcenter()
}

val dokkaVersion: String by project
dependencies {
    implementation(kotlin("stdlib"))
    compileOnly("org.jetbrains.dokka:dokka-core:$dokkaVersion")
    implementation("org.jetbrains.dokka:dokka-base:$dokkaVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.7.3")

    testImplementation(kotlin("test-junit"))
    testImplementation("org.jetbrains.dokka:dokka-test-api:$dokkaVersion")
    testImplementation("org.jetbrains.dokka:dokka-base-test-utils:$dokkaVersion")
}

val dokkaOutputDir = "$buildDir/dokka"

tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }
    dokkaHtml {
        outputDirectory.set(file(dokkaOutputDir))
    }
}

val javadocJar by tasks.registering(Jar::class) {
    dependsOn(tasks.dokkaHtml)
    archiveClassifier.set("javadoc")
    from(dokkaOutputDir)
}

java {
    withSourcesJar()
}

/**
 * As we've multiple js files and they are loaded asynchronously by Dokka,
 * we can't change the execution order. As we need to change the mermaid initialize parameters,
 * we need to merge those js files automatically.
 */
task("mergeJs") {
    val dir = rootDir.path + "/src/main/js"
    File(rootDir.path + "/src/main/resources/dokka/dokka-mermaid.js").writeText(
        File(dir + "/mermaid.min.js").readText() + "\n" +
                File(dir + "/extras.js").readText()
    )
}

publishing {
    publications {
        val htmlMermaidDokkaPlugin by creating(MavenPublication::class) {
            artifactId = project.name
            from(components["java"])
            artifact(javadocJar.get())

            pom {
                name.set("Mermaid Html Dokka plugin")
                description.set("Plugin to support Mermaid.js graphs from HTML renderer")
                url.set("https://github.com/glureau/dokka-mermaid")

                licenses {
                    license {
                        name.set("The Apache Software License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                        distribution.set("repo")
                    }
                }

                developers {
                    developer {
                        id.set("glureau")
                        name.set("Grégory Lureau")
                    }
                }

                scm {
                    connection.set("scm:git:git://github.com/glureau/dokka-mermaid.git")
                    url.set("https://github.com/glureau/dokka-mermaid/tree/master")
                }
            }
        }
        signPublicationsIfKeyPresent(htmlMermaidDokkaPlugin)
    }

    repositories {
        maven {
            url = URI("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            credentials {
                username = System.getenv("SONATYPE_USER")
                password = System.getenv("SONATYPE_PASSWORD")
            }
        }
    }
}

fun Project.signPublicationsIfKeyPresent(publication: MavenPublication) {
    val signingKey: String? = System.getenv("SIGN_KEY")
    val signingKeyPassphrase: String? = System.getenv("SIGN_KEY_PASSPHRASE")

    if (!signingKey.isNullOrBlank()) {
        extensions.configure<SigningExtension>("signing") {
            useInMemoryPgpKeys(signingKey, signingKeyPassphrase)
            sign(publication)
        }
    }
}
