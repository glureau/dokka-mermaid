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

    private val mermaidDetectionList = listOf(
        // https://mermaid-js.github.io/mermaid/#/flowchart?id=flowchart-orientation
        "flowchart TB",
        "flowchart TD",
        "flowchart BT",
        "flowchart RL",
        "flowchart LR",
        // https://mermaid-js.github.io/mermaid/#/sequenceDiagram
        "sequenceDiagram",
        // https://mermaid-js.github.io/mermaid/#/classDiagram
        "classDiagram",
        // https://mermaid-js.github.io/mermaid/#/stateDiagram
        "stateDiagram",
        "stateDiagram-v2",
        // https://mermaid-js.github.io/mermaid/#/entityRelationshipDiagram
        "erDiagram",
        // https://mermaid-js.github.io/mermaid/#/user-journey
        "journey",
        // https://mermaid-js.github.io/mermaid/#/gantt
        "gantt",
        // https://mermaid-js.github.io/mermaid/#/pie
        "pie",
        "pie title .*",
        // https://mermaid-js.github.io/mermaid/#/requirementDiagram
        "requirementDiagram",

        // deduced from https://mermaid-js.github.io/mermaid/#/examples
        "graph TB",
        "graph TD",
        "graph BT",
        "graph RL",
        "graph LR",
    ).map { Regex(it) }

    override fun buildHtml(page: PageNode, resources: List<String>, content: FlowContent.() -> Unit): String {
        // TODO: Define a "Style"/script installer like here https://github.com/Kotlin/dokka/blob/c1efae49d5595c79ccbf004ceb6aabd7367ed9de/plugins/versioning/src/main/kotlin/versioning/htmlPreprocessors.kt
        val addedRes: List<String> = listOf(
            *resources.toTypedArray(),
            "https://cdnjs.cloudflare.com/ajax/libs/mermaid/6.0.0/mermaid.css",
            "https://cdnjs.cloudflare.com/ajax/libs/mermaid/6.0.0/mermaid.js"
        )
        return super.buildHtml(page, addedRes, content)
    }

    override fun FlowContent.buildCodeBlock(code: ContentCodeBlock, pageContext: ContentPage) {
        // TODO: Understand why language is not filled with "mermaid" in the unit test
        var isMermaidGraph = code.language == "mermaid"
        if (!isMermaidGraph && code.language == "") { // Trying to guess if it's actually a Mermaid graph
            val firstLine = (code.children.firstOrNull() as? ContentText)?.text?.trim()
            if (firstLine != null) {
                isMermaidGraph = mermaidDetectionList.any { it.matches(firstLine) }
            }
        }
        if (isMermaidGraph) {
            div("sample-container") {
                div("mermaid") {
                    +code.children.filterIsInstance<ContentText>().joinToString("\n") { it.text }
                }
            }
        } else {
            // TODO: Original code from HtmlRenderer, no idea how to override and use super of a member extension function...
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