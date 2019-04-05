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
package com.shopify.testify

import com.nhaarman.mockitokotlin2.whenever
import com.shopify.testify.internal.DeviceIdentifier
import org.junit.Test

import org.junit.Assert.assertEquals
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.mock

class DeviceIdentifierTest {

    private val formatter = mockFormatter()

    private fun mockFormatter(): DeviceIdentifier.DeviceStringFormatter {
        val formatter = mock(DeviceIdentifier.DeviceStringFormatter::class.java)

        doReturn("21").whenever<DeviceIdentifier.DeviceStringFormatter>(formatter).androidVersion
        doReturn("380dp").whenever(formatter).deviceDensity
        doReturn("1024").whenever<DeviceIdentifier.DeviceStringFormatter>(formatter).deviceHeight
        doReturn("800").whenever<DeviceIdentifier.DeviceStringFormatter>(formatter).deviceWidth
        doReturn("en").whenever(formatter).language
        doReturn("Class").whenever<DeviceIdentifier.DeviceStringFormatter>(formatter).testClass
        doReturn("method").whenever(formatter).getTestName()

        return formatter
    }

    @Test
    fun testFolderNameFormat() {
        assertEquals("21-800x1024@380dp-en", DeviceIdentifier.formatDeviceString(formatter, DeviceIdentifier.DEFAULT_FOLDER_FORMAT))
    }

    @Test
    fun testFileNameFormat() {
        assertEquals("Class_method", DeviceIdentifier.formatDeviceString(formatter, DeviceIdentifier.DEFAULT_NAME_FORMAT))
    }

    @Test
    fun testCIFormat() {
        assertEquals("21-800x1024@380dp-en#Class_method", DeviceIdentifier.formatDeviceString(formatter, "a-wxh@d-l#c_n"))
    }

    @Test
    fun testCustomFormat() {
        assertEquals("21Class380dp1024enmethod800", DeviceIdentifier.formatDeviceString(formatter, "acdhlnw"))
    }
}
