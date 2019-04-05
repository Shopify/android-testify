# Testify — Android Screenshot Testing

Add screenshots to your Android tests

|  ![](https://travis-ci.com/Shopify/android-testify.svg?token=sYqH7qszpSqeVUazMVxV&branch=master)  |   [ ![Download](https://api.bintray.com/packages/shopify/shopify-android/testify/images/download.svg) ](https://bintray.com/shopify/shopify-android/testify/_latestVersion)  |  [ ![Download](https://api.bintray.com/packages/shopify/shopify-android/testify-plugin/images/download.svg) ](https://bintray.com/shopify/shopify-android/testify-plugin/_latestVersion)  |
|:------:|:-------:|:------:|
| Travis | Library | Plugin |


Expand your test coverage by including the View-layer. Testify allows you to easily set up a variety of screenshot tests in your application. Capturing a screenshot of your view gives you a new tool for monitoring the quality of your UI experience. It's also an easy way to review changes to your UI. Once you've established a comprehensive set of screenshots for your application, you can use them as a "visual dictionary". In this case, a picture really is worth a thousand words; it's easy to catch unintended changes in your view rendering by watching for differences in your captured images.

Testify screenshot tests are built on top of [Android Instrumentation tests](https://developer.android.com/training/testing/unit-testing/instrumented-unit-tests) and so integrate seamlessly with your existing test suites. You can run tests and capture screenshots from within Android Studio or using the Gradle command-line tools. Testify also works well with most Continuous Integration services. 

You can easily capture screenshots with different resolutions, orientations, API versions and languages by simply configuring different emulators. Testify natively supports grouping screenshot tests by device characteristics. Testify captures a bitmap of your specified View after all layout and draw calls have completed so you know that you're capturing an authentic rendering representative of what your users will see in your final product.

# Download
```groovy
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath "com.shopify.testify:plugin:0.9.6"
    }
}

apply plugin: 'com.shopify.testify'
```

# Library projects

Testify can infer most properties for your project, but for library projects you need to provide an `applicationPackageId`. This is necessary for the Testify plugin to correctly synchronize your test images with your emulator when running tests.

```groovy
testify {
    applicationPackageId "com.example.library"
}
```

# Writing a test

```kotlin
@RunWith(AndroidJUnit4::class)
class MainActivityScreenshotTest {

    @get:Rule var rule = ScreenshotRule(MainActivity::class.java)

    @ScreenshotInstrumentation
    @Test
    fun default() {
        rule.assertSame()
    }
}
```
For additional testing scenarios, please refer to the [recipe book](RECIPES.md).


# License

    MIT License
    
    Copyright (c) 2019 Shopify
    
    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:
    
    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.
    
    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
