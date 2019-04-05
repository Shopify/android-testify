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

import com.shopify.testify.internal.compare.FuzzyCompare
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import org.junit.Test

class FuzzyCompareTest {

    @Test
    fun huesIdentical() {
        for (hue in 0..359) {
            assertFalse(FuzzyCompare.isHueDifferent(hue.toFloat(), hue.toFloat(), 1.0f))
        }
    }

    @Test
    fun huesIdenticalMod360() {
        assertFalse(FuzzyCompare.isHueDifferent(0f, 360f, 1.0f))
        assertFalse(FuzzyCompare.isHueDifferent(360f, 0f, 1.0f))
    }

    @Test
    fun huesWithTinyDifferentAnd1PercentTolerance() {
        for (hue in 0..359) {
            assertFalse(FuzzyCompare.isHueDifferent(hue.toFloat(), hue + 0.01f, 0.99f))
        }
    }

    @Test
    fun huesWithTinyDifferentAndNoTolerance() {
        for (hue in 0..359) {
            assertTrue(FuzzyCompare.isHueDifferent(hue.toFloat(), hue + 0.01f, 1.0f))
        }
    }

    @Test
    fun hues5PercentDifferentWith5PercentTolerance() {
        for (hue in 0..359) {
            assertFalse(FuzzyCompare.isHueDifferent(hue.toFloat(), (hue + 18).toFloat(), 0.95f))
        }
    }

    @Test
    fun hues5PercentDifferentWith1PercentTolerance() {
        for (hue in 0..359) {
            assertTrue(FuzzyCompare.isHueDifferent(hue.toFloat(), (hue + 18).toFloat(), 0.99f))
        }
    }

    @Test
    fun hueSpecialCases() {
        assertFalse(FuzzyCompare.isHueDifferent(0f, 360f, 1.0f))
        assertFalse(FuzzyCompare.isHueDifferent(0f, 3.6f, 0.99f))
        assertTrue(FuzzyCompare.isHueDifferent(0f, 3.7f, 0.99f))
        assertFalse(FuzzyCompare.isHueDifferent(360f, 3.6f, 0.99f))
        assertTrue(FuzzyCompare.isHueDifferent(360f, 3.7f, 0.99f))
        assertFalse(FuzzyCompare.isHueDifferent(356.4f, 0f, 0.99f))
        assertTrue(FuzzyCompare.isHueDifferent(356.3f, 0f, 0.99f))
    }

    @Test
    fun valueIdentical() {
        var value = 0.0f
        while (value < 1.0f) {
            assertFalse(FuzzyCompare.isValueDifferent(value, value, 1.0f))
            value += 0.001f
        }
    }

    @Test
    fun valueDifferent() {
        var value = 0.0f
        while (value < 1.0f) {
            assertTrue(FuzzyCompare.isValueDifferent(value, value + 0.001f, 1.0f))
            value += 0.001f
        }
    }

    @Test
    fun valueIdenticalWithTolerance() {
        var value = 0.0f
        while (value < 1.0f) {
            assertFalse(FuzzyCompare.isValueDifferent(value, value + 0.01f, 0.99f))
            value += 0.001f
        }
    }
}
