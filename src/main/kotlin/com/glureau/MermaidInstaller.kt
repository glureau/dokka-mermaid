package com.glureau

import org.jetbrains.dokka.pages.RendererSpecificResourcePage
import org.jetbrains.dokka.pages.RenderingStrategy
import org.jetbrains.dokka.pages.RootPageNode
import org.jetbrains.dokka.plugability.DokkaContext
import org.jetbrains.dokka.transformers.pages.PageTransformer

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