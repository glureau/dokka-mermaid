package com.glureau

import org.jetbrains.dokka.base.DokkaBase
import org.jetbrains.dokka.plugability.DokkaPlugin
import org.jetbrains.dokka.plugability.DokkaPluginApiPreview
import org.jetbrains.dokka.plugability.PluginApiPreviewAcknowledgement
import org.jetbrains.dokka.plugability.configuration

class HtmlMermaidDokkaPlugin : DokkaPlugin() {

    private val dokkaBase by lazy { plugin<DokkaBase>() }

    val codeBlockRenderer by extending {
        dokkaBase.htmlCodeBlockRenderers providing { ctx ->
            MermaidHtmlRenderer(configuration<HtmlMermaidDokkaPlugin, HtmlMermaidConfiguration>(ctx))
        }
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

    @DokkaPluginApiPreview
    override fun pluginApiPreviewAcknowledgement(): PluginApiPreviewAcknowledgement =
        PluginApiPreviewAcknowledgement
}

