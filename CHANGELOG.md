# Testify Change Log

## Unreleased

There are no unreleased changes

---

## 1.1.0-beta3

### Library

### Gradle Plugin

#### Bug fixes

- Several internal changes to support Gradle 7.
- Fix https://github.com/Shopify/android-testify/issues/225
    Apply annotations to task properties for up-to-date checks. Adds missing annotations on public properties in Tasks to assist with up-to-date checks. Missing annotations are now errors in Gradle 7.0+.
- Fix https://github.com/Shopify/android-testify/issues/234
    Using the Testify plugin with AGP 7+ would generate the error "Cannot query the value of property 'applicationId' because configuration of project ':app' has not completed yet". Fixed by catching the error and providing a sensible default. In the case where the test packaged ID is incorrectly inferred, the user can specify `testify { testPackageId "my.custom.package.test" }` in their build.gradle to override the inferred value.

### Library

#### Changes

- Renamed the `rootViewId` setter on `ScreenshotTestRule` for better Java interoperability. It has been renamed to `setRootViewIdResource`.

---

## 1.1.0-beta2

### Library

#### Bug fixes

- Fix "ParallelPixelProcessor.kt:90 - java.lang.IndexOutOfBoundsException: index=315250 out of bounds (limit=315250)"
    https://github.com/Shopify/android-testify/issues/215

#### Updates

- AGP from 4.2.0-beta6 to 4.2.0

### Sample

#### Changes

-  Remove kotlin-android-extensions from Sample and replace with viewBinding


## 1.1.0-beta1

### Library

#### Changes

- Optional constructor argument enableReporter added to ScreenshotRule. Allows you to specify whether to run the reporter for this test rule.
- TestifyFeatures now support multiple named strings.
- PixelCopyCapture can be enabled using either "testify-experimental-capture" or "testify-pixelcopy-capture" in the AndroidManifest
- FuzzyCompare (setExactness) is now multi-threaded and significantly faster.
- ScreenshotRule constructor argument activityClass is now `protected`
- ScreenshotRule constructor argument rootViewId is now `protected`
- ScreenshotRule constructor argument launchActivity is now `protected`
- ScreenshotRule.testNameComponents is now `public`
- ScreenshotRule.fullyQualifiedTestPath is now `public`
- ScreenshotRule.getRootView is now `public`
- ScreenshotRule.instrumentationPrintln is now `public`
- ScreenshotRule.isRecordMode is now `public`
- ScreenshotRule.getModuleName is now `public`
- ScreenshotRule.beforeActivityLaunched() is now annotated with `@CallSuper`
- ScreenshotRule.afterActivityLaunched() is now annotated with `@CallSuper`

#### Added

- Added `TestifyFeatures.GenerateDiffs`. When enabled, will output a `.diff.png` alongside existing baseline images. These images are high-contrast images where each difference, regardless of how minor, are indicated in red against a black background. See the `generateDiffs` test in `ScreenshotRuleExampleTests` for an example. Diff images will be pulled from the device when running `screenshotPull`.
- ScreenshotRule now supports the generation of YAML test reports.
- Added `Reporter` feature flag to TestifyFeatures. Allows you to enable test reporting.
- Added method `ScreenshotRule.beforeAssertSame()`. This method is invoked immediately before assertSame and before the activity is launched.
- Added method `ScreenshotRule.beforeInitializeView(activity: Activity)`. This method is invoked prior to any view modifications and prior to layout inflation.
- Added method `ScreenshotRule.afterInitializeView(activity: Activity)`. This method is invoked after layout inflation and all view modifications have been applied.
- Added method `ScreenshotRule.beforeScreenshot(activity: Activity)`. This method is invoked immediately before the screenshot is taken.
- Added method `ScreenshotRule.afterScreenshot(activity: Activity, currentBitmap: Bitmap?)`. This method is invoked immediately after the screenshot has been taken.
- Added method `ScreenshotRule.applyViewModifications(parentView: ViewGroup)`. This method is called on the parent view to make runtime modifications to the view properties or layout.

#### Updates:

- Compile and Target SDK from 29 to 30
- AGP from 4.1.0 to 4.2.0-beta6
- Gradle from 6.5 to 6.7.1
- Kotlin from 1.3.72 to 1.4.31
- AppCompat from 1.1.0 to 1.2.0
- Espresso from 3.2.0 to 3.3.0
- JUnit from 1.1.1 to 1.1.2
- Test Rules from 1.2.0 to 1.3.0
- Test Runner from 1.1.1 to 1.3.0

### Gradle Plugin

#### Added

- Added task `reportShow` to print the test result report to the console.
- Added task `reportPull` to copy the report file from the device and wait for it to be committed to disk.


### Sample

#### Updates:

- Material from 1.1.0 to 1.3.0
- MockitoKotlin from 2.1.0 to 2.2.0

