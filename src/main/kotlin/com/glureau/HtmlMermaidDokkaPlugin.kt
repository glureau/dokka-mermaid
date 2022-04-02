package com.glureau

import org.jetbrains.dokka.CoreExtensions
import org.jetbrains.dokka.base.DokkaBase
import org.jetbrains.dokka.pages.RendererSpecificResourcePage
import org.jetbrains.dokka.pages.RenderingStrategy
import org.jetbrains.dokka.pages.RootPageNode
import org.jetbrains.dokka.plugability.DokkaContext
import org.jetbrains.dokka.plugability.DokkaPlugin
import org.jetbrains.dokka.transformers.pages.PageTransformer
import java.io.File

class HtmlMermaidDokkaPlugin : DokkaPlugin() {

    private val dokkaBase by lazy { plugin<DokkaBase>() }

    val renderer by extending {
        CoreExtensions.renderer providing ::MermaidHtmlRenderer override dokkaBase.htmlRenderer
    }

    val mermaidInstaller by extending {
        dokkaBase.htmlPreprocessors providing ::MermaidInstaller order {
            after(dokkaBase.assetsInstaller)
            before(dokkaBase.customResourceInstaller)
        }
    }

    init {
        println("Mermaid Plugin installed")
    }
}

class MermaidInstaller(private val dokkaContext: DokkaContext) : PageTransformer {
    private val components = listOf(
        // File generated (merged) by gradle from src/main/js
        "dokka-mermaid.js",
    )

    override fun invoke(input: RootPageNode): RootPageNode {
        return input
            .modified(children = input.children + components.toRenderSpecificResourcePage())
            .transformContentPagesTree { it.modified(embeddedResources = it.embeddedResources + components) }
    }

    private fun List<String>.toRenderSpecificResourcePage(): List<RendererSpecificResourcePage> =
        map { RendererSpecificResourcePage(it, emptyList(), RenderingStrategy.Copy("/dokka/$it")) }
}