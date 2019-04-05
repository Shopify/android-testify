/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2019 Shopify Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.shopify.testify.internal

import com.shopify.testify.testifySettings
import org.gradle.api.Project
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

internal fun Project.getDestinationImageDirectory(language: String = Device.language()): String {
    return "${project.testifySettings.baselineSourceDir}/${Device.deviceKey(language)}/"
}

private typealias OutputHandler = (String, StringBuilder) -> Unit

private val directHandler = fun(s: String, sb: StringBuilder) {
    sb.append(s)
    sb.append(System.lineSeparator())
}

private val streamedHandler = fun(s: String, sb: StringBuilder) {
    directHandler(s, sb)
    println(s)
}

fun runProcess(command: String, stream: Boolean = false): String {
    val process = Runtime.getRuntime().exec(command)
    val reader = BufferedReader(InputStreamReader(process.inputStream))
    val outputHandler: OutputHandler = if (stream) streamedHandler else directHandler

    val stringBuilder = StringBuilder()
    reader.useLines {
        it.forEach { outputHandler(it, stringBuilder) }
    }

    process.waitFor()
    return stringBuilder.toString()
}

val File.pathRelativeToCurrentDir: String
    get() = ".${this.path.removePrefix(currentDir)}"

private val currentDir: String
    get() = runProcess("pwd")

const val FILESYSTEM_TIMEOUT = 300L

sealed class AnsiFormat(private val code: String) {
    object Reset : AnsiFormat("\u001B[0m")
    object Black : AnsiFormat("\u001B[30m")
    object Red : AnsiFormat("\u001B[31m")
    object Green : AnsiFormat("\u001B[32m")
    object Yellow : AnsiFormat("\u001B[33m")
    object Blue : AnsiFormat("\u001B[34m")
    object Purple : AnsiFormat("\u001B[35m")
    object Cyan : AnsiFormat("\u001B[36m")
    object White : AnsiFormat("\u001B[37m")
    object Bold : AnsiFormat("\u001B[1m")

    override fun toString(): String {
        return code
    }
}

fun println(format: AnsiFormat, message: Any?) {
    print(format, message)
    println()
}

fun print(format: AnsiFormat, message: Any?) {
    print(format)
    print(message)
    print(AnsiFormat.Reset)
}

internal fun File.assurePath() {
    if (!this.exists()) {
        this.mkdirs()
    }
    if (!this.exists()) {
        throw RuntimeException("Destination directory ${this.path} could not be found")
    }
}

internal fun File.deleteFilesWithSubstring(substring: String) {
    listFiles().filter {
        it.nameWithoutExtension.contains(substring)
    }.forEach {
        println("    Deleting ${it.name}")
        it.delete()
    }
}

@Suppress("UNCHECKED_CAST")
internal fun <T> String.fromEnv(defaultValue: T): T {
    val envVal: String? = System.getenv(this)
    return if (envVal?.isNotEmpty() == true) {
        envVal as T
    } else {
        defaultValue
    }
}