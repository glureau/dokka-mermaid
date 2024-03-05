package com.glureau

import kotlinx.html.FlowContent
import kotlinx.html.div
import kotlinx.html.id
import kotlinx.html.script
import kotlinx.html.unsafe
import org.jetbrains.dokka.base.renderers.html.HtmlCodeBlockRenderer
import kotlin.math.absoluteValue
import kotlin.random.Random

internal class MermaidHtmlRenderer(
    private val config: HtmlMermaidConfiguration?
) : HtmlCodeBlockRenderer {

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
        "pie title",
        // https://mermaid-js.github.io/mermaid/#/requirementDiagram
        "requirementDiagram",

        // https://mermaid-js.github.io/mermaid/#/gitgraph
        "gitGraph",

        // deduced from https://mermaid-js.github.io/mermaid/#/examples
        "graph TB",
        "graph TD",
        "graph BT",
        "graph RL",
        "graph LR",

        "C4Context",
        "mindmap",
        "timeline",
        "zenuml",
        "sankey-beta",
        "xychart-beta",
        "block-beta",
    )

    override fun isApplicableForDefinedLanguage(language: String): Boolean = language == "mermaid"

    override fun isApplicableForUndefinedLanguage(code: String): Boolean =
        mermaidDetectionList.any { code.startsWith(it, ignoreCase = true) }

    override fun FlowContent.buildCodeBlock(language: String?, code: String) {
        val graphDef = code.replace("\"", "\\\"")
        val rand = Random.nextLong().absoluteValue.toString(32)
        val mermaidContainerId = "mermaid-container-$rand"
        val mermaidTargetId = "mermaid-target-$rand"
        div("sample-container") {
            div {
                id = mermaidContainerId
            }
        }
        script {
            unsafe {
                +"""
                |
                |window.addEventListener('load', function() {
                |  var graphDef =  `$graphDef`;
                |  var container = document.getElementById('$mermaidContainerId');
                |  container.innerHTML = '<div id="$mermaidTargetId"></div>';
                |  var updateGraph$rand = function() {
                |    // setTimeout required or else the 1st render could be done before mermaid.initialize() has applied the theme.
                |    // Also required because we can't listen to localStorage events reliably, and it's changed on the same click event...
                |    setTimeout(() => {
                |      var dokkaDarkModeItem = localStorage.getItem("dokka-dark-mode");
                |      var isDarkMode = dokkaDarkModeItem ? JSON.parse(dokkaDarkModeItem) : false
                |      var theme = '${config.defaultTheme()}';
                |      if (isDarkMode === true) {
                |        theme = '${config.darkTheme()}';
                |      }
                |      mermaid.initialize({'theme': theme});
                |      mermaid.render('$mermaidTargetId', graphDef).then(({ svg, bindFunctions }) => {
                |          container.innerHTML = svg;
                |          // Trick to make the graph takes only the required height.
                |          document.getElementById('$mermaidTargetId').removeAttribute('height')
                |          bindFunctions?.(element);
                |      });
                |    }, 0);
                |  }
                |  updateGraph$rand();
                |  
                |  var themeToggleButton = document.getElementById('theme-toggle-button');
                |  themeToggleButton.addEventListener('click', () => {
                |    updateGraph$rand();
                |  });
                |});
                """.trimMargin()
            }
        }
    }
}
