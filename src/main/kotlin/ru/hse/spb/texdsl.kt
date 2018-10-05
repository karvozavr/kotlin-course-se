package ru.hse.spb

import java.io.OutputStream


private const val TEX_INDENT = "  "

@DslMarker
annotation class TexMaker

fun renderParams(params: List<String>) =
        if (!params.isEmpty()) "[${params.joinToString(", ")}]" else ""


infix fun String.to(value: String): String {
    return "$this=$value"
}


abstract class Element {
    abstract fun render(builder: StringBuilder, indent: String)

    override fun toString(): String {
        val stringBuilder = StringBuilder()
        render(stringBuilder, "")
        return stringBuilder.toString()
    }

    fun toOutputStream(out: OutputStream) {
        val writer = out.writer()
        writer.use {
            it.append(toString())
        }
    }
}


class TextElement(private val text: String) : Element() {
    override fun render(builder: StringBuilder, indent: String) {
        text.trimIndent().lines().forEach { builder.append("$indent$it\n") }
    }
}


@TexMaker
abstract class SingleTag(val name: String, val argument: String, vararg val params: String) : Element() {

    override fun render(builder: StringBuilder, indent: String) {
        builder.append("$indent\\$name${renderParams(params.toList())}{$argument}\n")
    }
}


class UsePackage(argument: String, vararg params: String) : SingleTag("usepackage", argument, *params)
class DocumentClass(argument: String, vararg params: String) : SingleTag("documentClass", argument, *params)


class Math(private val content: String) : Element() {
    override fun render(builder: StringBuilder, indent: String) {
        builder.append("$indent\$$content\$")
    }
}


class Newline : Element() {
    override fun render(builder: StringBuilder, indent: String) {
        builder.append('\n')
    }
}


@TexMaker
open class CustomTag(val name: String, vararg val params: String) : Element() {
    val children = arrayListOf<Element>()

    operator fun String.unaryPlus() {
        children.add(TextElement(this))
    }

    override fun render(builder: StringBuilder, indent: String) {
        builder.append("$indent\\begin{$name}${renderParams(params.toList())}\n")
        children.forEach { it.render(builder, indent + TEX_INDENT) }
        builder.append("$indent\\end{$name}\n")
    }

    protected fun <T : Element> initTag(tag: T, init: T.() -> Unit): T {
        tag.init()
        children.add(tag)
        return tag
    }

    fun customTag(name: String, vararg params: String, init: CustomTag.() -> Unit) =
            initTag(CustomTag(name, *params), init)

    fun itemize(init: Enumerable.() -> Unit) = initTag(Enumerable("itemize"), init)
    fun enumerate(init: Enumerable.() -> Unit) = initTag(Enumerable("enumerate"), init)
    fun math(content: String) = children.add(Math(content))
    fun newline() = children.add(Newline())
    fun frame(frameTitle: String, vararg params: String, init: Frame.() -> Unit) = initTag(Frame(frameTitle, *params), init)
    fun align(type: Alignment.AlignType, init: Alignment.() -> Unit) = initTag(Alignment(type), init)
}


class Alignment(type: AlignType) : CustomTag(type.alignType) {
    enum class AlignType(val alignType: String) {
        CENTER("center"),
        LEFT("flushleft"),
        RIGHT("flushright")
    }
}


class Frame(private val title: String, vararg params: String) : CustomTag("frame", *params) {
    override fun render(builder: StringBuilder, indent: String) {
        builder.append("$indent\\begin{$name}${renderParams(params.toList())}\n")
        builder.append("$indent$TEX_INDENT\\frametitle{$title}\n")
        children.forEach { it.render(builder, indent + TEX_INDENT) }
        builder.append("$indent\\end{$name}\n")
    }
}


class Item : CustomTag("item") {
    override fun render(builder: StringBuilder, indent: String) {
        builder.append("$indent\\$name\n")
        children.forEach { it.render(builder, indent + TEX_INDENT) }
    }
}


class Enumerable(name: String) : CustomTag(name) {


    fun item(init: Item.() -> Unit): Item = initTag(Item(), init)
}


class Document : CustomTag("document") {
    private val headerChildren = arrayListOf<Element>()

    override fun render(builder: StringBuilder, indent: String) {
        headerChildren.forEach { it.render(builder, indent) }
        super.render(builder, indent)
    }

    fun documentClass(argument: String, vararg params: String) =
            headerChildren.add(DocumentClass(argument, *params))

    fun usepackage(argument: String, vararg params: String) =
            headerChildren.add(UsePackage(argument, *params))
}


fun document(init: Document.() -> Unit): Document {
    val document = Document()
    document.init()
    return document
}