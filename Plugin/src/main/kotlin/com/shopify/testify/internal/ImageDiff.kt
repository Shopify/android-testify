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

import org.gradle.api.Project
import java.io.File

private fun generateDiff(sourceFile: File, diffFile: String) {
    print("    Extracting differences for ${sourceFile.nameWithoutExtension}")
    val baselineFile = File("${sourceFile.parent}/master-${sourceFile.name}")
    print(".")

    Git.fetchOriginal(sourceFile, baselineFile)
    print(".")

    sourceFile.createComparisonFile(baseline = baselineFile, output = File(diffFile))
    print(".")

    baselineFile.delete()
    println(".")
}

fun Project.generateDiffs() {

    val failedScreenshots = listFailedScreenshots()
    if (failedScreenshots.isEmpty()) {
        println("  No failed screenshot tests found.")
        return
    } else {
        println("  ${failedScreenshots.size} failed screenshot test${if (failedScreenshots.size == 1) "" else "s"} found")
    }

    println(AnsiFormat.Green, "  Generating diffs for changed screenshots...")

    failedScreenshots.forEach {
        val file = File(it)
        val dir = file.parent
        val name = file.nameWithoutExtension
        val extension = file.extension

        generateDiff(file, "$dir/$name-diff.$extension")
    }
}