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

package com.shopify.testify

import org.gradle.api.Project

class InputSettingsExtension {
    String applicationPackageId
    String testPackageId
    long pullWaitTime = 0
    String testRunner
    String baselineSourceDir
    String moduleName;
    String testContextId
    boolean useSdCard = false
    String outputFileNameFormat

    String getTestRunner() {
        if (testRunner == null) {
            testRunner = "android.support.test.runner.AndroidJUnitRunner"
        }
        return testRunner
    }

    String getTestContextId() {
        if (testContextId == null) {
            testContextId = getTestPackageId()
        }
        return testContextId
    }

    String getTestPackageId() {
        if (testPackageId == null) {
            return "${applicationPackageId}.test"
        }
        return testPackageId
    }

    static void validate(Project project) {
        InputSettingsExtension extension = project.getExtensions().findByType(InputSettingsExtension)
        if (extension == null || extension.applicationPackageId == null) {
            throw new Exception("You must define a `testify` extension block in your build.gradle")
        }
    }
}
