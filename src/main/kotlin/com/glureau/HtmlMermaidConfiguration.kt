package com.glureau

import org.jetbrains.dokka.plugability.ConfigurableBlock

data class HtmlMermaidConfiguration(
    val lightTheme: String?,
    val darkTheme: String?,
) : ConfigurableBlock

fun HtmlMermaidConfiguration?.defaultTheme() = this?.lightTheme ?: "default"
fun HtmlMermaidConfiguration?.darkTheme() = this?.darkTheme ?: "dark"
