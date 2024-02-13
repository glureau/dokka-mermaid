import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    kotlin("jvm")
    id("org.jetbrains.dokka")
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    dokkaPlugin("com.glureau:html-mermaid-dokka-plugin:0.6.0")
}
tasks.withType<DokkaTask>().configureEach {
    dokkaSourceSets {
        configureEach {
            val markdowns = fileTree("src/main/kotlin") {
                include("**/*.md")
            }
            includes.from(markdowns.files)
        }
    }
}
