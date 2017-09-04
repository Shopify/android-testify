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
package com.shopify.testify.modification;

import android.graphics.Rect;
import android.text.method.TransformationMethod;
import android.view.View;

import java.util.Arrays;

class StaticPasswordTransformationMethod implements TransformationMethod {

    //CHECKSTYLE:OFF
    private static char DOT = '\u2022';
    //CHECKSTYLE:ON
    private static StaticPasswordTransformationMethod instance;

    static StaticPasswordTransformationMethod getInstance() {
        if (instance != null) {
            return instance;
        }
        instance = new StaticPasswordTransformationMethod();
        return instance;
    }

    public CharSequence getTransformation(CharSequence source, View v) {
        return new ReplacementCharSequence(source);
    }

    @Override
    public void onFocusChanged(View view, CharSequence sourceText, boolean focused, int direction, Rect previouslyFocusedRect) {
        // not used
    }

    private static class ReplacementCharSequence implements CharSequence {

        private CharSequence sourceCharSequence;

        ReplacementCharSequence(CharSequence source) {
            sourceCharSequence = source;
        }

        @Override
        public int length() {
            return sourceCharSequence.length();
        }

        @Override
        public char charAt(int index) {
            return DOT;
        }

        @Override
        public CharSequence subSequence(int start, int end) {
            char[] chars = new char[end - start];
            Arrays.fill(chars, DOT);
            return new String(chars);
        }
    }
}
