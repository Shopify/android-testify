/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2020 Shopify Inc.
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

import android.app.Activity
import android.view.View

internal class FocusModification : ViewModification(isEnabled = true) {

    private val focusAlteredSet = HashSet<View>()

    fun modify(activity: Activity) {
        if (!isEnabled) {
            return
        }

        while (activity.isAnyViewWithFocus) {
            activity.currentFocus?.let { view ->
                performModification(view)
                focusAlteredSet.add(view)
            }
        }

        focusAlteredSet.forEach { view ->
            view.restoreFocusable()
        }
        focusAlteredSet.clear()
    }

    override fun performModification(view: View) {
        view.removeFocusable()
    }

    override fun qualifies(view: View) = false

    private val Activity.isAnyViewWithFocus: Boolean
        get() {
            return this.currentFocus != null
        }

    private fun View.removeFocusable() {
        this.isFocusableInTouchMode = false
        this.isFocusable = false
    }

    private fun View.restoreFocusable() {
        this.isFocusableInTouchMode = false
        this.isFocusable = false
    }
}
