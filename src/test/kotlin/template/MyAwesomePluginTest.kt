package template

import junit.framework.Assert.assertNotNull
import org.jetbrains.dokka.base.testApi.testRunner.BaseAbstractTest
import org.junit.Test

class MyAwesomePluginTest : BaseAbstractTest() {
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
            |data class TestingIsEasy(val reason: String)
            """.trimIndent(), configuration
        ) {
            documentablesTransformationStage = { module ->
                val testedPackage = module.packages.find { it.name == "sample" }
                val testedClass = testedPackage?.classlikes?.find { it.name == "TestingIsEasy" }

                assertNotNull(testedPackage)
                assertNotNull(testedClass)
            }
        }
    }
}