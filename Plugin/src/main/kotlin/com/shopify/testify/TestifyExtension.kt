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

import com.shopify.testify.internal.android
import org.gradle.api.GradleException
import org.gradle.api.Project

internal data class TestifySettings(
    var applicationPackageId: String,
    var baselineSourceDir: String,
    var moduleName: String,
    var outputFileNameFormat: String?,
    var pullWaitTime: Long = 0L,
    var testRunner: String,
    var useSdCard: Boolean,
    var testContextId: String,
    var testPackageId: String,
    var applicationIdSuffix: String
) {

    fun override(extension: TestifyExtension): TestifySettings {
        if (extension.applicationPackageId?.isNotEmpty() == true) {
            this.applicationPackageId = extension.applicationPackageId!!
        }
        if (extension.applicationIdSuffix?.isNotEmpty() == true) {
            this.applicationIdSuffix = extension.applicationIdSuffix!!
        }
        if (extension.baselineSourceDir?.isNotEmpty() == true) {
            this.baselineSourceDir = extension.baselineSourceDir!!
        }
        if (extension.moduleName?.isNotEmpty() == true) {
            this.moduleName = extension.moduleName!!
        }
        if (extension.outputFileNameFormat?.isNotEmpty() == true) {
            this.outputFileNameFormat = extension.outputFileNameFormat!!
        }
        if (extension.pullWaitTime != null) {
            this.pullWaitTime = extension.pullWaitTime!!
        }
        if (extension.testRunner?.isNotEmpty() == true) {
            this.testRunner = extension.testRunner!!
        }
        if (extension.useSdCard != null) {
            this.useSdCard = extension.useSdCard!!
        }
        if (extension.testContextId?.isNotEmpty() == true) {
            this.testContextId = extension.testContextId!!
        }
        if (extension.testPackageId?.isNotEmpty() == true) {
            this.testPackageId = extension.testPackageId!!
        }
        if (this.testContextId.isEmpty()) {
            this.testContextId = createTestContextId(this.applicationPackageId, this.applicationIdSuffix)
        }
        return this
    }

    fun validate() {
        if (applicationPackageId.isEmpty()) {
            throw GradleExtensionException("""

  You must define an `applicationPackageId` in your `testify` gradle extension block:

      testify {
          applicationPackageId "com.example.app"
      }
      """)
        }
    }
}

fun createTestContextId(applicationPackageId: String, applicationIdSuffix: String): String {
    return if (applicationPackageId.isEmpty()) "" else "$applicationPackageId$applicationIdSuffix"
}

internal class TestifySettingsFactory {

    companion object {

        fun create(project: Project): TestifySettings {
            val android = project.android
            val applicationPackageId = android.defaultConfig?.applicationId ?: ""
            val assetsSet = android.sourceSets?.getByName("""androidTest""")?.assets
            val baselineSourceDir = "${assetsSet?.srcDirs?.first()?.path}/screenshots"
            val testRunner = android.defaultConfig.testInstrumentationRunner
            val applicationIdSuffix = ""
            val testContextId = createTestContextId(applicationPackageId, applicationIdSuffix)
            val testPackageId = android.testVariants?.first()?.applicationId ?: ""

            return TestifySettings(
                applicationPackageId = applicationPackageId,
                baselineSourceDir = baselineSourceDir,
                moduleName = project.name,
                outputFileNameFormat = null,
                pullWaitTime = 0L,
                testRunner = testRunner,
                useSdCard = false,
                testContextId = testContextId,
                testPackageId = testPackageId,
                applicationIdSuffix = applicationIdSuffix
            )
        }
    }
}

open class TestifyExtension {
    var applicationPackageId: String? = null
    var baselineSourceDir: String? = null
    var moduleName: String? = null
    var outputFileNameFormat: String? = null
    var pullWaitTime: Long? = null
    var testRunner: String? = null
    var useSdCard: Boolean? = null
    var testContextId: String? = null
    var testPackageId: String? = null
    var applicationIdSuffix: String? = null

    companion object {
        const val NAME = "testify"
    }
}

fun Project.validateExtension() {
    if (this.extensions.findByName(TestifyExtension.NAME) == null) {
        throw GradleExtensionException("You must define a `testify` extension block in your build.gradle")
    }
}

fun Project.getTestifyExtension(): TestifyExtension = this.extensions.findByType(TestifyExtension::class.java)!!

data class GradleExtensionException(override val message: String) : GradleException(message)
