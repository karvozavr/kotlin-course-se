package ru.hse.spb

import org.junit.Assert.assertEquals
import org.junit.Test

class TexDSLTest {

    @Test
    fun testDocument() {
        val sb = StringBuilder()
        document {
            documentClass("article", "12pt")
            usepackage("babel", "russian")
        }.render(sb, "")
        val expected = """
            \documentclass[12pt]{article}
            \usepackage[russian]{babel}
            \begin{document}
            \end{document}
        """.trimIndent()
        assertEquals(expected, sb.toString().trimIndent())
    }

    @Test
    fun testItemize() {
        val sb = StringBuilder()
        document {
            documentClass("article", "12pt")
            itemize {
                item { +"Hello 1" }
                item { +"Hello 2" }
                item {
                    enumerate {
                        item { +"Hi 1" }
                        item { +"Hi 2" }
                    }
                }
            }
        }.render(sb, "")
        val expected = """
            \documentclass[12pt]{article}
            \begin{document}
              \begin{itemize}
                \item
                  Hello 1
                \item
                  Hello 2
                \item
                  \begin{enumerate}
                    \item
                      Hi 1
                    \item
                      Hi 2
                  \end{enumerate}
              \end{itemize}
            \end{document}
        """.trimIndent()
        assertEquals(expected, sb.toString().trimIndent())
    }

    @Test
    fun testMath() {
        val sb = StringBuilder()
        document {
            math("2 + \\varepsilon 3")
            newline()
        }.render(sb, "")
        val expected = """
            \begin{document}
              $2 + \varepsilon 3$
            \end{document}
        """.trimIndent()
        assertEquals(expected, sb.toString().trimIndent())
    }

    @Test
    fun testFrame() {
        val actual = document {
            frame("Frame 1", "arg" to "value") {
                +"Some text"
            }
            frame("Frame 2") {
                +"Other text"
            }
        }.toString().trimIndent()

        val expected = """
            \begin{document}
              \begin{frame}[arg=value]
                \frametitle{Frame 1}
                Some text
              \end{frame}
              \begin{frame}
                \frametitle{Frame 2}
                Other text
              \end{frame}
            \end{document}
        """.trimIndent()

        assertEquals(expected, actual)
    }

    @Test
    fun testCustomTag() {
        val actual = document {
            customTag("pyglist", "language" to "kotlin") {
                +"""
               |val a = 1
               |
                """
            }
        }.toString().trimIndent()

        val expected = """
            \begin{document}
              \begin{pyglist}[language=kotlin]
                |val a = 1
                |
              \end{pyglist}
            \end{document}
        """.trimIndent()

        assertEquals(expected, actual)
    }

    @Test
    fun testAlign() {
        val actual = document {
            align(Alignment.AlignType.CENTER) {
                +"Aligned text"
            }
        }.toString().trimIndent()

        val expected = """
            \begin{document}
              \begin{center}
                Aligned text
              \end{center}
            \end{document}
        """.trimIndent()

        assertEquals(expected, actual)
    }

    @Test
    fun testComplex() {
        val rows = listOf("line 1", "line 2", "line 3")
        val actual = document {
            documentClass("beamer")
            usepackage("babel", "russian" /* varargs */)
            frame("frametitle", "arg1" to "arg2") {
                itemize {
                    for (row in rows) {
                        item { +"$row text" }
                    }
                }

                customTag("pyglist", "language" to "kotlin") {
                    +"""
                       |val a = 1
                       |
                        """
                }
            }
        }.toString().trimIndent()

        val expected = """
            \documentClass{beamer}
            \usepackage[russian]{babel}
            \begin{document}
              \begin{frame}[arg1=arg2]
                \frametitle{frametitle}
                \begin{itemize}
                  \item
                    line 1 text
                  \item
                    line 2 text
                  \item
                    line 3 text
                \end{itemize}
                \begin{pyglist}[language=kotlin]
                  |val a = 1
                  |
                \end{pyglist}
              \end{frame}
            \end{document}
        """.trimIndent()

        assertEquals(expected, actual)
    }
}