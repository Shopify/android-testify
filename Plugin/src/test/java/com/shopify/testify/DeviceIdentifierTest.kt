package com.shopify.testify

import com.nhaarman.mockitokotlin2.whenever
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
