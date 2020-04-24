# Testify Change Log

## 1.0.0-beta1 -- March 21, 2020

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
- Testify will now clear keyboard focus by default

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
