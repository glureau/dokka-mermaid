# Mermaid Html Dokka plugin

## Step 1: install

```kotlin
dependencies {
  dokkaPlugin("com.glureau:html-markdown-dokka-plugin:0.1.0")
}
```

## Step 2: put your Mermaid graphs in your code comments.

```kotlin
     /**
      * A visual illustration brought by Mermaid:
      * ```mermaid
      * graph LR
      *   A[Christmas] -->|Get money B|(Go shopping)
      *   B --> C{Let me think}
      *   C -->|One| D[Laptop]
      *   C -->|Two| E[iPhone]
      *   C -->|Three| F[fa:fa-car Car]
      * ```
      */
    class ChristmasStateMachine
```

## Step 3: enjoy your Dokka documentation

`./gradlew dokkaHtml`

![img.png](doc/img.png)