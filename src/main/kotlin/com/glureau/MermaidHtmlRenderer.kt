package com.glureau

import kotlinx.html.FlowContent
import kotlinx.html.code
import kotlinx.html.div
import kotlinx.html.pre
import org.jetbrains.dokka.base.renderers.html.HtmlRenderer
import org.jetbrains.dokka.pages.ContentCodeBlock
import org.jetbrains.dokka.pages.ContentPage
import org.jetbrains.dokka.pages.ContentText
import org.jetbrains.dokka.pages.PageNode
import org.jetbrains.dokka.plugability.DokkaContext

open class MermaidHtmlRenderer(
    context: DokkaContext
) : HtmlRenderer(context) {

    override fun buildHtml(page: PageNode, resources: List<String>, content: FlowContent.() -> Unit): String {
        val addedRes: List<String> = listOf(*resources.toTypedArray(),
            "https://cdnjs.cloudflare.com/ajax/libs/mermaid/6.0.0/mermaid.css",
            "https://cdnjs.cloudflare.com/ajax/libs/mermaid/6.0.0/mermaid.js"
        )
        return super.buildHtml(page, addedRes, content)
    }

    override fun FlowContent.buildCodeBlock(code: ContentCodeBlock, pageContext: ContentPage) {
        val guessMermaid: Boolean =
            (code.language == "") && ((code.children.firstOrNull() as? ContentText)?.text?.startsWith("graph ") == true)

        if (code.language == "mermaid" || guessMermaid) {
            div("sample-container") {
                div("mermaid") {
                    +code.children.filterIsInstance<ContentText>().joinToString("\n") { it.text }
                }
            }
        } else {
            // Original code from HtmlRenderer, no idea how to override and use super of an extension function...
            div("sample-container") {
                code(code.style.joinToString(" ") { it.toString().toLowerCase() }) {
                    attributes["theme"] = "idea"
                    pre {
                        code.children.forEach { buildContentNode(it, pageContext) }
                    }
                }
            }
        }
    }
}