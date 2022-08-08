> **Warning**
>
> 🚚 **We have moved!** 🚧
>
> Effective August 1, 2022, all development will occur in our new repository: https://github.com/ndtp/android-testify/
> 
>

<a href="https://ndtp.github.io/android-testify/docs/migration"><img width="*" src="./7628.png"/></a>

<sub><sup>Move house vector created by pch.vector - [www.freepik.com](https://www.freepik.com/vectors/move-house)</sup></sub>

---

# Testify — Android Screenshot Testing


Add screenshots to your Android tests

<a href="https://search.maven.org/artifact/com.shopify.testify/testify"><img alt="Maven Central" src="https://img.shields.io/maven-central/v/com.shopify.testify/testify?color=%236e40ed&label=com.shopify.testify%3Atestify"/></a> <a href="https://search.maven.org/artifact/com.shopify.testify/plugin"><img alt="Maven Central" src="https://img.shields.io/maven-central/v/com.shopify.testify/plugin?color=%234da1ea&label=com.shopify.testify%3Aplugin"/></a>

---

Expand your test coverage by including the View-layer. Testify allows you to easily set up a variety of screenshot tests in your application. Capturing a screenshot of your view gives you a new tool for monitoring the quality of your UI experience. It's also an easy way to review changes to your UI. Once you've established a comprehensive set of screenshots for your application, you can use them as a "visual dictionary". In this case, a picture really is worth a thousand words; it's easy to catch unintended changes in your view rendering by watching for differences in your captured images.

Testify screenshot tests are built on top of [Android Instrumentation tests](https://developer.android.com/training/testing/unit-testing/instrumented-unit-tests) and so integrate seamlessly with your existing test suites. You can run tests and capture screenshots from within Android Studio or using the Gradle command-line tools. Testify also works well with most Continuous Integration services. 

You can easily capture screenshots with different resolutions, orientations, API versions, and languages by simply configuring different emulators. Testify natively supports grouping screenshot tests by device characteristics. Testify captures a bitmap of your specified View after all layout and draw calls have been completed so you know that you're capturing an authentic rendering representative of what your users will see in your final product.

# Set up Testify

Before building your screenshot test with Testify, make sure to set a dependency reference to the Testify plugin:

**Root build.gradle**
```groovy
buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath "com.shopify.testify:plugin:1.2.0-alpha01"
    }
}
```

**Application build.gradle**
```groovy
plugins {
    id("com.shopify.testify")
}

dependencies {
    androidTestImplementation "androidx.test:rules:1.4.0"
}
```

## Device Configuration

It is required for you to turn off animations on your test device — leaving system animations turned on in the test device might cause synchronization issues which may lead your test to fail. Turn off animations from _Settings_ by opening _Developer options_ and turning all the following options off:

- **Window animation scale**
- **Transition animation scale**
- **Animator duration scale**

You can find a recommended emulator configuration [here](https://github.com/Shopify/android-testify/wiki/Recipes#setting-up-an-emulator-to-run-the-sample).

## Android Studio Plugin

<img width="800px" src="./Plugins/IntelliJ/marketplace/screenshot_22606.png"/>

Testify screenshot tests are built on top of Android Instrumentation tests and so already integrate seamlessly with existing test suites. Screenshots can be captured directly from within Android Studio or using the Gradle command-line tools.

However, the current Android Studio support relies fully on the fact that Testify tests extend ActivityTestRule and can be invoked using the built-in support for running instrumentation tests with various commands (notably sidebar icons) in Android Studio. These are limited to run and debug commands. Android Studio has no concept of recording or pulling screenshots from the device. Thus, it requires developers to drop to the Gradle Panel or command-line to fully use Testify. 

With the installation of an Intellij-platform plugin, many common Testify actions can be seamlessly integrated into your IDE. The Testify Android Studio plugin is available for Android Studio version 4.0 through 4.2 via the Intellij Marketplace.

<a href="https://plugins.jetbrains.com/plugin/14687-android-testify--screenshot-instrumentation-tests"><img width="300px" alt="Get from Marketplace" src="./Plugins/IntelliJ/marketplace/get.png"/></a>


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

### Record a new baseline

Run all the screenshot tests in your app and update the local baseline.

```bash
./gradlew screenshotRecord
```

### Verify the tests

Run all the screenshot tests in your app and fail if any differences from the baseline are detected.

```bash
./gradlew screenshotTest
```

### Pull images from the device

Copy images from the `app_images` directory on your emulator to your local `androidTest/assets` directory.

```bash
./gradlew screenshotPull
```

### Erase any existing images from the device

Clear any baseline images that may be remaining on your emulator.

```bash
./gradlew screenshotClear
```

### Generate a YAML test report

You can optionally generate a YAML test report for offline parsing by adding `<meta-data android:name="testify-reporter" android:value="true" />` to your `AndroidManifest.xml`.
Once enabled, Testify will create a `report.yml` cataloging the statistics about the most recent test run.
You can view the report with:
```bash
./gradlew reportShow
```
You can copy the report.yml file to your local project directory with:
```bash
./gradlew reportPull
```

There are a variety of additional Gradle commands available through the Testify plugin. For advance usage, please refer to the [Plugin guide](Plugins/Gradle/README.md).

# License

    MIT License
    
    Copyright (c) 2021 Shopify
    
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
