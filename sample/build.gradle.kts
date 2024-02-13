plugins {
    kotlin("jvm")
    id("org.jetbrains.dokka")
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    dokkaPlugin("com.glureau:html-mermaid-dokka-plugin:0.5.0")
}