---

## 1.0.0-rc3

### Library

#### Changes

- ScreenshotUtility is now public
- ScreenshotBaselineNotDefinedException now reports the expected device key

#### Added

- Public method getRootViewId() has been added to ScreenshotRule

## 1.0.0-rc2

### Library

#### Bug fixes

- Increase the timeout values on orientation change. Addresses various `Failed to apply requested rotation` and `Activity did not resume` errors when invoking `setOrientation`.

#### Updates

- Android Gradle Plugin to 4.1.0
- Gradle Wrapper to 6.5

### Gradle Plugin

#### Bug fixes

- Access task names lazily via names property. We were previously accessing task names in a way which resulted in early configuration of tasks resulting in Gradle failing to sync on the latest versions of Gradle and Android Gradle Plugin when custom lint checks were used in a project. Likely related to https://issuetracker.google.com/issues/67482030#comment2.
Use the `TaskContainer.names` which doesn't cause all tasks to be resolved immediately.

### Sample App

- Update Sample application to use a Pixel 3a API 30 baseline emulator.

---

## 1.0.0-rc1

#### Changes

- Replace Travis CI with GitHub Actions. https://github.com/Shopify/android-testify/actions

### Library

#### Bug fixes

- Fix https://github.com/Shopify/android-testify/issues/138
    Introduce the `setFocusTarget` method on `ScreenshotRule` which allows for keyboard focus to be placed on an explicit View. 

- Fix https://github.com/Shopify/android-testify/issues/165
    Increase the timeout on the ActivityLifecycleMonitor to 5 seconds to allow for the rotation to complete.
    Deregister the lifecycle callback.

- Fix https://github.com/Shopify/android-testify/issues/166
    Replace the existing FuzzyCompare algorithm with CIEDE2000. Calculate the colour difference value between two colours in lab space.
    The CIELAB color space (also known as CIE L* a* b* or sometimes abbreviated as simply "Lab" color space) is a color space defined by the International Commission on Illumination (CIE) in 1976.
    It expresses color as three values: L* for the lightness from black (0) to white (100), a* from green (−) to red (+), and b* from blue (−) to yellow (+).
    CIELAB was designed so that the same amount of numerical change in these values corresponds to roughly the same amount of visually perceived change.
    The CIE calls their distance metric ΔE * ab where delta is a Greek letter often used to denote difference, and E stands for Empfindung; German for "sensation".
    If deltaE < 1 then the difference can't recognized by human eyes.

### Plugin

#### Changed

- The gradle plugin has moved to `Plugins/Gradle`

---

## 1.0.0-beta5

### Library

#### New

- Add defineExclusionRects method to ScreenshotRule. You can now use defineExclusionRects to exclude regions from a bitmap comparison.

### Updates

- Android Gradle Plugin to 4.0.0
- AndroidX Junit to 1.1.1
- AndroidX Test Rules to 1.2.0
- Kotlin to 1.3.72
- Mockito to 3.3.3

---

## 1.0.0-beta4 -- June 17, 2020

### Plugin

#### New

- Added `autoImplementLibrary` member to the `testify` extension. Defaults to `true`.
    When set to `false`, prevents the Plugin from automatically adding a Library `androidTestImplementation` dependency to your project.
    This is useful for local debugging or if you require a different version of the library and plugin.

### Library

#### Bug fixes
- Fixed issue #153 - Orientation change will now be reliably applied regardless of how many times you invoke `setOrientation` in a single test class.

## 1.0.0-beta3 -- June 11, 2020

### Library

#### Bug Fixes
- Fixed issue #151. Plugin correctly displays Locale string with an underscore, not a dash. e.g. `en_US`, not `en-US`

## 1.0.0-beta2 -- May 20, 2020

### Library

#### Bug Fixes
- Fixed issue #148. Can now correctly override both enabled and disabled features using an AndroidManifest meta-data tag.


## 1.0.0-beta1 -- May 2, 2020

Breaking changes introduced. Bumped Testify to 1.0.0-beta1

### Library

#### New

- :warning: Screenshot images now are written to subdirectories under the `screenshot` directory on device. Screenshot paths on device now include the full device key and such are properly defined to fully represent the device configuration used to generate the images.
    This can be a breaking change if you use custom scripting to pull/access screenshot images from the emulator.
- :warning: The emulator locale is now encoded into the device key by default. Previously, only the language was encoded in the key. You can disable this behavior using the `Locale` (`testify-experimental-locale`) feature.
    This change will require you to rename your baseline image directory to include the full locale path.
