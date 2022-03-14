package template

import org.jetbrains.dokka.base.testApi.testRunner.BaseAbstractTest
import org.junit.Test
import kotlin.test.assertEquals

class MermaidHtmlDokkaPluginTest : BaseAbstractTest() {
    private val configuration = dokkaConfiguration {
        sourceSets {
            sourceSet {
                sourceRoots = listOf("src/main/kotlin")
            }
        }
    }

    @Test
    fun `my plugin should render class code blocks containing mermaid code`() {
        testInline(
            """
            |/src/main/kotlin/sample/Test.kt
            |package sample
            | /**
            |  * A visual illustration brought by Mermaid:
            |  * ```mermaid
            |  * graph TD
            |  *   A[Christmas] -->|Get money| B(Go shopping)
            |  *   B --> C{Let me think}
            |  *   C -->|One| D[Laptop]
            |  *   C -->|Two| E[iPhone]
            |  *   C -->|Three| F[fa:fa-car Car]
            |  * ```
            |  */
            |class ChristmasStateMachine
            """.trimIndent(), configuration,
            cleanupOutput = false
        ) {
            documentablesTransformationStage = { module ->
                val testedPackage = module.packages.find { it.name == "sample" }!!
                val testedClass = testedPackage.classlikes.find { it.name == "ChristmasStateMachine" }!!
                val codeBlock = testedClass.documentation.values.first().children.first().children[1]
                assertEquals("mermaid", codeBlock.params["lang"])
                // Here I can check more about the structure but no idea where to check the generated html.
            }
        }
    }
}