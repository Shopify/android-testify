/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2021 Shopify Inc.
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
package com.shopify.testify.report

import com.shopify.testify.internal.exception.ActivityMustImplementResourceOverrideException
import com.shopify.testify.internal.exception.ActivityNotRegisteredException
import com.shopify.testify.internal.exception.AssertSameMustBeLastException
import com.shopify.testify.internal.exception.MissingAssertSameException
import com.shopify.testify.internal.exception.MissingScreenshotInstrumentationAnnotationException
import com.shopify.testify.internal.exception.NoScreenshotsOnUiThreadException
import com.shopify.testify.internal.exception.RootViewNotFoundException
import com.shopify.testify.internal.exception.ScreenshotBaselineNotDefinedException
import com.shopify.testify.internal.exception.ScreenshotDirectoryNotFoundException
import com.shopify.testify.internal.exception.ScreenshotIsDifferentException
import com.shopify.testify.internal.exception.TestMustLaunchActivityException
import com.shopify.testify.internal.exception.TestMustWrapContextException
import com.shopify.testify.internal.exception.UnexpectedOrientationException
import com.shopify.testify.internal.exception.ViewModificationException
import kotlin.reflect.KClass

/**
 * Cause of a test failure
 */
@Suppress("unused")
enum class ErrorCause(val klass: KClass<*>) {
    ACTIVITY_OVERRIDE(ActivityMustImplementResourceOverrideException::class),
    ASSERT_LAST(AssertSameMustBeLastException::class),
    DIFFERENT(ScreenshotIsDifferentException::class),
    LAUNCH_ACTIVITY(TestMustLaunchActivityException::class),
    NO_ACTIVITY(ActivityNotRegisteredException::class),
    NO_ANNOTATION(MissingScreenshotInstrumentationAnnotationException::class),
    NO_ASSERT(MissingAssertSameException::class),
    NO_BASELINE(ScreenshotBaselineNotDefinedException::class),
    NO_DIRECTORY(ScreenshotDirectoryNotFoundException::class),
    NO_ROOT_VIEW(RootViewNotFoundException::class),
    UI_THREAD(NoScreenshotsOnUiThreadException::class),
    UNEXPECTED_ORIENTATION(UnexpectedOrientationException::class),
    VIEW_MODIFICATION(ViewModificationException::class),
    WRAP_CONTEXT(TestMustWrapContextException::class),
    UNKNOWN(Throwable::class);

    lateinit var description: String

    companion object {
        internal fun match(throwable: Throwable): ErrorCause {
            val cause = values().find { it.klass == throwable::class } ?: UNKNOWN
            cause.description = throwable.message?.replace("\n", " ")?.trim() ?: "Unknown"
            return cause
        }
    }
}
