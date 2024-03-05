package template

import org.jetbrains.dokka.base.testApi.testRunner.BaseAbstractTest
import org.junit.Test
import signatures.renderedContent
import utils.TestOutputWriterPlugin
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class HtmlMermaidDokkaPluginTest : BaseAbstractTest() {
    private val configuration = dokkaConfiguration {
        sourceSets {
            sourceSet {
                sourceRoots = listOf("src/main/kotlin")
            }
        }
    }

    @Test
    fun `my plugin should render class code blocks containing mermaid code`() {
        val writerPlugin = TestOutputWriterPlugin()
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
            cleanupOutput = false,
            pluginOverrides = listOf(writerPlugin)
        ) {
            documentablesTransformationStage = { module ->
                val testedPackage = module.packages.find { it.name == "sample" }!!
                val testedClass = testedPackage.classlikes.find { it.name == "ChristmasStateMachine" }!!
                val codeBlock = testedClass.documentation.values.first().children.first().children[1]
                assertEquals("mermaid", codeBlock.params["lang"])
            }
            renderingStage = { _, _ ->
                val content = writerPlugin.writer.renderedContent("root/sample/-christmas-state-machine/index.html")

                val sampleContainer = assertNotNull(content.getElementsByClass("sample-container").singleOrNull())
                val divWithMermaidId = assertNotNull(sampleContainer.children().singleOrNull())
                assertTrue(divWithMermaidId.id().startsWith("mermaid-container-"))
                val codeBlockParent = assertNotNull(sampleContainer.parent())
                val script = assertNotNull(codeBlockParent.getElementsByTag("script").singleOrNull()).data()

                // just a minimal check, that script contains mermaid declaration and original graph
                assertContains(script, "var container = document.getElementById('mermaid-container-")
                assertContains(script, "A[Christmas] -->|Get money| B(Go shopping)")
            }
        }
    }
}
