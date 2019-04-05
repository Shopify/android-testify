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

internal object Devices {

    val count: Int
        get() {
            val result = Adb()
                .argument("devices")
                .execute()

            return result.lines().filter {
                it.isNotBlank() && !it.contains("List of devices attached")
            }.count()
        }

    val targets: Map<Int, String>
        get() {
            val map = HashMap<Int, String>()
            enumerateDevices().mapIndexed { index, s ->
                map[index] = s
            }
            return map
        }

    private fun enumerateDevices(): List<String> {
        val result = Adb()
            .argument("devices")
            .execute()

        return result.lines().filter {
            it.isNotBlank() && !it.contains("List of devices attached")
        }.map {
            it.substringBefore("\t")
        }
    }
}