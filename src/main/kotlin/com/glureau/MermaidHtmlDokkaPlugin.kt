package com.glureau

import org.jetbrains.dokka.CoreExtensions
import org.jetbrains.dokka.base.DokkaBase
import org.jetbrains.dokka.plugability.DokkaPlugin

class MermaidHtmlDokkaPlugin : DokkaPlugin() {

    private val dokkaBase by lazy { plugin<DokkaBase>() }

    val renderer by extending {
        CoreExtensions.renderer providing ::MermaidHtmlRenderer override dokkaBase.htmlRenderer
    }

    init {
        println("AWESOME")
    }
}