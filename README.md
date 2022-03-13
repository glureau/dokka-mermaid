# Dokka plugin template

This repository provides a template for creating [Dokka](https://github.com/Kotlin/dokka) plugins 
(check the [Creating a repository from a template](https://help.github.com/en/enterprise/2.20/user/github/creating-cloning-and-archiving-repositories/creating-a-repository-from-a-template) article).

> **TL;DR:** Click the <kbd>Use this template</kbd> button and clone it in IntelliJ IDEA.

### Getting started

Before writing a plugin it might be beneficial to spend 5 minutes reading our [developer guide](https://kotlin.github.io/dokka/1.5.0/developer_guide/introduction/) to have a basic idea about the data model and system architecture.
If some you miss some information or something is unclear please let us know on [community slack](https://kotlinlang.slack.com/archives/C0F4UNJET) or via [github issue](https://github.com/Kotlin/dokka/issues).

### Structure
A minimal repository for a Dokka plugin should contain files in the following structure:
```
.
├── LICENSE
├── README.md
├── build.gradle.kts
├── gradle
│   └── wrapper
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradle.properties
├── gradlew
├── gradlew.bat
├── settings.gradle.kts
└── src
    └── main
        ├── kotlin
        │   └── template
        │       └── MyAwesomeDokkaPlugin.kt
        └── resources
            └── META-INF
                └── services
                    └── org.jetbrains.dokka.plugability.DokkaPlugin

```

### Applying the plugin

In order to apply a plugin it needs to be published to a repository.
For development use we recommend the mavenLocal.
This repository contains a basic setup for publishing artefacts to that repository.
In order to do it use `publishToMavenLocal` task. 

After that you will need a sample project to test on.
Kotlin has a lot of sample projects to choose from eg: [dokka-gradle-example](https://github.com/Kotlin/kotlin-examples/tree/master/gradle/dokka/dokka-gradle-example)

In order to apply the plugin you need to add it to project dependencies:
```kotlin
dependencies {
    dokkaPlugin("template:template-dokka-plugin:0.1")
}
```

Please keep in mind, that you need to have a `mavenLocal()` repository in your project.

After that you can run Dokka on this project and see the results. 
You should use a Dokka command you desire to write a plugin for (eg. `dokkaHtml`, `dokkaGfm` or other) with `--info` logging level.
In project logs you should see the name of a plugin:
```
...
Initializing plugins
Loaded plugins: [org.jetbrains.dokka.base.DokkaBase, template.MyAwesomeDokkaPlugin]
Loaded: [
...
```

### Testing

This project includes a test dependency on `dokka-test-api` and `dokka-base-test-utils` that allows for easy testing. 
We highly encourage for you to extend tests classes with `BaseAbstractTest()` which allows you to write kotlin or java code
in your tests without a need to provide external files.
This way the tests are much cleaner and easier to reason about.

This repository contains most basic example of a [test using this mechanism](src/test/kotlin/template/MyAwesomePluginTest.kt).

### Debugging

Sometimes things don't work as we expected :) 

From our experience using debugger is the most efficient.
Apart from debugging tests you are able to debug whole projects while Dokka is running.
Enable the debugger in the project you wish to generate documentation for using `org.gradle.debug = true` and,
in intellij with your plugin, run the remote configuration.

For more information please visit [official intellij guide](https://www.jetbrains.com/help/idea/tutorial-remote-debug.html#67dc8)

### Publishing

#### Publishing locally

In order to test your plugin locally, please use the `publishToMavenLocal` task, as explained in the [Applying the plugin](#applying-the-plugin) section.

#### Publishing to Maven Central

Publishing extension has been preconfigured for deployment to Maven Central repository via OSSRH.
A jar file with documentation (`javadoc.jar`) is created with Dokka.
In order to sigh the publication, you have to provide one of the following sets of environmental variables:

1) * SIGN_KEY_ID - The public key ID (The last 8 symbols of the keyId)
   * SIGN_KEY - The secret (private) key
   * SIGN_KEY_PASSPHRASE - The passphrase used to protect your private key
   
2) * SIGN_KEY - The secret (private) key
   * SIGN_KEY_PASSPHRASE - The passphrase used to protect your private key
  
For more information about signing the publication, please refer to the [Signing Plugin readme](https://docs.gradle.org/current/userguide/signing_plugin.html).

OSSRH credentials also have to be provided via
 
* SONATYPE_USER 
* SONATYPE_PASSWORD
    
environmental variables. 

Please follow the [OSSRH guide](https://central.sonatype.org/pages/ossrh-guide.html) for the detailed steps on how to get the credentials and claim the group name.

Finally, the publication can be started with `./gradlew publish` command

### Final words
After creating a plugin please consider sharing it with the community on [official Dokka plugins list](https://kotlin.github.io/dokka/1.5.0/community/plugins-list/)
