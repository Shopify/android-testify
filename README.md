# android-testify

[![GitHub license](https://img.shields.io/badge/license-MIT-lightgrey.svg)](https://github.com/Shopify/android-testify/blob/master/LICENSE)
[![Build Status](https://circleci.com/gh/Shopify/android-testify/tree/master.svg?style=shield&circle-token=a2199afd9a696583d3c35b18d80eba7a0422560b)](https://circleci.com/gh/Shopify/android-testify/tree/master)
[ ![Library](https://api.bintray.com/packages/shopify/shopify-android/testify/images/download.svg) ](https://bintray.com/shopify/shopify-android/testify/_latestVersion)
[ ![Plugin](https://api.bintray.com/packages/shopify/shopify-android/testify-plugin/images/download.svg)](https://bintray.com/shopify/shopify-android/testify-plugin/_latestVersion)

### Set Up


To use Testify, you need to add the Testify plugin to your `build.gradle`:

```
buildscript {
    repositories {
        maven { url 'http://dl.bintray.com/shopify/shopify-android/' }
    }
    dependencies {
        classpath 'com.shopify.testify:plugin:0.5.4'
    }
}
apply plugin: 'com.shopify.testify'
```

You must also configure the Testify plugin settings by adding a `testify` block to your `build.gradle`:

```
testify {
    moduleName "app"
    applicationPackageId "com.example.myapp"
    testContextId "com.example.myapp.debug"
    testPackageId "com.example.myapp.test"
    testRunner "android.support.test.runner.AndroidJUnitRunner"
    baselineSourceDir "./app/src/androidTest/assets/screenshots"
    pullWaitTime 0
}
```

- `moduleName` : [REQUIRED] The name of your application module. By default this is `app`
- `applicationPackageId`: [REQUIRED] Your application package name. ex. `com.example.myapp`
- `testContextId`: [OPTIONAL] The package name for the Context of the application being tested. This is used to find the baseline images when running tests. This is typically the same as your application package id.
- `testPackageId`: [OPTIONAL] The package name for your test pacakge. ex. `com.example.myapp.test`
- `testRunner`: [OPTIONAL] Fully qualified name for any custom test runner you're using. Defaults to `android.support.test.runner.AndroidJUnitRunner`
- `baselineSourceDir`: [REQUIRED] The relative path to your screenshot assets.
- `pullWaitTime`: [OPTIONAL] The amount of time in milliseconds to wait for files to copy locally. Defaults to `0`.

### How to run a single test

Take the following test code:

```
package com.shopify.testifysample;

import com.shopify.testify.ScreenshotTest;
import com.shopify.testify.ScreenshotTestCase;

public class BasicTests extends ScreenshotTestCase<TestHarnessActivity> {

    public BasicTests() {
        super(TestHarnessActivity.class, R.id.component_placeholder);
    }

    public void testBootstrap() throws Exception {
        new ScreenshotTest(this, R.layout.test_bootstrap).assertSame();
    }

    public void testMainActivity() throws Exception {
        new ScreenshotTest(this, R.layout.activity_main).assertSame();
    }
}
```

If you wanted to run a single test, you can use the following command:

`./gradlew screenshotTest -PtestClass=com.shopify.testifysample.BasicTests -PtestName=testBootstrap`

Similarly, you can run & record the test in one step using:

`./gradlew screenshotRecord -PtestClass=com.shopify.testifysample.BasicTests -PtestName=testBootstrap`

You can also shorten the syntax slightly to:

`./gradlew screenshotRecord -PtestClass=com.shopify.testifysample.BasicTests#testBootstrap`

### How to write a test

#### JUnit3

```
public class SampleJUnit3Tests extends ScreenshotTestCase<MyActivity> {

    public SampleJUnit3Tests() {
        super(MyActivity.class, R.id.root_view);
    }

    public void testScreenshot() throws Exception {
        new ScreenshotTest(this, R.layout.test_sample)
                .setViewModifications(new ScreenshotTest.ViewModification() {
                    @Override
                    public void modifyView(ViewGroup rootView) {
                        ((RadioButton) rootView.findViewById(R.id.radioButton)).setChecked(true);
                    }
                })
                .setEspressoActions(new ScreenshotTest.EspressoActions() {
                    @Override
                    public void performEspressoActions() {
                        onView(withId(R.id.checkBox)).perform(click());
                    }
                })
                .assertSame();
    }
}
```

#### JUnit4

```
@RunWith(AndroidJUnit4.class)
public class SampleJUnit4Tests {

    @Rule
    public final ScreenshotTestRule<MyActivity> screenshotTestRule = new ScreenshotTestRule<>(MyActivity.class);

    @Test
    @TestifyLayout(layoutId = R.layout.test_sample)
    public void screenshot() throws Exception {
        screenshotTestRule
                .setEspressoActions(new ScreenshotTest.EspressoActions() {
                    @Override
                    public void performEspressoActions() {
                        onView(withId(R.id.checkBox)).perform(click());
                    }
                })
                .setViewModifications(new ScreenshotTest.ViewModification() {
                    @Override
                    public void modifyView(ViewGroup rootView) {
                        ((RadioButton) rootView.findViewById(R.id.radioButton)).setChecked(true);
                    }
                });
    }
}

```

### How to build?

#### How to build for local development

`./gradlew install publishToMavenLocal`

#### Plugin:

- Visit https://bintray.com/profile/edit
- Set `BINTRAY_USER` to your personal profile name (from "Your Profile")
  - ex. `export BINTRAY_USER="danieljette"`
- Set `BINTRAY_KEY`. It can be found on the left menu, under "API Key"
  - ex. `export BINTRAY_KEY="8e5e06...d418b"`
- `./gradlew :plugin:bintrayUpload`

Note: You might need to bump the `VERSION_NAME` in `./plugin/build.gradle`

### Troubleshooting

#### `screenshotPull` is not working

Check that you have root access to your emulator.
Run `adb root` if necessary.

### TODO:

- update for `testify` extensions
- document all the tasks
- show how to build locally
- demonstrate sharding

### How Can I Contribute?

We welcome contributions. Follow the steps in the [CONTRIBUTING](CONTRIBUTING.md) file.

### License

The Mobile Buy SDK is provided under an MIT Licence. See the [LICENSE](LICENSE) file.
