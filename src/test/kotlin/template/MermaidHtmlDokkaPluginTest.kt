package template

import junit.framework.Assert.assertNotNull
import org.jetbrains.dokka.base.testApi.testRunner.BaseAbstractTest
import org.junit.Test

class MermaidHtmlDokkaPluginTest : BaseAbstractTest() {
    private val configuration = dokkaConfiguration {
        sourceSets {
            sourceSet {
                sourceRoots = listOf("src/main/kotlin")
            }
        }
    }

    @Test
    fun `my awesome plugin should find packages and classes`() {
        testInline(
            """
            |/src/main/kotlin/sample/Test.kt
            |package sample
            | /**
            |  * A visual illustration brought by Mermaid:
            |  * ``` mermaid
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
                val testedPackage = module.packages.find { it.name == "sample" }
                val testedClass = testedPackage?.classlikes?.find { it.name == "TestingIsEasy" }

                assertNotNull(testedPackage)
                assertNotNull(testedClass)

                // TODO: Proper testing...
            }
        }
    }
}