- [TestifyFeatures](https://github.com/Shopify/android-testify/blob/7d0833b2cfedf05d4084d048d165d5f6646a8cba/Library/src/main/java/com/shopify/testify/TestifyFeatures.kt)
    Enable or disable some features at runtime via manifest entry or in code.
- [ScreenshotRule.withExperimentalFeatureEnabled](https://github.com/Shopify/android-testify/blob/7d0833b2cfedf05d4084d048d165d5f6646a8cba/Library/src/main/java/com/shopify/testify/ScreenshotRule.kt#L211)
    Used in conjunction with `TestifyFeatures`, you can selectively enable an experimental feature on the test rule. Features are reset after `assertSame` is called.
- [TestifyLayout](https://github.com/Shopify/android-testify/blob/7d0833b2cfedf05d4084d048d165d5f6646a8cba/Library/src/main/java/com/shopify/testify/annotation/TestifyLayout.kt) can now accept a layout resource name or a layout resource ID.
    This is useful for library projects where resource IDs are not stable.
- Two new bitmap capture algorithms have been added. You can now select between PixelCopy, Canvas and DrawingCache capture methods.
    These can be enabled with by passing either CanvasCapture or PixelCopyCapture to `withExperimentalFeatureEnabled`, or by enabling `testify-canvas-capture` or `testify-experimental-capture` in your manifest.

#### Bug Fixes

- Fully support changing locale during testing on API 19+
- Correctly access external files directory on API 28+
- Correctly detect the locale on API 22
- `SoftwareRenderViewModification` now qualifies all views, not just `ImageView`
- setFontScale now works correctly on API 21+

#### Testing changes

- Add `TestActivity` to androidTest configuration of Library for testing
- Update Library baseline test images to support `en_US` locale
- Add tests for `Experimental PixelCopy` API
- Update `DeviceIdentifierTest` to work with locale (not language)
- Add tests for `TestifyFeatures`

#### Updates

- Gradle from 4.10.2 to 6.2.1
- Android Gradle Plugin from 3.3.2 to 3.6.1
- ktlint from 0.29.0 to 0.36.0
- Compile and Target SDK from 28 to 29
- AndroidX ConstraintLayout from 2.0.0-alpha3 to 2.0.0-beta4
- AndroidX AppCompat from 1.0.0 to 1.1.0
- AndroidX Core Ktx from 1.0.1 to 1.1.0
- AndroidX Test Core from 2.0.0-rc01 to 2.1.0
- AndroidX Test Espresso from 3.1.0 to 3.2.0
- Dokka from 0.9.18 to 0.10.0
- Kotlin from 1.3.21 to 1.3.70
- Mockito2 from 2.23.0 to 2.28.2
- Travis CI now downloads and installs Android SDK 29
- Split Bintray gradle script to separate file
- Kotlin stdlib8 from stdlib7

### Plugin:

#### New

- :warning: **Breaking Change**
    The `testify {}` gradle extension been restructured. Testify no longer requires the `testify` extension to be defined in your project.
    This is particularly valuable for Android library projects as Testify can now correctly infer most settings automatically.
    Most settings can now be inferred. Testify now supports multiple flavor dimensions and product flavors.
    - `testContextId` has been deleted and is no longer needed.
    - `applicationIdSuffix` has been deleted. Its value can now be inferred.
    - `installTask` has been added. You can specify which task to run to install your APK. This is automatically inferred, but you may wish to override the default value.
    - `installAndroidTestTask` has been added. You can specify which task to run to install the Android Instrumentation test package. This is automatically inferred, but you may wish to override the default value.
    You can view the inferred extension values by running `./gradlew testifySettings`
- Added `verbose` logging support. Add `-Pverbose=true` to your gradle commands to enable verbose logging. e.g. `./gradlew Sample:screenshotTest -Pverbose=true`
- The device key is now based off the emulator locale, not language. (e.g. `en_US` instead of just `en`)
- Testify plugin no longer requires `adb root` access. `screenshotPull` and `screenshotClear` can now work on any device or Google Play emulator image.

#### Bug Fixes

- `screenshotClear` will now properly delete files when running from a Windows client.
- Screenshots are now copied to the correct baseline directory when captured on a landscape emulator.
- Screenshots are now copied to the correct baseline directory regardless of emulator locale.
- Testify now more accurately detects the appropriate `install` task for your project. Testify relies on this to correctly insall your APK for testing and can now infer more project types correctly.
- Correctly set and get the locale language and region on API 22.
- Correctly support overridden display density.

#### Testing changes
- JVM tests now log status to console.

#### Updates

- Update to kotlin stdlib8 from stdlib7.

### Sample App:

- Update compile SDK from 28 to 29
- Update target SDK from 28 to 29
- Extend MaterialComponents theme instead of AppCompat theme
- Re-record baseline using locale key

---

## 1.0.0-alpha1 - April 5, 2019

https://github.com/Shopify/android-testify/commit/3a5b617cf9a1f13e31830cfbcb009393c90e3b37

Initial public release 🎉
