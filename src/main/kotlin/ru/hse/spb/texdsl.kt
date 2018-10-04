package ru.hse.spb

import java.lang.StringBuilder

private val TEX_INDENT = "  "

@DslMarker
annotation class TexMaker

interface Element {
    fun render(builder: StringBuilder, indent: String)
}

class TextElement(val text: String) : Element {
    override fun render(builder: StringBuilder, indent: String) {
        builder.append("$indent$text\n")
    }
}

@TexMaker
abstract class Tag(val name: String) : Element {
    val children = arrayListOf<Element>()

    override fun render(builder: StringBuilder, indent: String) {
        builder.append("$indent\\begin{$name}\n")
        children.forEach { it.render(builder, indent + TEX_INDENT) }
        builder.append("$indent\\end{$name}\n")
    }
}

abstract class TagWithText(name: String) : Tag(name) {
    operator fun String.unaryPlus() {
        children.add(TextElement(this))
    }
}



/*fun document(init: Document.() -> Unit): Document {
    val document = Document()
    document.init()
    return document
}*/