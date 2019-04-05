---
layout: index
---

# Testify — Android Screenshot Testing

Add screenshots to your Android tests

<a href="https://travis-ci.com/Shopify/android-testify"><img alt="Travis CI" src="https://travis-ci.com/Shopify/android-testify.svg?token=sYqH7qszpSqeVUazMVxV&branch=master"/></a> <a href="https://bintray.com/shopify/shopify-android/testify/_latestVersion"><img alt="Testify library download" src="https://api.bintray.com/packages/shopify/shopify-android/testify/images/download.svg"/></a> <a href="https://bintray.com/shopify/shopify-android/testify-plugin/_latestVersion"><img alt="Testify plugin download" src="https://api.bintray.com/packages/shopify/shopify-android/testify-plugin/images/download.svg"/></a>
---

Expand your test coverage by including the View-layer. Testify allows you to easily set up a variety of screenshot tests in your application. Capturing a screenshot of your view gives you a new tool for monitoring the quality of your UI experience. It's also an easy way to review changes to your UI. Once you've established a comprehensive set of screenshots for your application, you can use them as a "visual dictionary". In this case, a picture really is worth a thousand words; it's easy to catch unintended changes in your view rendering by watching for differences in your captured images.

Testify screenshot tests are built on top of [Android Instrumentation tests](https://developer.android.com/training/testing/unit-testing/instrumented-unit-tests) and so integrate seamlessly with your existing test suites. You can run tests and capture screenshots from within Android Studio or using the Gradle command-line tools. Testify also works well with most Continuous Integration services. 

You can easily capture screenshots with different resolutions, orientations, API versions and languages by simply configuring different emulators. Testify natively supports grouping screenshot tests by device characteristics. Testify captures a bitmap of your specified View after all layout and draw calls have completed so you know that you're capturing an authentic rendering representative of what your users will see in your final product.

# Set up Testify

Before building your screenshot test with Testify, make sure to set a dependency reference to the Testify plugin:

```groovy
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath "com.shopify.testify:plugin:1.0.0-alpha1"
    }
}

apply plugin: 'com.shopify.testify'
```

It is required for you to turn off animations on your test device — leaving system animations turned on in the test device might cause synchronization issues which may lead your test to fail. Turn off animations from _Settings_ by opening _Developer options_ and turning all the following options off:

- **Window animation scale**
- **Transition animation scale**
- **Animator duration scale**

# Library projects

Testify can infer most properties for your project, but for library projects you need to provide an `applicationPackageId`. This is necessary for the Testify plugin to correctly synchronize your test images with your emulator when running tests.

```groovy
testify {
    applicationPackageId "com.example.library"
}
```

# Write a test

Testify is a subclass of Android's [`ActivityTestRule`](https://developer.android.com/reference/androidx/test/rule/ActivityTestRule.html). The testing framework launches the activity under test before each test method annotated with [`@Test`](https://junit.org/junit4/javadoc/latest/org/junit/Test.html) and before any method annotated with [`@Before`](http://junit.sourceforge.net/javadoc/org/junit/Before.html). 

Each screenshot test method must be annotated with the `@ScreenshotInstrumentation` annotation.

Within your test method, you can configure the `Activity` as needed and call `assertSame()` to capture and validate your UI. The framework handles shutting down the activity after the test finishes and all methods annotated with [`@After`](http://junit.sourceforge.net/javadoc/org/junit/After.html) are run.

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

# Update your baseline

Testify works by referencing a PNG baseline found in your `androidTest/assets` directory for each test case that you write. As you write and run tests, an updated baseline image is maintained on your device or emulator. In order to update the baseline, you need to copy or pull the image from the device to your local development environment. Testify offers a variety of Gradle tasks to simplify the copying of your baseline images.

### Pull images from the device

Copy images from the `app_images` directory on your emulator to your local `androidTest/assets` directory.

```bash
./gradlew screenshotPull
```

### Record a new baseline

Run all the screenshot tests in your app and update the local baseline.

```bash
./gradlew screenshotRecord
```

### Verify the tests

Run all the screenshot tests in your app and fail if any differences from the baseline are detected.

```bash
./gradlew :screenshotTest
```

### Erase any existing images from the device

Clear any baseline images that may be remaining on your emulator.

```bash
./gradlew :screenshotClear
```

There are a variety of additional Gradle commands available through the Testify plugin. For advance usage, please refer to the [Plugin guide](Plugin/README.md).

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
