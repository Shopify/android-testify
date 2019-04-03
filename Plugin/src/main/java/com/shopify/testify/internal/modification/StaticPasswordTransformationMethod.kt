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
package com.shopify.testify.internal.modification

import android.graphics.Rect
import android.text.method.TransformationMethod
import android.view.View

import java.util.Arrays

internal class StaticPasswordTransformationMethod : TransformationMethod {

    override fun getTransformation(source: CharSequence, v: View): CharSequence {
        return ReplacementCharSequence(source)
    }

    override fun onFocusChanged(view: View, sourceText: CharSequence, focused: Boolean, direction: Int, previouslyFocusedRect: Rect) {
        // not used
    }

    class ReplacementCharSequence(private val sourceCharSequence: CharSequence) : CharSequence {
        override val length: Int
            get() = sourceCharSequence.length

        override fun get(index: Int) = DOT

        override fun subSequence(startIndex: Int, endIndex: Int): CharSequence {
            val chars = CharArray(endIndex - startIndex)
            Arrays.fill(chars, DOT)
            return String(chars)
        }

    }

    companion object {

        //CHECKSTYLE:OFF
        private val DOT = '\u2022'
        //CHECKSTYLE:ON
        private var instance: StaticPasswordTransformationMethod? = null

        fun getInstance(): StaticPasswordTransformationMethod? {
            if (instance != null) {
                return instance
            }
            instance = StaticPasswordTransformationMethod()
            return instance
        }
    }
}
