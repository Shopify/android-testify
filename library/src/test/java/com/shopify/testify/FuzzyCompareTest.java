/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2017 Shopify Inc.
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
package com.shopify.testify;

import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class FuzzyCompareTest {

    @Test
    public void huesIdentical() {
        for (int hue = 0; hue < 360; hue++) {
            assertFalse(FuzzyCompare.isHueDifferent(hue, hue, 1.0f));
        }
    }

    @Test
    public void huesIdenticalMod360() {
        assertFalse(FuzzyCompare.isHueDifferent(0, 360, 1.0f));
        assertFalse(FuzzyCompare.isHueDifferent(360, 0, 1.0f));
    }

    @Test
    public void huesWithTinyDifferentAnd1PercentTolerance() {
        for (int hue = 0; hue < 360; hue++) {
            assertFalse(FuzzyCompare.isHueDifferent(hue, hue + 0.01f, 0.99f));
        }
    }

    @Test
    public void huesWithTinyDifferentAndNoTolerance() {
        for (int hue = 0; hue < 360; hue++) {
            assertTrue(FuzzyCompare.isHueDifferent(hue, hue + 0.01f, 1.0f));
        }
    }

    @Test
    public void hues5PercentDifferentWith5PercentTolerance() {
        for (int hue = 0; hue < 360; hue++) {
            assertFalse(FuzzyCompare.isHueDifferent(hue, hue + 18, 0.95f));
        }
    }

    @Test
    public void hues5PercentDifferentWith1PercentTolerance() {
        for (int hue = 0; hue < 360; hue++) {
            assertTrue(FuzzyCompare.isHueDifferent(hue, hue + 18, 0.99f));
        }
    }

    @Test
    public void hueSpecialCases() {
        assertFalse(FuzzyCompare.isHueDifferent(0, 360, 1.0f));
        assertFalse(FuzzyCompare.isHueDifferent(0, 3.6f, 0.99f));
        assertTrue(FuzzyCompare.isHueDifferent(0, 3.7f, 0.99f));
        assertFalse(FuzzyCompare.isHueDifferent(360, 3.6f, 0.99f));
        assertTrue(FuzzyCompare.isHueDifferent(360, 3.7f, 0.99f));
        assertFalse(FuzzyCompare.isHueDifferent(356.4f, 0, 0.99f));
        assertTrue(FuzzyCompare.isHueDifferent(356.3f, 0, 0.99f));
    }

    @Test
    public void valueIdentical() {
        for (float val = 0.0f; val < 1.0f; val += 0.001f) {
            assertFalse(FuzzyCompare.isValueDifferent(val, val, 1.0f));
        }
    }

    @Test
    public void valueDifferent() {
        for (float val = 0.0f; val < 1.0f; val += 0.001f) {
            assertTrue(FuzzyCompare.isValueDifferent(val, val + 0.001f, 1.0f));
        }
    }

    @Test
    public void valueIdenticalWithTolerance() {
        for (float val = 0.0f; val < 1.0f; val += 0.001f) {
            assertFalse(FuzzyCompare.isValueDifferent(val, val + 0.01f, 0.99f));
        }
    }
}
